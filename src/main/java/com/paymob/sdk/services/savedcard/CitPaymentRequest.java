package com.paymob.sdk.services.savedcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for Customer Initiated Transaction (CIT).
 * Customer is present at checkout, requires CVV re-entry.
 * This model is typically used to create an intention with a saved card token.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CitPaymentRequest {
    @JsonProperty("card_token")
    private String cardToken;

    @JsonProperty("cvv")
    private String cvv;

    @JsonProperty("amount")
    private int amount;

    @JsonProperty("currency")
    private String currency;

    public CitPaymentRequest() {
    }

    private CitPaymentRequest(Builder builder) {
        this.cardToken = builder.cardToken;
        this.cvv = builder.cvv;
        this.amount = builder.amount;
        this.currency = builder.currency;
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

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
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

    @Override
    public String toString() {
        return "CitPaymentRequest{" +
                "cardToken='" + cardToken + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }

    public static class Builder {
        private String cardToken;
        private String cvv;
        private int amount;
        private String currency;

        public Builder cardToken(String cardToken) {
            this.cardToken = cardToken;
            return this;
        }

        public Builder cvv(String cvv) {
            this.cvv = cvv;
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

        public CitPaymentRequest build() {
            return new CitPaymentRequest(this);
        }
    }
}
