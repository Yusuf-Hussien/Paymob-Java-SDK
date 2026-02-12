package com.paymob.sdk.services;

import com.paymob.sdk.Paymob;
import com.paymob.sdk.client.PaymobClient;
import com.paymob.sdk.models.auth.AuthRequest;
import com.paymob.sdk.models.auth.AuthResponse;

public class AuthService {
    private final PaymobClient client;

    public AuthService() {
        this(new PaymobClient());
    }

    public AuthService(PaymobClient client) {
        this.client = client;
    }

    /**
     * Authenticate with Paymob to get an Auth Token.
     * Uses the API Key set in Paymob.init().
     *
     * @return The response containing the Auth Token.
     */
    public AuthResponse authenticate() {
        AuthRequest request = new AuthRequest(Paymob.getApiKey());
        return client.post("/auth/tokens", request, AuthResponse.class);
    }
}
