package com.paymob.sdk.models.intention;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class IntentionResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("amount")
    private int amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("payment_keys")
    private List<PaymentKey> paymentKeys;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<PaymentKey> getPaymentKeys() {
        return paymentKeys;
    }

    public void setPaymentKeys(List<PaymentKey> paymentKeys) {
        this.paymentKeys = paymentKeys;
    }

    public static class PaymentKey {
        @JsonProperty("key")
        private String key;

        @JsonProperty("integration_id")
        private int integrationId;

        @JsonProperty("iframe_id")
        private String iframeId;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getIntegrationId() {
            return integrationId;
        }

        public void setIntegrationId(int integrationId) {
            this.integrationId = integrationId;
        }

        public String getIframeId() {
            return iframeId;
        }

        public void setIframeId(String iframeId) {
            this.iframeId = iframeId;
        }
    }
}
