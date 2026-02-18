package com.paymob.sdk.services.transaction;

/**
 * Request for voiding a same-day transaction.
 */
public class VoidRequest {
    private long transactionId;

    public VoidRequest(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }
}
