package com.paymob.sdk.services.savedcard;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;

/**
 * Service for saved card tokenization payments.
 * Supports both CIT (Customer Initiated) and MIT (Merchant Initiated) transactions.
 */
public class SavedCardService {
    private final HttpClient httpClient;
    private final AuthStrategy authStrategy;
    private final PaymobConfig config;

    public SavedCardService(HttpClient httpClient, AuthStrategy authStrategy, PaymobConfig config) {
        this.httpClient = httpClient;
        this.authStrategy = authStrategy;
        this.config = config;
    }

    /**
     * Processes a Customer Initiated Transaction (CIT).
     * Customer is present at checkout, requires CVV re-entry.
     * @param request The CIT payment request
     * @return Payment response
     */
    public TokenizedPaymentResponse processCitPayment(CitPaymentRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/v1/intention/", request, TokenizedPaymentResponse.class, authStrategy);
    }

    /**
     * Processes a Merchant Initiated Transaction (MIT).
     * Automated charges for subscriptions/top-ups, no customer present.
     * @param request The MIT payment request
     * @return Payment response
     */
    public TokenizedPaymentResponse processMitPayment(MitPaymentRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/v1/intention/", request, TokenizedPaymentResponse.class, authStrategy);
    }
}
