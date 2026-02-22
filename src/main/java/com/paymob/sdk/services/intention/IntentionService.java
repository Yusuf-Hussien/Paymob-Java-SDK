package com.paymob.sdk.services.intention;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;

/**
 * Service for handling Payment Intention APIs.
 * The modern entry point for all payments with unified checkout.
 */
public class IntentionService {
    private final HttpClient httpClient;
    private final AuthStrategy authStrategy;
    private final PaymobConfig config;

    public IntentionService(HttpClient httpClient, AuthStrategy authStrategy, PaymobConfig config) {
        this.httpClient = httpClient;
        this.authStrategy = authStrategy;
        this.config = config;
    }

    /**
     * Creates a new payment intention.
     * @param request The intention creation request
     * @return The intention response with payment keys and URLs
     */
    public IntentionResponse createIntention(IntentionRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/v1/intention/", request, IntentionResponse.class, authStrategy);
    }

    /**
     * Updates an existing payment intention.
     * @param clientSecret The client secret from the original intention
     * @param request The update request
     * @return The updated intention response
     */
    public IntentionResponse updateIntention(String clientSecret, IntentionRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.put("/v1/intention/" + clientSecret, request, IntentionResponse.class, authStrategy);
    }

    /**
     * Retrieves an existing payment intention.
     * @param clientSecret The client secret from the intention
     * @return The intention details
     */
    public IntentionResponse retrieveIntention(String clientSecret) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        String endpoint = "/v1/intention/element/" + config.getPublicKey() + "/" + clientSecret + "/";
        return httpClient.get(endpoint, IntentionResponse.class, authStrategy);
    }

    /**
     * Generates the Unified Checkout URL for the client.
     * @param clientSecret The client secret obtained from createIntention
     * @return The URL to redirect the user to
     */
    public String getUnifiedCheckoutUrl(String clientSecret) {
        return config.getRegion().getBaseUrl() + "/unifiedcheckout/?publicKey=" + config.getPublicKey() + "&clientSecret=" + clientSecret;
    }
}
