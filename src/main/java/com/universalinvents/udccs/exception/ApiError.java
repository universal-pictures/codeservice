package com.universalinvents.udccs.exception;

/**
 *
 */
public class ApiError extends Throwable {
    private String message;

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