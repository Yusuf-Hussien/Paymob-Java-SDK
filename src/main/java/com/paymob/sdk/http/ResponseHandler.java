package com.paymob.sdk.http;

import com.paymob.sdk.exceptions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import okhttp3.Response;
import java.io.IOException;

/**
 * Handler for processing HTTP responses and mapping to appropriate objects or exceptions.
 * Centralizes response parsing and error handling logic.
 */
public class ResponseHandler {
    private final ObjectMapper objectMapper;

    public ResponseHandler() {
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Processes successful response and maps to response class.
     */
    public <T> T handleSuccess(Response response, Class<T> responseClass) throws IOException {
        String responseBody = response.body() != null ? response.body().string() : "";
        
        if (responseClass == String.class) {
            return (T) responseBody;
        }
        
        return objectMapper.readValue(responseBody, responseClass);
    }

    /**
     * Maps HTTP error responses to appropriate exceptions.
     */
    public PaymobException handleError(Response response) throws IOException {
        int statusCode = response.code();
        String responseBody = response.body() != null ? response.body().string() : "";

        switch (statusCode) {
            case 401:
                return new AuthenticationException("Authentication failed", statusCode, responseBody);
            case 404:
                return new ResourceNotFoundException("Resource not found", statusCode, responseBody);
            case 406:
                return new ValidationException("Validation failed", statusCode, responseBody);
            case 500:
            case 502:
            case 503:
            case 504:
                return new PaymobServerException("Server error", statusCode, responseBody);
            default:
                return new PaymobException("HTTP error: " + statusCode, statusCode, responseBody);
        }
    }

    /**
     * Checks if response indicates a timeout.
     */
    public boolean isTimeout(Response response) {
        return response.code() == 408 || 
               response.code() == 429 || 
               response.message().toLowerCase().contains("timeout");
    }

    /**
     * Creates timeout exception.
     */
    public PaymobTimeoutException createTimeoutException(String message, Throwable cause) {
        return new PaymobTimeoutException(message, cause);
    }
}
