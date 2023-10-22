package com.weljak.request_counter.service.github;

import com.weljak.request_counter.domain.github.request.GithubGetUserDetailsRequestCount;
import com.weljak.request_counter.domain.github.request.GithubUserDetailsRequestCountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class GithubUserDetailsRequestCountServiceTest {
    @Autowired
    private GithubUserDetailsRequestCountRepository repository;

    @MockBean
    private Object httpClient;

    @Autowired
    GithubUserDetailsRequestCountService service;

    @Test
    void serviceShouldReturnResult() {
        //given
        String testLogin = "testUser";

        //when
        GithubUserDetails result = service.getUserDetails(testLogin);

        //then
        verify(httpClient, times(1)).getGithubUserDetails(testLogin);

        assertEquals(testId, result.getId());
        assertEquals(testLogin, result.getLogin());
        assertEquals(testName, result.getName());
        assertEquals(testType, result.getType());
        assertEquals(testAvatarUrl, result.getAvatarUrl());
        assertEquals(testCreatedAt, result.getCreatedAt());
        assertEquals(testCalculations, result.getCalculations());
    }

    @Test
    void serviceShouldCreateNewEntryAndCallRepoAndHttpClient() {
        //given
        String testLogin = "testUser";

        //when
        GithubUserDetails result = service.getUserDetails(testLogin);

        //then
        verify(httpClient, times(1)).getGithubUserDetails(testLogin);

        assertEquals(testLogin, result.getLogin());

        Optional<GithubGetUserDetailsRequestCount> resultEntity = repository.findByLogin(testLogin);
        assertTrue(resultEntity.isPresent());
        assertEquals(1, resultEntity.get().getRequestCount());

    }

    @Test
    void serviceShouldNotReturnCalculationsFieldWhenUserFollowCountIsZero() {
        //given
        String testLogin = "testUser";

        //when
        GithubUserDetails result = service.getUserDetails(testLogin);

        //then
        verify(httpClient, times(1)).getGithubUserDetails(testLogin);
        assertEquals(testLogin, result.getLogin());
        assertNull(result.getCalculations);
    }

    @Test
    void serviceShouldIncrementRequestCountForExistingEntry() {
        //given
        String testLogin = "testUser";
        Long initialRequestCount = 123L;
        repository.save(new GithubGetUserDetailsRequestCount(testLogin, initialRequestCount));

        //when
        GithubUserDetails result = service.getUserDetails(testLogin);

        //then
        verify(repository, times(1)).findByLogin(testLogin);
        verify(httpClient, times(1)).getGithubUserDetails(testLogin);
        assertEquals(testLogin, result.getLogin());

        Optional<GithubGetUserDetailsRequestCount> resultEntity = repository.findByLogin(testLogin);
        assertTrue(resultEntity.isPresent());
        assertEquals(initialRequestCount + 1, resultEntity.get().getRequestCount());
    }

    @Test
    void serviceShouldIncrementRequestCountEvenWhenExternalServiceIsUnavailable() {
        //given
        String testLogin = "testUser";
        Long initialRequestCount = 123L;
        repository.save(new GithubGetUserDetailsRequestCount(testLogin, initialRequestCount));

        //when
        GithubUserDetails result = service.getUserDetails(testLogin);
        when(httpClient.getUserDetails).thenThrow();

        //then
        verify(repository, times(1)).findByLogin(testLogin);
        verify(httpClient, times(1)).getGithubUserDetails(testLogin);
        assertEquals(testLogin, result.getLogin());

        Optional<GithubGetUserDetailsRequestCount> resultEntity = repository.findByLogin(testLogin);
        assertTrue(resultEntity.isPresent());
        assertEquals(initialRequestCount + 1, resultEntity.get().getRequestCount());
    }
}
