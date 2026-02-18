package com.paymob.sdk.services.transaction;

/**
 * Request for refunding a transaction (full or partial).
 */
public class RefundRequest {
    private long transactionId;
    private int amountCents;

    public RefundRequest(long transactionId, int amountCents) {
        this.transactionId = transactionId;
        this.amountCents = amountCents;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(int amountCents) {
        this.amountCents = amountCents;
    }
}
