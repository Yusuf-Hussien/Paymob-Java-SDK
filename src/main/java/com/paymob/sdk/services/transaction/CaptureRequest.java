package com.paymob.sdk.services.transaction;

/**
 * Request for capturing an authorize-only transaction.
 */
public class CaptureRequest {
    private long transactionId;
    private int amountCents;

    public CaptureRequest(long transactionId, int amountCents) {
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
