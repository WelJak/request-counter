package com.weljak.request_counter.webapi.github;

import com.weljak.request_counter.service.ExternalUserDetailsService;
import com.weljak.request_counter.utils.Endpoints;
import com.weljak.request_counter.webapi.model.request.GithubRequestValidator;
import com.weljak.request_counter.webapi.model.response.GetExternalUserDetailsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    ResponseEntity<GetExternalUserDetailsResponse> getGithubUserDetails(@PathVariable String login) {
        if (!GithubRequestValidator.validate(login)) {
            return new ResponseEntity<>(
                    new GetExternalUserDetailsResponse(
                            false,
                            HttpStatus.BAD_REQUEST.value(),
                            "Invalid input",
                            null
                    ),
                    HttpStatus.BAD_REQUEST
            );
        }
        return new ResponseEntity<>(
                new GetExternalUserDetailsResponse(
                        true,
                        HttpStatus.OK.value(),
                        "Fetched data",
                        githubUserDetailsService.getUserDetails(login)
                ),
                HttpStatus.OK
        );
    }

}
