package com.example.user_service.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final Object responseBody;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.responseBody = message;
    }

    public ApiException(HttpStatus status, Object responseBody, String message) {
        super(message);
        this.status = status;
        this.responseBody = responseBody;
    }

    public HttpStatus getStatus() { return status; }
    public Object getResponseBody() { return responseBody; }
}
