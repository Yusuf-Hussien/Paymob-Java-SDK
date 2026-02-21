package com.paymob.sdk.core.auth;

/**
 * Interface for different authentication strategies used by Paymob APIs.
 */
public interface AuthStrategy {
    /**
     * Applies authentication to HTTP request.
     * @param requestBuilder The request builder to apply auth to
     */
    void apply(Object requestBuilder);

    /**
     * Returns the type of authentication strategy.
     * @return The auth strategy type
     */
    String getType();

    /**
     * Returns the authorization header value.
     * @return The authorization header value
     */
    String getAuthorizationHeader();
}
