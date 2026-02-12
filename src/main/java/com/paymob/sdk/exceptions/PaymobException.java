package com.paymob.sdk.exceptions;

public class PaymobException extends RuntimeException {
    public PaymobException(String message) {
        super(message);
    }

    public PaymobException(String message, Throwable cause) {
        super(message, cause);
    }
}
