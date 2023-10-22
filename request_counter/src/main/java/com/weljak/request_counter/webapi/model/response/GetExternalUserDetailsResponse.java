package com.weljak.request_counter.webapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetExternalUserDetailsResponse {
    private Boolean success;
    private Integer statusCode;
    private String message;
    private Object payload;
}
