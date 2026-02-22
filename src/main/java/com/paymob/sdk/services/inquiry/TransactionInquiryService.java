package com.paymob.sdk.services.inquiry;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;

/**
 * Service for transaction inquiry and reconciliation.
 * Supports lookup by merchant_order_id, order_id, or transaction_id.
 */
public class TransactionInquiryService {
    private final HttpClient httpClient;
    private final AuthStrategy authStrategy;
    private final PaymobConfig config;

    public TransactionInquiryService(HttpClient httpClient, AuthStrategy authStrategy, PaymobConfig config) {
        this.httpClient = httpClient;
        this.authStrategy = authStrategy;
        this.config = config;
    }

    /**
     * Retrieves transaction by merchant order ID.
     * @param merchantOrderId Your custom order reference
     * @return Transaction details
     */
    public InquiryResponse byMerchantOrderId(String merchantOrderId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        InquiryRequest request = new InquiryRequest();
        request.setMerchantOrderId(merchantOrderId);
        return httpClient.post("/api/ecommerce/orders/transaction_inquiry", request, InquiryResponse.class, authStrategy);
    }

    /**
     * Retrieves transaction by Paymob order ID.
     * @param orderId Paymob's internal order ID
     * @return Transaction details
     */
    public InquiryResponse byOrderId(int orderId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        InquiryRequest request = new InquiryRequest();
        request.setOrderId(orderId);
        return httpClient.post("/api/ecommerce/orders/transaction_inquiry", request, InquiryResponse.class, authStrategy);
    }

    /**
     * Retrieves transaction by transaction ID.
     * @param transactionId The specific transaction ID
     * @return Transaction details
     */
    public InquiryResponse byTransactionId(long transactionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.get("/api/acceptance/transactions/" + transactionId, InquiryResponse.class, authStrategy);
    }
}
