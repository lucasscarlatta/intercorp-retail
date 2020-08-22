package com.example.demo.exception.error;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ApiErrorBuilder {

    private HttpStatus status;
    private int code;
    private String message;
    private String debugMessage;
    private String path;

    private List<String> errors;

    public ApiErrorBuilder setStatus(HttpStatus status) {
        this.status = status;
        return this;
    }

    public ApiErrorBuilder setCode(int code) {
        this.code = code;
        return this;
    }

    public ApiErrorBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public ApiErrorBuilder setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
        return this;
    }

    public ApiErrorBuilder setPath(String path) {
        this.path = path;
        return this;
    }

    public ApiErrorBuilder setErrors(List<String> errors) {
        this.errors = errors;
        return this;
    }

    public ApiError build() {
        return new ApiError(this.status, this.code, LocalDateTime.now(), this.message, this.debugMessage, this.path,
                this.errors);
    }
}
