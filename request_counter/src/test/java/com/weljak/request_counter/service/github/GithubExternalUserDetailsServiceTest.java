package com.weljak.request_counter.service.github;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import com.weljak.request_counter.domain.github.request.GithubGetUserDetailsRequestCount;
import com.weljak.request_counter.domain.github.request.GithubUserDetailsRequestCountRepository;
import com.weljak.request_counter.service.ExternalUserDetailsService;
import com.weljak.request_counter.service.external.github.exception.ExternalApiException;
import com.weljak.request_counter.util.FileLoader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EnableWireMock({
        @ConfigureWireMock(port = 443, name = "github-service", property = "weljak.com.github.base.url")
})
public class GithubExternalUserDetailsServiceTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private GithubUserDetailsRequestCountRepository repository;

    @Autowired
    private ExternalUserDetailsService service;

    @InjectWireMock("github-service")
    private WireMockServer wireMockServer;

    @BeforeAll
    static void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    void serviceShouldCreateNewEntryAndCallRepoAndHttpClient() throws IOException {
        //given
        String fileName = "mock/response/mocked_response_github_api.json";
        String jsonResponse = FileLoader.loadFileContent(fileName);
        String testLogin = "WelJak";
        String expectedId = "54286288";
        String expectedCalculations = "126";
        assertTrue(repository.findByLogin(testLogin).isEmpty());

        //when
        wireMockServer.stubFor(get("/users/" + testLogin).willReturn(
                aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)
        ));


        GithubUserDetails result = (GithubUserDetails) service.getUserDetails(testLogin);

        //then
        var entity = repository.findByLogin(testLogin);
        assertTrue(entity.isPresent());
        assertEquals(1, entity.get().getRequestCount());

        assertEquals(expectedId, result.getId());
        assertEquals(testLogin, result.getLogin());
        assertNotNull(result.getCreatedAt());
        assertEquals(expectedCalculations, result.getCalculations());
    }

    @Test
    void serviceShouldIncrementRequestCountForExistingEntry() throws IOException {
        //given
        String fileName = "mock/response/mocked_response_github_api.json";
        String jsonResponse = FileLoader.loadFileContent(fileName);
        String testLogin = "WelJak";
        String expectedId = "54286288";
        String expectedCalculations = "126";

        repository.save(new GithubGetUserDetailsRequestCount(testLogin, 12L));
        assertTrue(repository.findByLogin(testLogin).isPresent());

        //when
        wireMockServer.stubFor(get("/users/" + testLogin).willReturn(
                aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)
        ));


        GithubUserDetails result = (GithubUserDetails) service.getUserDetails(testLogin);

        //then
        var entity = repository.findByLogin(testLogin);
        assertTrue(entity.isPresent());
        assertEquals(13, entity.get().getRequestCount());

        assertEquals(expectedId, result.getId());
        assertEquals(testLogin, result.getLogin());
        assertNotNull(result.getCreatedAt());
        assertEquals(expectedCalculations, result.getCalculations());
    }

    @Test
    void serviceShouldIncrementInvocationCountEvenWhenApiReturnsError() {
        //given
        String testLogin = "WelJak";

        repository.save(new GithubGetUserDetailsRequestCount(testLogin, 5L));
        assertTrue(repository.findByLogin(testLogin).isPresent());

        //when
        wireMockServer.stubFor(get("/users/" + testLogin).willReturn(
                aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
        ));

        assertThrows(ExternalApiException.class, () -> service.getUserDetails(testLogin));

        //then
        var entity = repository.findByLogin(testLogin);
        assertTrue(entity.isPresent());
        assertEquals(6, entity.get().getRequestCount());
    }

    @Test
    void serviceShouldNullCalculationsFieldWhenFollowersCountIsZero() {
        //given
        String fileName = "mock/response/mocked_response_github_api_0_follower_count.json";
        String jsonResponse = FileLoader.loadFileContent(fileName);
        String testLogin = "WelJak";
        String expectedId = "54286288";
        assertTrue(repository.findByLogin(testLogin).isEmpty());

        //when
        wireMockServer.stubFor(get("/users/" + testLogin).willReturn(
                aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonResponse)
        ));


        GithubUserDetails result = (GithubUserDetails) service.getUserDetails(testLogin);

        //then
        var entity = repository.findByLogin(testLogin);
        assertTrue(entity.isPresent());
        assertEquals(1, entity.get().getRequestCount());

        assertEquals(expectedId, result.getId());
        assertEquals(testLogin, result.getLogin());
        assertNotNull(result.getCreatedAt());
        assertNull(result.getCalculations());
    }
}
