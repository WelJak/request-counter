package com.weljak.request_counter.service.external.github;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubApiService {
    @GET("/users/{login}")
    Call<GithubUserDetailsDto> getGithubUserDetails(@Path("login") String login);
}
