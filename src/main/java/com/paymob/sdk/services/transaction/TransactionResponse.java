package com.paymob.sdk.services.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.paymob.sdk.models.common.Transaction;

/**
 * Response for transaction operations (refund, void, capture).
 * Wraps the full transaction details returned by the API.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResponse extends Transaction {
    @JsonProperty("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "id=" + getId() +
                ", success=" + isSuccess() +
                ", message='" + message + '\'' +
                '}';
    }
}
