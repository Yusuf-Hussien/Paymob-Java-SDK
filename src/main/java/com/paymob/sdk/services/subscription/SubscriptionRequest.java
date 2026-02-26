package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for creating customer subscriptions.
 * Note: Enrollment usually happens via Intention API by passing
 * subscription_plan_id.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionRequest {
    @JsonProperty("plan")
    private long planId;

    @JsonProperty("customer_email")
    private String customerEmail;

    @JsonProperty("customer_phone")
    private String customerPhone;

    @JsonProperty("card_token")
    private String cardToken;

    @JsonProperty("merchant_order_id")
    private String merchantOrderId;

    public SubscriptionRequest() {
    }

    private SubscriptionRequest(Builder builder) {
        this.planId = builder.planId;
        this.customerEmail = builder.customerEmail;
        this.customerPhone = builder.customerPhone;
        this.cardToken = builder.cardToken;
        this.merchantOrderId = builder.merchantOrderId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    @Override
    public String toString() {
        return "SubscriptionRequest{" +
                "planId=" + planId +
                ", customerEmail='" + customerEmail + '\'' +
                ", merchantOrderId='" + merchantOrderId + '\'' +
                '}';
    }

    public static class Builder {
        private long planId;
        private String customerEmail;
        private String customerPhone;
        private String cardToken;
        private String merchantOrderId;

        public Builder planId(long planId) {
            this.planId = planId;
            return this;
        }

        public Builder customerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
            return this;
        }

        public Builder customerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
            return this;
        }

        public Builder cardToken(String cardToken) {
            this.cardToken = cardToken;
            return this;
        }

        public Builder merchantOrderId(String merchantOrderId) {
            this.merchantOrderId = merchantOrderId;
            return this;
        }

        public SubscriptionRequest build() {
            return new SubscriptionRequest(this);
        }
    }
}
