package com.weljak.request_counter.webapi.github;

import com.weljak.request_counter.service.ExternalUserDetailsService;
import com.weljak.request_counter.service.github.GithubUserDetails;
import com.weljak.request_counter.utils.Endpoints;
import com.weljak.request_counter.utils.mapper.GithubApiResponseMapper;
import com.weljak.request_counter.webapi.exception.RequestValidationException;
import com.weljak.request_counter.webapi.model.request.GithubRequestValidator;
import com.weljak.request_counter.webapi.model.response.GetGithubUserDetailsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GithubUserDetailsController {
    private final ExternalUserDetailsService githubUserDetailsService;

    @GetMapping(Endpoints.GET_GITHUB_USER_DETAILS)
    ResponseEntity<GetGithubUserDetailsResponse> getGithubUserDetails(@PathVariable String login) {
        if (!GithubRequestValidator.validate(login)) {
            log.error("Validation failed for login: {}", login);
            throw new RequestValidationException("Invalid request parameter.");
        }
        return ResponseEntity.ok(GithubApiResponseMapper.INSTANCE.toGithubUserDetailsResponse((GithubUserDetails) githubUserDetailsService.getUserDetails(login)));
    }

}
