package com.paymob.sdk.core.auth;

/**
 * Authentication strategy using Secret Key.
 * Header format: Authorization: Token <SECRET_KEY>
 * Used by: Intention, Transaction, Inquiry, QuickLink, SavedCard APIs
 */
public class SecretKeyAuthStrategy implements AuthStrategy {
    private final String secretKey;

    public SecretKeyAuthStrategy(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void apply(Object requestBuilder) {
        // This will be implemented with OkHttp Request.Builder
        // For now, we'll store the secret key for later use
    }

    @Override
    public String getType() {
        return "SECRET_KEY";
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getAuthorizationHeader() {
        return "Token " + secretKey;
    }
}
