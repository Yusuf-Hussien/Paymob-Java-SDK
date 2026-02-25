package com.paymob.sdk.services.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for voiding a same-day transaction.
 */
public class VoidRequest {
    @JsonProperty("transaction_id")
    private long transactionId;

    public VoidRequest() {
    }

    private VoidRequest(Builder builder) {
        this.transactionId = builder.transactionId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long transactionId;

        public Builder transactionId(long transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public VoidRequest build() {
            return new VoidRequest(this);
        }
    }

    @Override
    public String toString() {
        return "VoidRequest{" +
                "transactionId=" + transactionId +
                '}';
    }
}
