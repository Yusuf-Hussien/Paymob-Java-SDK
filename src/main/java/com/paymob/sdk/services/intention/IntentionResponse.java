package com.paymob.sdk.services.intention;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response object for payment intention operations.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntentionResponse {
    private String clientSecret;
    private String redirectionUrl;
    private String specialReference;
    private int amount;
    private String currency;
    private List<PaymentKey> paymentKeys;

    // Getters and setters
    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectionUrl() {
        return redirectionUrl;
    }

    public void setRedirectionUrl(String redirectionUrl) {
        this.redirectionUrl = redirectionUrl;
    }

    public String getSpecialReference() {
        return specialReference;
    }

    public void setSpecialReference(String specialReference) {
        this.specialReference = specialReference;
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

    /**
     * Payment key information for different payment methods.
     */
    public static class PaymentKey {
        private int integration;
        private String key;
        private String gatewayType;
        private String iframeId;
        private int orderId;
        private String redirectionUrl;
        private boolean saveCard;

        // Getters and setters
        public int getIntegration() {
            return integration;
        }

        public void setIntegration(int integration) {
            this.integration = integration;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getGatewayType() {
            return gatewayType;
        }

        public void setGatewayType(String gatewayType) {
            this.gatewayType = gatewayType;
        }

        public String getIframeId() {
            return iframeId;
        }

        public void setIframeId(String iframeId) {
            this.iframeId = iframeId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getRedirectionUrl() {
            return redirectionUrl;
        }

        public void setRedirectionUrl(String redirectionUrl) {
            this.redirectionUrl = redirectionUrl;
        }

        public boolean isSaveCard() {
            return saveCard;
        }

        public void setSaveCard(boolean saveCard) {
            this.saveCard = saveCard;
        }
    }
}
