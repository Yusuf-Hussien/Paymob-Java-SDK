package com.paymob.sdk.models.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OrderRequest {
    @JsonProperty("auth_token")
    private String authToken;
    @JsonProperty("delivery_needed")
    private boolean deliveryNeeded;
    @JsonProperty("amount_cents")
    private String amountCents;
    @JsonProperty("currency")
    private String currency; // "EGP"
    @JsonProperty("merchant_order_id")
    private String merchantOrderId;
    @JsonProperty("items")
    private List<Object> items; // Can be detailed Item object

    // Getters and Setters and Constructors
    public OrderRequest() {
    }

    public OrderRequest(String authToken, String amountCents, String currency, String merchantOrderId) {
        this.authToken = authToken;
        this.amountCents = amountCents;
        this.currency = currency;
        this.merchantOrderId = merchantOrderId;
        this.deliveryNeeded = false;
        this.items = List.of();
    }

    // Standard Getters/Setters ommitted for brevity in this tool call but included
    // in file
    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean isDeliveryNeeded() {
        return deliveryNeeded;
    }

    public void setDeliveryNeeded(boolean deliveryNeeded) {
        this.deliveryNeeded = deliveryNeeded;
    }

    public String getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(String amountCents) {
        this.amountCents = amountCents;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public List<Object> getItems() {
        return items;
    }

    public void setItems(List<Object> items) {
        this.items = items;
    }
}
