package com.wanchcoach.global.api;

import org.springframework.http.HttpStatus;

public class ApiError {

    private final int code;
    private final String message;

    ApiError(HttpStatus code, Throwable throwable) {
        this(code, throwable.getMessage());
    }

    ApiError(HttpStatus code, String message) {
        this.code = code.value();
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
