package com.paymob.sdk.services;

import com.paymob.sdk.Paymob;
import com.paymob.sdk.client.PaymobClient;
import com.paymob.sdk.models.auth.AuthRequest;
import com.paymob.sdk.models.auth.AuthResponse;

public class AuthService {
    private final PaymobClient client;

    public AuthService() {
        this.client = new PaymobClient();
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
