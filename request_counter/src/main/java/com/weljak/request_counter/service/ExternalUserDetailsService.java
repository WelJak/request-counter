package com.weljak.request_counter.service;

public interface ExternalUserDetailsService {
    ExternalUserDetails getUserDetails(String login);
}
