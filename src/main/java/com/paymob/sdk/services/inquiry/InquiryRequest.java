package com.paymob.sdk.services.inquiry;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for transaction inquiry operations.
 */
public class InquiryRequest {
    @JsonProperty("merchant_order_id")
    @JsonAlias({"merchantOrderId"})
    private String merchantOrderId;

    @JsonProperty("order_id")
    @JsonAlias({"orderId"})
    private Integer orderId;

    @JsonProperty("transaction_id")
    @JsonAlias({"transactionId"})
    private Long transactionId;

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
}
