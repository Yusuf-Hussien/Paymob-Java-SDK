package com.paymob.sdk.exceptions;

/**
 * Exception thrown for Paymob server errors (HTTP 5xx).
 * Occurs when Paymob servers experience issues.
 */
public class PaymobServerException extends PaymobException {
    public PaymobServerException(String message) {
        super(message);
    }

    public PaymobServerException(String message, Integer httpStatus, String errorBody) {
        super(message, httpStatus, errorBody);
    }

    public PaymobServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaymobServerException(String message, Throwable cause, Integer httpStatus, String errorBody) {
        super(message, cause, httpStatus, errorBody);
    }
}
