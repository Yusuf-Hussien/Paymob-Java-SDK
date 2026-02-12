package com.paymob.sdk.services;

import com.paymob.sdk.client.PaymobClient;
import com.paymob.sdk.models.payments.PaymentKeyRequest;
import com.paymob.sdk.models.payments.PaymentKeyResponse;

public class PaymentService {
    private final PaymobClient client;

    public PaymentService() {
        this(new PaymobClient());
    }

    public PaymentService(PaymobClient client) {
        this.client = client;
    }

    public PaymentKeyResponse requestPaymentKey(PaymentKeyRequest request) {
        return client.post("/acceptance/payment_keys", request, PaymentKeyResponse.class);
    }
}
