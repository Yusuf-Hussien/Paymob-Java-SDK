package com.paymob.sdk.services;

import com.paymob.sdk.client.PaymobClient;

import com.paymob.sdk.models.transaction.TransactionControlRequest;

public class TransactionService {
    private final PaymobClient client;

    public TransactionService() {
        this(new PaymobClient());
    }

    public TransactionService(PaymobClient client) {
        this.client = client;
    }

    public Object getTransaction(long transactionId, String authToken) {
        return client.getWithBearerToken("/acceptance/transactions/" + transactionId, authToken, Object.class);
    }

    public Object voidTransaction(long transactionId) {
        TransactionControlRequest request = new TransactionControlRequest(transactionId);
        return client.postWithSecretKey("/api/acceptance/void_refund/void", request, Object.class);
    }

    public Object refundTransaction(long transactionId, int amountCents) {
        TransactionControlRequest request = new TransactionControlRequest(transactionId, amountCents);
        return client.postWithSecretKey("/api/acceptance/void_refund/refund", request, Object.class);
    }

    public Object captureTransaction(long transactionId, int amountCents) {
        TransactionControlRequest request = new TransactionControlRequest(transactionId, amountCents);
        return client.postWithSecretKey("/api/acceptance/capture", request, Object.class);
    }

}
