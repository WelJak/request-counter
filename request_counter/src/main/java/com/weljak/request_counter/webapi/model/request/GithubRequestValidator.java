package com.weljak.request_counter.webapi.model.request;

public class GithubRequestValidator {
    public static boolean validate(String login) {
        if (login.isEmpty()) return false;
        if (login.isBlank()) return false;
        return !login.startsWith(" ");
    }
}
