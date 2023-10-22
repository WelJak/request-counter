package com.weljak.request_counter.service.github;

import com.weljak.request_counter.domain.github.request.GithubGetUserDetailsRequestCount;
import com.weljak.request_counter.domain.github.request.GithubUserDetailsRequestCountRepository;
import com.weljak.request_counter.service.ExternalUserDetailsService;
import com.weljak.request_counter.service.external.github.GithubApiService;
import com.weljak.request_counter.service.external.github.GithubUserDetailsDto;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.test.context.ActiveProfiles;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class GithubExternalUserDetailsServiceTest {
    @Autowired
    private GithubUserDetailsRequestCountRepository repository;

    @MockBean(reset = MockReset.BEFORE)
    private Retrofit retrofit;


    @Autowired
    GithubUserDetailsService service;

    @AfterEach
    void cleanup() {
        repository.deleteAll();
    }

    @Test
    void serviceShouldCreateNewEntryAndCallRepoAndHttpClient() throws IOException {
        //given
        Long testId = 1234L;
        String testLogin = "testUser";
        String testName = "testname";
        String testType = "testtype";
        String testAvatarUrl = "testAvatarurl";
        ZonedDateTime testCreatedAt = ZonedDateTime.now();
        String testCalculations = "18";

        var detailsDto = new GithubUserDetailsDto(
                testAvatarUrl,
                null,
                null,
                null,
                testCreatedAt,
                null,
                null,
                1L,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                testId,
                null,
                testLogin,
                testName,
                null,
                null,
                null,
                1L,
                null,
                null,
                null,
                null,
                null,
                null,
                testType,
                null,
                null
        );

        assertTrue(repository.findByLogin(testLogin).isEmpty());

        //when
        when(retrofit.create(any())).thenReturn(mock(GithubApiService.class));
        when(retrofit.create(GithubApiService.class).getGithubUserDetails(any())).thenReturn(mock(Call.class));
        when(retrofit.create(GithubApiService.class).getGithubUserDetails(any()).execute()).thenReturn(mock(Response.class));
        when(retrofit.create(GithubApiService.class).getGithubUserDetails(testLogin).execute().body()).thenReturn(detailsDto);
        when(retrofit.create(GithubApiService.class).getGithubUserDetails(any()).execute().isSuccessful()).thenReturn(true);

        GithubUserDetails result = (GithubUserDetails) service.getUserDetails(testLogin);

        //then
        var entity = repository.findByLogin(testLogin);
        assertTrue(entity.isPresent());
        assertEquals(1, entity.get().getRequestCount());

        assertEquals(testId.toString(), result.getId());
        assertEquals(testLogin, result.getLogin());
        assertEquals(testName, result.getName());
        assertEquals(testType, result.getType());
        assertEquals(testAvatarUrl, result.getAvatarUrl());
        assertNotNull(result.getCreatedAt());
        assertEquals(testCalculations, result.getCalculations());
    }

    @Test
    void serviceShouldIncrementRequestCountForExistingEntry() throws IOException {
        //given
        Long initialRequestCount = 123L;
        Long testId = 1234L;
        String testLogin = "testUser";
        String testName = "testname";
        String testType = "testtype";
        String testAvatarUrl = "testAvatarurl";
        ZonedDateTime testCreatedAt = ZonedDateTime.now();
        String testCalculations = "18";

        repository.save(new GithubGetUserDetailsRequestCount(testLogin, initialRequestCount));


        var detailsDto = new GithubUserDetailsDto(
                testAvatarUrl,
                null,
                null,
                null,
                testCreatedAt,
                null,
                null,
                1L,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                testId,
                null,
                testLogin,
                testName,
                null,
                null,
                null,
                1L,
                null,
                null,
                null,
                null,
                null,
                null,
                testType,
                null,
                null
        );

        assertTrue(repository.findByLogin(testLogin).isPresent());

        //when
        when(retrofit.create(any())).thenReturn(mock(GithubApiService.class));
        when(retrofit.create(GithubApiService.class).getGithubUserDetails(any())).thenReturn(mock(Call.class));
        when(retrofit.create(GithubApiService.class).getGithubUserDetails(any()).execute()).thenReturn(mock(Response.class));
        when(retrofit.create(GithubApiService.class).getGithubUserDetails(testLogin).execute().body()).thenReturn(detailsDto);
        when(retrofit.create(GithubApiService.class).getGithubUserDetails(any()).execute().isSuccessful()).thenReturn(true);

        GithubUserDetails result = (GithubUserDetails) service.getUserDetails(testLogin);

        //then
        var entity = repository.findByLogin(testLogin);
        assertTrue(entity.isPresent());
        assertEquals(initialRequestCount + 1, entity.get().getRequestCount());

        assertEquals(testId.toString(), result.getId());
        assertEquals(testLogin, result.getLogin());
        assertEquals(testName, result.getName());
        assertEquals(testType, result.getType());
        assertEquals(testAvatarUrl, result.getAvatarUrl());
        assertEquals(testCreatedAt, result.getCreatedAt());
        assertEquals(testCalculations, result.getCalculations());
    }
}
