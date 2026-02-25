package com.paymob.sdk.services.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for capturing an authorize-only transaction.
 */
public class CaptureRequest {
    @JsonProperty("transaction_id")
    private long transactionId;

    @JsonProperty("amount_cents")
    private int amountCents;

    public CaptureRequest() {
    }

    private CaptureRequest(Builder builder) {
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

        public CaptureRequest build() {
            return new CaptureRequest(this);
        }
    }

    @Override
    public String toString() {
        return "CaptureRequest{" +
                "transactionId=" + transactionId +
                ", amountCents=" + amountCents +
                '}';
    }
}
