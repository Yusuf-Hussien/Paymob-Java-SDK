package com.paymob.sdk.exceptions;

/**
 * Exception thrown for network/connection timeouts.
 * Occurs when requests exceed the configured timeout period.
 */
public class PaymobTimeoutException extends PaymobException {
    public PaymobTimeoutException(String message) {
        super(message);
    }

    public PaymobTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
