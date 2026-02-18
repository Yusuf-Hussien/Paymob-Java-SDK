package com.paymob.sdk.services.quicklink;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;

/**
 * Service for creating shareable payment links.
 * Pay-by-Link functionality for invoicing and quick payments.
 */
public class QuickLinkService {
    private final HttpClient httpClient;
    private final AuthStrategy authStrategy;
    private final PaymobConfig config;

    public QuickLinkService(HttpClient httpClient, AuthStrategy authStrategy, PaymobConfig config) {
        this.httpClient = httpClient;
        this.authStrategy = authStrategy;
        this.config = config;
    }

    /**
     * Creates a shareable payment link.
     * @param request The quick link creation request
     * @return Payment link details
     */
    public QuickLinkResponse createPaymentLink(QuickLinkRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/quicklinks", request, QuickLinkResponse.class, authStrategy);
    }
}
