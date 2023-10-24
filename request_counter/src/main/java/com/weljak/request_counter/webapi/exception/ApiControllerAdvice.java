package com.weljak.request_counter.webapi.exception;

import com.weljak.request_counter.service.external.github.exception.ExternalApiException;
import com.weljak.request_counter.webapi.model.response.FailResponse;
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
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class, RequestValidationException.class})
    ResponseEntity<FailResponse> handleInvalidRequestParameters(Exception exception, ServletWebRequest webRequest) {
        log.error("Invalid argument for request: {}", webRequest.getRequest().getRequestURI());
        return new ResponseEntity<>(
                new FailResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid request parameter"
                ),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalApiException.class)
    ResponseEntity<FailResponse> handleExternalApiException(Exception exception, ServletWebRequest webRequest) {
        log.error("Error occurred for request: {}", webRequest.getRequest().getRequestURI());
        return new ResponseEntity<>(
                new FailResponse(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Error occurred while calling github api - try again later"
                ),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
