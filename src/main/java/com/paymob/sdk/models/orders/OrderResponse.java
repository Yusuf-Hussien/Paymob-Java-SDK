package com.paymob.sdk.models.orders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponse {
    @JsonProperty("id")
    private long id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("delivery_needed")
    private boolean deliveryNeeded;
    @JsonProperty("merchant_order_id")
    private String merchantOrderId;

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isDeliveryNeeded() {
        return deliveryNeeded;
    }

    public void setDeliveryNeeded(boolean deliveryNeeded) {
        this.deliveryNeeded = deliveryNeeded;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }
}
