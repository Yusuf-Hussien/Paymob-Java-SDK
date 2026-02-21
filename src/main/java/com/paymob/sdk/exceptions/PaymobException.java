package com.paymob.sdk.exceptions;

/**
 * Base exception for all Paymob SDK errors.
 * Carries HTTP status, error body, and developer-readable message.
 */
public class PaymobException extends RuntimeException {
    private final Integer httpStatus;
    private final String errorBody;

    public PaymobException(String message) {
        super(message);
        this.httpStatus = null;
        this.errorBody = null;
    }

    public PaymobException(String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = null;
        this.errorBody = null;
    }

    public PaymobException(String message, Integer httpStatus, String errorBody) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorBody = errorBody;
    }

    public PaymobException(String message, Throwable cause, Integer httpStatus, String errorBody) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorBody = errorBody;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public String getErrorBody() {
        return errorBody;
    }
}
