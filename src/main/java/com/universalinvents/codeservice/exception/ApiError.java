package com.universalinvents.codeservice.exception;

/**
 *
 */
public class ApiError {
    private String message;

    public ApiError () {}
    public ApiError(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}