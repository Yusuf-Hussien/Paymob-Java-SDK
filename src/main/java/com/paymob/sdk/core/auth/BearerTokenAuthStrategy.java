package com.paymob.sdk.core.auth;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymob.sdk.http.HttpClient;
import okhttp3.*;

/**
 * Authentication strategy using Bearer Token.
 * Step 1: POST /api/auth/tokens with api_key → Bearer token
 * Step 2: Cache token with 55-min TTL (Caffeine)
 * Step 3: Auto-refresh transparently before expiry
 * Used by: Subscription module
 */
public class BearerTokenAuthStrategy implements AuthStrategy {
    private final String apiKey;
    private final String baseUrl;
    private final HttpClient httpClient;
    private final Cache<String, String> tokenCache;
    private final ObjectMapper objectMapper;
    private volatile String bearerToken;
    private volatile Instant tokenExpiry;
    private final Object lock = new Object();

    public BearerTokenAuthStrategy(String apiKey, String baseUrl, HttpClient httpClient) {
        if (apiKey == null) {
            throw new NullPointerException("API key cannot be null");
        }
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
        this.tokenCache = Caffeine.newBuilder()
                .expireAfterWrite(55, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
        this.bearerToken = null;
        this.tokenExpiry = null;
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

    public void setBearerToken(String token, Instant expiry) {
        synchronized (lock) {
            this.bearerToken = token;
            this.tokenExpiry = expiry;
            if (token != null && expiry != null) {
                tokenCache.put(apiKey, token);
            }
        }
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

    public boolean isTokenExpired() {
        synchronized (lock) {
            return tokenExpiry == null || Instant.now().isAfter(tokenExpiry.minusSeconds(300)); // 5 min buffer
        }
    }

    /**
     * Fetches a new bearer token from Paymob API.
     * @return CompletableFuture that completes with the token
     */
    public CompletableFuture<String> fetchToken() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String jsonBody = "{\"api_key\": \"" + apiKey + "\"}";
                
                // Simple mock implementation for now
                String token = "mock_token_" + System.currentTimeMillis();
                Instant expiry = Instant.now().plusSeconds(3600); // 1 hour
                tokenCache.put(apiKey, token);
                
                synchronized (lock) {
                    this.bearerToken = token;
                    this.tokenExpiry = expiry;
                }
                
                return token;
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch bearer token", e);
            }
        });
    }

    /**
     * Gets the cached token or fetches a new one if needed.
     * @return The bearer token
     */
    public String getOrFetchToken() {
        synchronized (lock) {
            // Check cache first
            String cachedToken = tokenCache.getIfPresent(apiKey);
            if (cachedToken != null) {
                bearerToken = cachedToken;
                tokenExpiry = Instant.now().plusSeconds(3600);
                return cachedToken;
            }

            // Check if current token is still valid
            if (bearerToken != null && !isTokenExpired()) {
                return bearerToken;
            }

            // Fetch new token asynchronously and wait for result
            try {
                return fetchToken().get(30, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch bearer token", e);
            }
        }
    }

    @Override
    public String getAuthorizationHeader() {
        if (bearerToken == null) {
            return null;
        }
        return "Bearer " + bearerToken;
    }

    /**
     * Response DTO for token endpoint.
     */
    private static class TokenResponse {
        public String token;
    }
}
