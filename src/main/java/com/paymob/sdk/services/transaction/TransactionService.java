package com.paymob.sdk.services.transaction;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;

/**
 * Service for handling post-payment operations: Refund, Void & Capture.
 */
public class TransactionService {
    private final HttpClient httpClient;
    private final AuthStrategy authStrategy;
    private final PaymobConfig config;

    public TransactionService(HttpClient httpClient, AuthStrategy authStrategy, PaymobConfig config) {
        this.httpClient = httpClient;
        this.authStrategy = authStrategy;
        this.config = config;
    }

    /**
     * Refunds a transaction (full or partial).
     *
     * @param request The refund request
     * @return Refund response containing transaction details
     */
    public TransactionResponse refundTransaction(RefundRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/void_refund/refund", request, TransactionResponse.class, authStrategy);
    }

    /**
     * Voids a same-day transaction to avoid processing fees.
     *
     * @param request The void request
     * @return Void response containing transaction details
     */
    public TransactionResponse voidTransaction(VoidRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/void_refund/void", request, TransactionResponse.class, authStrategy);
    }

    /**
     * Captures an authorize-only transaction.
     * Must be done within 14 days of authorization.
     *
     * @param request The capture request
     * @return Capture response containing transaction details
     */
    public TransactionResponse captureTransaction(CaptureRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/capture", request, TransactionResponse.class, authStrategy);
    }
}
