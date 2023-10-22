package com.weljak.request_counter.service;

import java.io.IOException;

public interface ExternalUserDetailsService {
    ExternalUserDetails getUserDetails(String login);
}
