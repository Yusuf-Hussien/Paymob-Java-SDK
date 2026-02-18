package com.paymob.sdk.services.inquiry;

/**
 * Response for transaction inquiry operations.
 */
public class InquiryResponse {
    private Object transactionData;

    public Object getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(Object transactionData) {
        this.transactionData = transactionData;
    }
}
