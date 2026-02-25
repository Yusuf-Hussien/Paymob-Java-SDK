package com.paymob.sdk.models.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 * Represents a Paymob Transaction with full details.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
    @JsonProperty("id")
    private long id;

    @JsonProperty("pending")
    private boolean pending;

    @JsonProperty("amount_cents")
    private int amountCents;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("is_auth")
    private boolean isAuth;

    @JsonProperty("is_capture")
    private boolean isCapture;

    @JsonProperty("is_standalone_payment")
    private boolean isStandalonePayment;

    @JsonProperty("is_voided")
    private boolean isVoided;

    @JsonProperty("is_refunded")
    private boolean isRefunded;

    @JsonProperty("is_3d_secure")
    private boolean is3dSecure;

    @JsonProperty("integration_id")
    private int integrationId;

    @JsonProperty("profile_id")
    private int profileId;

    @JsonProperty("has_parent_transaction")
    private boolean hasParentTransaction;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("error_occured")
    private boolean errorOccured;

    @JsonProperty("is_live")
    private boolean isLive;

    @JsonProperty("is_captured")
    private boolean isCaptured;

    @JsonProperty("captured_amount")
    private Integer capturedAmount;

    @JsonProperty("merchant_order_id")
    private String merchantOrderId;

    @JsonProperty("source_data")
    private Map<String, Object> sourceData;

    @JsonProperty("data")
    private Map<String, Object> data;

    @JsonProperty("order")
    private OrderInfo order;

    /**
     * Nested order information within a transaction.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderInfo {
        @JsonProperty("id")
        private long id;

        @JsonProperty("created_at")
        private String createdAt;

        @JsonProperty("amount_cents")
        private int amountCents;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("merchant_order_id")
        private String merchantOrderId;

        @JsonProperty("items")
        private List<Item> items;

        @JsonProperty("shipping_data")
        private BillingData shippingData;

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

        public int getAmountCents() {
            return amountCents;
        }

        public void setAmountCents(int amountCents) {
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

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public BillingData getShippingData() {
            return shippingData;
        }

        public void setShippingData(BillingData shippingData) {
            this.shippingData = shippingData;
        }
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(int amountCents) {
        this.amountCents = amountCents;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public boolean isCapture() {
        return isCapture;
    }

    public void setCapture(boolean capture) {
        isCapture = capture;
    }

    public boolean isStandalonePayment() {
        return isStandalonePayment;
    }

    public void setStandalonePayment(boolean standalonePayment) {
        isStandalonePayment = standalonePayment;
    }

    public boolean isVoided() {
        return isVoided;
    }

    public void setVoided(boolean voided) {
        isVoided = voided;
    }

    public boolean isRefunded() {
        return isRefunded;
    }

    public void setRefunded(boolean refunded) {
        isRefunded = refunded;
    }

    public boolean isIs3dSecure() {
        return is3dSecure;
    }

    public void setIs3dSecure(boolean is3dSecure) {
        this.is3dSecure = is3dSecure;
    }

    public int getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(int integrationId) {
        this.integrationId = integrationId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public boolean isHasParentTransaction() {
        return hasParentTransaction;
    }

    public void setHasParentTransaction(boolean hasParentTransaction) {
        this.hasParentTransaction = hasParentTransaction;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isErrorOccured() {
        return errorOccured;
    }

    public void setErrorOccured(boolean errorOccured) {
        this.errorOccured = errorOccured;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public void setCaptured(boolean captured) {
        isCaptured = captured;
    }

    public Integer getCapturedAmount() {
        return capturedAmount;
    }

    public void setCapturedAmount(Integer capturedAmount) {
        this.capturedAmount = capturedAmount;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public Map<String, Object> getSourceData() {
        return sourceData;
    }

    public void setSourceData(Map<String, Object> sourceData) {
        this.sourceData = sourceData;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", success=" + success +
                ", amountCents=" + amountCents +
                ", currency='" + currency + '\'' +
                ", status=" + (pending ? "PENDING" : (success ? "SUCCESS" : "FAILED")) +
                ", orderId=" + (order != null ? order.getId() : "null") +
                '}';
    }
}
