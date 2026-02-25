package com.paymob.sdk.services.savedcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for Merchant Initiated Transaction (MIT).
 * Automated charges for subscriptions/top-ups, no customer present.
 * This model is typically used to create an intention for recurrent payments.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MitPaymentRequest {
    @JsonProperty("card_token")
    private String cardToken;

    @JsonProperty("amount")
    private int amount;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("special_reference")
    private String merchantOrderId;

    public MitPaymentRequest() {
    }

    private MitPaymentRequest(Builder builder) {
        this.cardToken = builder.cardToken;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.merchantOrderId = builder.merchantOrderId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
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

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    @Override
    public String toString() {
        return "MitPaymentRequest{" +
                "cardToken='" + cardToken + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", merchantOrderId='" + merchantOrderId + '\'' +
                '}';
    }

    public static class Builder {
        private String cardToken;
        private int amount;
        private String currency;
        private String merchantOrderId;

        public Builder cardToken(String cardToken) {
            this.cardToken = cardToken;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder merchantOrderId(String merchantOrderId) {
            this.merchantOrderId = merchantOrderId;
            return this;
        }

        public MitPaymentRequest build() {
            return new MitPaymentRequest(this);
        }
    }
}
