package com.weljak.request_counter.service.github;

import com.weljak.request_counter.domain.github.request.GithubGetUserDetailsRequestCount;
import com.weljak.request_counter.domain.github.request.GithubUserDetailsRequestCountRepository;
import com.weljak.request_counter.service.ExternalUserDetails;
import com.weljak.request_counter.service.ExternalUserDetailsService;
import com.weljak.request_counter.service.external.github.GithubApiService;
import com.weljak.request_counter.service.external.github.model.GithubUserDetailsDto;
import com.weljak.request_counter.service.external.github.exception.ExternalApiException;
import com.weljak.request_counter.utils.mapper.GithubApiResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubUserDetailsService implements ExternalUserDetailsService {
    private final GithubUserDetailsRequestCountRepository repository;
    private final GithubApiResponseMapper mapper;
    private final GithubApiService githubApiService;

    private static final Long INCREMENT_VALUE = 1L;

    @Override
    public ExternalUserDetails getUserDetails(String login) {
        try {
            log.debug("Incrementing get github account details invocation count for user: {}", login);
            Optional<GithubGetUserDetailsRequestCount> entity = repository.findByLogin(login);
            if (entity.isPresent()) {
                repository.save(new GithubGetUserDetailsRequestCount(login, entity.get().getRequestCount() + INCREMENT_VALUE));
            } else {
                log.debug("No user found in db - creating new entry");
                repository.save(new GithubGetUserDetailsRequestCount(login, INCREMENT_VALUE));
            }
            log.debug("Calling github api to get user details for user: {}", login);
            Response<GithubUserDetailsDto> response = githubApiService.getGithubUserDetails(login).execute();
            if (response.isSuccessful()) {
                return mapper.toGitHubuserDetails(response.body());
            }
            log.error("Error occurred during calling github api: {}", response.errorBody().string());
            throw new ExternalApiException("Error occurred during calling external github api");
        } catch (IOException exception) {
            log.error("Error occurred during calling github api: ", exception);
            throw new ExternalApiException("Error occurred during calling external github api");
        }
    }
}
