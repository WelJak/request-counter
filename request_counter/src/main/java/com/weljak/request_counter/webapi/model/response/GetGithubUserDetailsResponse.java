package com.weljak.request_counter.webapi.model.response;

import lombok.Value;

import java.time.ZonedDateTime;

@Value
public class GetGithubUserDetailsResponse {
    String id;
    String login;
    String name;
    String type;
    String avatarUrl;
    ZonedDateTime createdAt;
    String calculations;
}
