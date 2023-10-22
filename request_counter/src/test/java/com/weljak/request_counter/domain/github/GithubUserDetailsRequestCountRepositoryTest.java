package com.weljak.request_counter.domain.github;

import com.weljak.request_counter.domain.github.request.GithubGetUserDetailsRequestCount;
import com.weljak.request_counter.domain.github.request.GithubUserDetailsRequestCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
public class GithubUserDetailsRequestCountRepositoryTest {
    @Autowired
    private GithubUserDetailsRequestCountRepository userDetailsCountRepository;

    @BeforeEach
    void setup() {
        userDetailsCountRepository.deleteAll();
    }

    @Test
    void repoShouldSaveGivenData() {
        //given
        String testLogin = "testUser";
        Long testCount = 12L;
        GithubGetUserDetailsRequestCount requestCount = new GithubGetUserDetailsRequestCount(testLogin, testCount);

        //when
        userDetailsCountRepository.save(requestCount);

        //then
        Optional<GithubGetUserDetailsRequestCount> resultOptional = userDetailsCountRepository.findByLogin(testLogin);
        assertTrue(resultOptional.isPresent());
        GithubGetUserDetailsRequestCount resultEntity = resultOptional.get();
        assertEquals(testCount, resultEntity.getRequestCount());
        assertEquals(testLogin, resultEntity.getLogin());
    }

    @Test
    void reposShouldUpdateExistingData() {
        //given
        String testLogin = "testUser";
        Long testCount = 12L;
        GithubGetUserDetailsRequestCount requestCount = new GithubGetUserDetailsRequestCount(testLogin, testCount);
        userDetailsCountRepository.save(requestCount);

        //when
        Long updatedCount = 2345L;
        GithubGetUserDetailsRequestCount updatedRequestCount = new GithubGetUserDetailsRequestCount(testLogin, updatedCount);
        userDetailsCountRepository.save(updatedRequestCount);

        //then
        Optional<GithubGetUserDetailsRequestCount> resultOptional = userDetailsCountRepository.findByLogin(testLogin);
        assertTrue(resultOptional.isPresent());
        GithubGetUserDetailsRequestCount resultEntity = resultOptional.get();
        assertEquals(updatedCount, resultEntity.getRequestCount());
        assertEquals(testLogin, resultEntity.getLogin());

    }

    @Test
    void repoShouldDeleteData() {
        //given
        String testLogin = "testUser";
        Long testCount = 12L;
        GithubGetUserDetailsRequestCount requestCount = new GithubGetUserDetailsRequestCount(testLogin, testCount);
        userDetailsCountRepository.save(requestCount);

        //when
        userDetailsCountRepository.deleteByLogin(testLogin);

        //then
        Optional<GithubGetUserDetailsRequestCount> resultOptional = userDetailsCountRepository.findByLogin(testLogin);
        assertTrue(resultOptional.isEmpty());
    }

    @Test
    void reposShouldReturnEmptyOptionalWhenLoginNotPresentInDb() {
        //given
        String testLogin = "testUser";

        // when
        Optional<GithubGetUserDetailsRequestCount> resultOptional = userDetailsCountRepository.findByLogin(testLogin);

        //then
        assertTrue(resultOptional.isEmpty());
    }
}
