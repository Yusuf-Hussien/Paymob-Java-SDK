package com.paymob.sdk.http;

import com.paymob.sdk.core.auth.AuthStrategy;

/**
 * Interface for HTTP client implementations.
 * Allows swapping between different HTTP client libraries.
 */
public interface HttpClient {
    /**
     * Performs a GET request with authentication.
     */
    <T> T get(String endpoint, Class<T> responseClass, AuthStrategy authStrategy);

    /**
     * Performs a POST request with authentication.
     */
    <T> T post(String endpoint, Object requestBody, Class<T> responseClass, AuthStrategy authStrategy);

    /**
     * Performs a PUT request with authentication.
     */
    <T> T put(String endpoint, Object requestBody, Class<T> responseClass, AuthStrategy authStrategy);

    /**
     * Performs a DELETE request with authentication.
     */
    <T> T delete(String endpoint, Class<T> responseClass, AuthStrategy authStrategy);

    /**
     * Sets the base URL for all requests.
     */
    void setBaseUrl(String baseUrl);

    /**
     * Sets the timeout for requests.
     */
    void setTimeout(int timeoutSeconds);
}
