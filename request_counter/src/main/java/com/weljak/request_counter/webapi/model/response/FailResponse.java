package com.weljak.request_counter.webapi.model.response;

import lombok.Value;

@Value
public class FailResponse {
    Integer statusCode;
    String message;
}
