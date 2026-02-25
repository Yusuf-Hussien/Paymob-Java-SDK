package com.paymob.sdk.services.savedcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.paymob.sdk.services.intention.IntentionResponse;

/**
 * Response for tokenized payment operations.
 * Extends IntentionResponse since CIT/MIT start with intention creation.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenizedPaymentResponse extends IntentionResponse {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("transaction_id")
    private long transactionId;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "TokenizedPaymentResponse{" +
                "id='" + getId() + '\'' +
                ", clientSecret='" + getClientSecret() + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
