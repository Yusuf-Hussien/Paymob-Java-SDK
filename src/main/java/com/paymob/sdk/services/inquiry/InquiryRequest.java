package com.paymob.sdk.services.inquiry;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for transaction inquiry operations.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InquiryRequest {
    @JsonProperty("merchant_order_id")
    @JsonAlias({ "merchantOrderId" })
    private String merchantOrderId;

    @JsonProperty("order_id")
    @JsonAlias({ "orderId" })
    private Integer orderId;

    @JsonProperty("transaction_id")
    @JsonAlias({ "transactionId" })
    private Long transactionId;

    public InquiryRequest() {
    }

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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String merchantOrderId;
        private Integer orderId;
        private Long transactionId;

        public Builder merchantOrderId(String merchantOrderId) {
            this.merchantOrderId = merchantOrderId;
            return this;
        }

        public Builder orderId(Integer orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder transactionId(Long transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public InquiryRequest build() {
            InquiryRequest request = new InquiryRequest();
            request.setMerchantOrderId(merchantOrderId);
            request.setOrderId(orderId);
            request.setTransactionId(transactionId);
            return request;
        }
    }
}
