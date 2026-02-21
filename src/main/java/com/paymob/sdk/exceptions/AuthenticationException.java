package com.paymob.sdk.exceptions;

/**
 * Exception thrown for authentication failures (HTTP 401).
 * Occurs when credentials are invalid or wrong environment key is used.
 */
public class AuthenticationException extends PaymobException {
    public AuthenticationException(String message) {
        super(message, 401, null);
    }

    public AuthenticationException(String message, String errorBody) {
        super(message, 401, errorBody);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause, 401, null);
    }

    public AuthenticationException(String message, int httpStatus, String errorBody) {
        super(message, httpStatus, errorBody);
    }
}
