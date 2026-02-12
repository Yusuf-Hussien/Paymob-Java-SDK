package com.paymob.sdk.services;

import com.paymob.sdk.client.PaymobClient;
import com.paymob.sdk.models.orders.OrderRequest;
import com.paymob.sdk.models.orders.OrderResponse;

public class OrderService {
    private final PaymobClient client;

    public OrderService() {
        this.client = new PaymobClient();
    }

    public OrderResponse createOrder(String authToken, String amountCents, String currency, String merchantOrderId) {
        OrderRequest request = new OrderRequest(authToken, amountCents, currency, merchantOrderId);
        return client.post("/ecommerce/orders", request, OrderResponse.class);
    }
}
