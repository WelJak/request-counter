package com.weljak.request_counter.webapi.exception;

import com.weljak.request_counter.service.external.ExternalApiException;
import com.weljak.request_counter.webapi.model.response.GetExternalUserDetailsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    ResponseEntity<GetExternalUserDetailsResponse> handleInvalidRequestParameters(Exception exception, ServletWebRequest webRequest) {
        log.error("Invalid argument for request: {}", webRequest.getRequest().getRequestURI());
        return new ResponseEntity<>(
                new GetExternalUserDetailsResponse(
                        false,
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage(),
                        null
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalApiException.class)
    ResponseEntity<GetExternalUserDetailsResponse> handleExternalApiException(Exception exception, ServletWebRequest webRequest) {
        log.error("Error occurred for request: {}", webRequest.getRequest().getRequestURI());
        return new ResponseEntity<>(
                new GetExternalUserDetailsResponse(
                        false,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        exception.getMessage(),
                        null
                ),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
