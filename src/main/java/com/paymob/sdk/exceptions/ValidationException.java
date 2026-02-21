package com.paymob.sdk.exceptions;

/**
 * Exception thrown for validation failures (HTTP 406).
 * Occurs when amount doesn't equal sum of items or other validation errors.
 */
public class ValidationException extends PaymobException {
    public ValidationException(String message) {
        super(message, 406, null);
    }

    public ValidationException(String message, String errorBody) {
        super(message, 406, errorBody);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause, 406, null);
    }

    public ValidationException(String message, int httpStatus, String errorBody) {
        super(message, httpStatus, errorBody);
    }
}
