package com.paymob.sdk.exceptions;

/**
 * Exception thrown when a resource is not found (HTTP 404).
 * Occurs when transaction/order ID doesn't exist in the queried region.
 */
public class ResourceNotFoundException extends PaymobException {
    public ResourceNotFoundException(String message) {
        super(message, 404, null);
    }

    public ResourceNotFoundException(String message, String errorBody) {
        super(message, 404, errorBody);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause, 404, null);
    }
}
