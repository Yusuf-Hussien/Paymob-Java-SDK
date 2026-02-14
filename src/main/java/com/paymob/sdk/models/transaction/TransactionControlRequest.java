package com.paymob.sdk.models.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionControlRequest {
    @JsonProperty("transaction_id")
    private long transactionId;

    @JsonProperty("amount_cents")
    private Integer amountCents;

    public TransactionControlRequest() {
    }

    public TransactionControlRequest(long transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionControlRequest(long transactionId, int amountCents) {
        this.transactionId = transactionId;
        this.amountCents = amountCents;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Integer amountCents) {
        this.amountCents = amountCents;
    }
}
