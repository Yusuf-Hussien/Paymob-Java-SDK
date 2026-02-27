package com.paymob.sdk.core.auth;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.paymob.sdk.http.HttpClient;

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

    private TokenResponse login(TokenRequest request) {
        httpClient.setBaseUrl(baseUrl);
        return httpClient.post("/api/auth/tokens", request, TokenResponse.class, null);
    }

    private String fetchTokenSync() {
        TokenResponse tokenResponse = login(new TokenRequest(apiKey));
        if (tokenResponse == null || tokenResponse.token == null || tokenResponse.token.isBlank()) {
            throw new RuntimeException("Token endpoint returned empty token");
        }

        String token = tokenResponse.token;
        Instant expiry = Instant.now().plusSeconds(3600);

        synchronized (lock) {
            this.bearerToken = token;
            this.tokenExpiry = expiry;
            tokenCache.put(apiKey, token);
        }

        return token;
    }

    /**
     * Fetches a new bearer token from Paymob API.
     * 
     * @return CompletableFuture that completes with the token
     */
    public CompletableFuture<String> fetchToken() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return fetchTokenSync();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch bearer token", e);
            }
        });
    }

    /**
     * Gets the cached token or fetches a new one if needed.
     * 
     * @return The bearer token
     */
    public String getOrFetchToken() {
        synchronized (lock) {
            // 1. Check current token
            if (bearerToken != null && !isTokenExpired()) {
                return bearerToken;
            }

            // 2. Check cache (in case another thread just updated it)
            String cachedToken = tokenCache.getIfPresent(apiKey);
            if (cachedToken != null) {
                bearerToken = cachedToken;
                tokenExpiry = Instant.now().plusSeconds(3600);
                return cachedToken;
            }

            // 3. Fetch new token while holding the lock to prevent concurrent logins
            return fetchTokenSyncUnderLock();
        }
    }

    private String fetchTokenSyncUnderLock() {
        TokenResponse tokenResponse = login(new TokenRequest(apiKey));
        if (tokenResponse == null || tokenResponse.token == null || tokenResponse.token.isBlank()) {
            throw new RuntimeException("Token endpoint returned empty token");
        }

        String token = tokenResponse.token;
        this.bearerToken = token;
        this.tokenExpiry = Instant.now().plusSeconds(3600);
        tokenCache.put(apiKey, token);

        return token;
    }

    @Override
    public String getAuthorizationHeader() {
        String token = getOrFetchToken();
        if (token == null || token.isBlank()) {
            return null;
        }
        return "Bearer " + token;
    }

    /**
     * Request DTO for token endpoint.
     */
    static class TokenRequest {
        public String api_key;

        public TokenRequest(String apiKey) {
            this.api_key = apiKey;
        }
    }

    /**
     * Response DTO for token endpoint.
     */
    static class TokenResponse {
        public String token;
    }
}
