package com.paymob.sdk.services.intention;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.paymob.sdk.models.common.BillingData;
import com.paymob.sdk.models.common.Item;

/**
 * Response object for payment intention operations.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntentionResponse {
    @JsonProperty("client_secret")
    private String clientSecret;

    @JsonProperty("redirection_url")
    private String redirectionUrl;

    @JsonProperty("special_reference")
    private String specialReference;

    @JsonProperty("payment_keys")
    private JsonNode paymentKeys;

    @JsonProperty("id")
    private String id;

    @JsonProperty("intention_order_id")
    private Long intentionOrderId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("confirmed")
    private boolean confirmed;

    @JsonProperty("created")
    private String created;

    @JsonProperty("intention_detail")
    private IntentionDetail intentionDetail;

    @JsonProperty("extras")
    private Map<String, Object> extras;

    /**
     * Helper to get amount from nested intention detail.
     * 
     * @return amount in cents
     */
    public int getAmount() {
        return intentionDetail != null ? intentionDetail.getAmount() : 0;
    }

    /**
     * Helper to get currency from nested intention detail.
     * 
     * @return currency code
     */
    public String getCurrency() {
        return intentionDetail != null ? intentionDetail.getCurrency() : null;
    }

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

    public JsonNode getPaymentKeys() {
        return paymentKeys;
    }

    public void setPaymentKeys(JsonNode paymentKeys) {
        this.paymentKeys = paymentKeys;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIntentionOrderId() {
        return intentionOrderId;
    }

    public void setIntentionOrderId(Long intentionOrderId) {
        this.intentionOrderId = intentionOrderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public IntentionDetail getIntentionDetail() {
        return intentionDetail;
    }

    public void setIntentionDetail(IntentionDetail intentionDetail) {
        this.intentionDetail = intentionDetail;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    @Override
    public String toString() {
        return "IntentionResponse{" +
                "id='" + id + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + getAmount() +
                ", currency='" + getCurrency() + '\'' +
                ", paymentKeys=" + paymentKeys +
                '}';
    }

    /**
     * Nested detail object for intention.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IntentionDetail {
        @JsonProperty("amount")
        private int amount;

        @JsonProperty("currency")
        private String currency;

        @JsonProperty("items")
        private List<Item> items;

        @JsonProperty("billing_data")
        private BillingData billingData;

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

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public BillingData getBillingData() {
            return billingData;
        }

        public void setBillingData(BillingData billingData) {
            this.billingData = billingData;
        }
    }

    /**
     * Payment key information for different payment methods.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaymentKey {
        @JsonProperty("integration")
        private int integration;

        @JsonProperty("key")
        private String key;

        @JsonProperty("gateway_type")
        private String gatewayType;

        @JsonProperty("iframe_id")
        private String iframeId;

        @JsonProperty("order_id")
        private Integer orderId;

        @JsonProperty("redirection_url")
        private String redirectionUrl;

        @JsonProperty("save_card")
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

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
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
