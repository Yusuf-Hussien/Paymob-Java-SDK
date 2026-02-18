package com.paymob.sdk.core.auth;

import java.time.Instant;

/**
 * Authentication strategy using Bearer Token.
 * Step 1: POST /api/auth/tokens with api_key → Bearer token
 * Step 2: Cache token with 55-min TTL (Caffeine)
 * Step 3: Auto-refresh transparently before expiry
 * Used by: Subscription module
 */
public class BearerTokenAuthStrategy implements AuthStrategy {
    private final String apiKey;
    private volatile String bearerToken;
    private volatile Instant tokenExpiry;
    private final Object lock = new Object();

    public BearerTokenAuthStrategy(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void apply(Object requestBuilder) {
        // This will be implemented with OkHttp Request.Builder
        // For now, we'll store the API key for later use
    }

    @Override
    public String getType() {
        return "BEARER_TOKEN";
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getBearerToken() {
        synchronized (lock) {
            return bearerToken;
        }
    }

    public Instant getTokenExpiry() {
        synchronized (lock) {
            return tokenExpiry;
        }
    }

    public void setBearerToken(String bearerToken, Instant expiry) {
        synchronized (lock) {
            this.bearerToken = bearerToken;
            this.tokenExpiry = expiry;
        }
    }

    public boolean isTokenExpired() {
        synchronized (lock) {
            return tokenExpiry == null || Instant.now().isAfter(tokenExpiry.minusSeconds(300)); // 5 min buffer
        }
    }

    public String getAuthorizationHeader() {
        synchronized (lock) {
            return "Bearer " + bearerToken;
        }
    }
}
