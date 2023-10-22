package com.weljak.request_counter.service.github;

import com.weljak.request_counter.service.ExternalUserDetails;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
public class GithubUserDetails implements ExternalUserDetails {
    String id;
    String login;
    String name;
    String type;
    String avatarUrl;
    ZonedDateTime createdAt;
    String calculations;
}
