package com.paymob.sdk.services;

import com.paymob.sdk.client.PaymobClient;

public class TransactionService {
    private final PaymobClient client;

    public TransactionService() {
        this.client = new PaymobClient();
    }

    public Object getTransaction(long transactionId, String authToken) {
        return client.get("/acceptance/transactions/" + transactionId, Object.class);
    }

    // Void and Refund usually require POST requests to specific endpoints
    // void: /acceptance/void_refund/void?token=...
    // refund: /acceptance/void_refund/refund?token=...
}
