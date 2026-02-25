package com.paymob.sdk.services.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for refunding a transaction (full or partial).
 */
public class RefundRequest {
    @JsonProperty("transaction_id")
    private long transactionId;

    @JsonProperty("amount_cents")
    private int amountCents;

    public RefundRequest() {
    }

    private RefundRequest(Builder builder) {
        this.transactionId = builder.transactionId;
        this.amountCents = builder.amountCents;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long transactionId;
        private int amountCents;

        public Builder transactionId(long transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder amountCents(int amountCents) {
            this.amountCents = amountCents;
            return this;
        }

        public RefundRequest build() {
            return new RefundRequest(this);
        }
    }

    @Override
    public String toString() {
        return "RefundRequest{" +
                "transactionId=" + transactionId +
                ", amountCents=" + amountCents +
                '}';
    }
}
