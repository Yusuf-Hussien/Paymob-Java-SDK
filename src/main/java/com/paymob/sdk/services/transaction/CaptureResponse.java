package com.paymob.sdk.services.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response for capture operations.
 */
public class CaptureResponse {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("transaction_id")
    private Long transactionId;

    @JsonProperty("amount_cents")
    private Integer amountCents;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("pending")
    private Boolean pending;

    @JsonProperty("is_3d_secure")
    private Boolean is3dSecure;

    @JsonProperty("integration_id")
    private Integer integrationId;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("transaction_processed_callback_responses")
    private Object transactionProcessedCallbackResponses;

    @JsonProperty("merchant_commission")
    private Object merchantCommission;

    @JsonProperty("is_capture")
    private Boolean isCapture;

    @JsonProperty("source_data")
    private Object sourceData;

    @JsonProperty("api_source")
    private String apiSource;

    @JsonProperty("created_through")
    private String createdThrough;

    @JsonProperty("sub_category")
    private String subCategory;

    @JsonProperty("data")
    private Object data;

    @JsonProperty("fee")
    private Integer fee;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("refunded_amount_cents")
    private Integer refundedAmountCents;

    @JsonProperty("is_standalone_payment")
    private Boolean isStandalonePayment;

    @JsonProperty("gateway_response")
    private Object gatewayResponse;

    @JsonProperty("settlement_report")
    private Object settlementReport;

    @JsonProperty("owner")
    private Integer owner;

    @JsonProperty("parent_transaction")
    private Object parentTransaction;

    @JsonProperty("acq_response_code")
    private String acqResponseCode;

    @JsonProperty("merchant_iban")
    private String merchantIban;

    @JsonProperty("profile_id")
    private Long profileId;

    @JsonProperty("is_bill")
    private Boolean isBill;

    @JsonProperty("biller")
    private Object biller;

    @JsonProperty("billed_items")
    private Object billedItems;

    @JsonProperty("bill_reference")
    private Object billReference;

    @JsonProperty("reconciliation_reference")
    private Object reconciliationReference;

    @JsonProperty("hide_shipping")
    private Boolean hideShipping;

    @JsonProperty("is_voided")
    private Boolean isVoided;

    @JsonProperty("voided_at")
    private String voidedAt;

    @JsonProperty("is_auth")
    private Boolean isAuth;

    @JsonProperty("is_refunded")
    private Boolean isRefunded;

    @JsonProperty("is_standalone_payment")
    private Boolean isStandalonePaymentFlag;

    @JsonProperty("is_voided")
    private Boolean isVoidedFlag;

    @JsonProperty("error_occured")
    private Boolean errorOccurred;

    @JsonProperty("has_parent_transaction")
    private Boolean hasParentTransaction;

    @JsonProperty("order")
    private Object order;

    @JsonProperty("currency")
    private String currencyFlag;

    @JsonProperty("owner")
    private Integer ownerFlag;

    @JsonProperty("settlement_report")
    private Object settlementReportFlag;

    @JsonProperty("source_data")
    private Object sourceDataFlag;

    @JsonProperty("transaction_processed_callback_responses")
    private Object transactionProcessedCallbackResponsesFlag;

    @JsonProperty("created_at")
    private String createdAtFlag;

    @JsonProperty("updated_at")
    private String updatedAt;

    @JsonProperty("retrieved_at")
    private String retrievedAt;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Integer amountCents) {
        this.amountCents = amountCents;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    public Boolean getIs3dSecure() {
        return is3dSecure;
    }

    public void setIs3dSecure(Boolean is3dSecure) {
        this.is3dSecure = is3dSecure;
    }

    public Integer getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(Integer integrationId) {
        this.integrationId = integrationId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getTransactionProcessedCallbackResponses() {
        return transactionProcessedCallbackResponses;
    }

    public void setTransactionProcessedCallbackResponses(Object transactionProcessedCallbackResponses) {
        this.transactionProcessedCallbackResponses = transactionProcessedCallbackResponses;
    }

    public Object getMerchantCommission() {
        return merchantCommission;
    }

    public void setMerchantCommission(Object merchantCommission) {
        this.merchantCommission = merchantCommission;
    }

    public Boolean getIsCapture() {
        return isCapture;
    }

    public void setIsCapture(Boolean isCapture) {
        this.isCapture = isCapture;
    }

    public Object getSourceData() {
        return sourceData;
    }

    public void setSourceData(Object sourceData) {
        this.sourceData = sourceData;
    }

    public String getApiSource() {
        return apiSource;
    }

    public void setApiSource(String apiSource) {
        this.apiSource = apiSource;
    }

    public String getCreatedThrough() {
        return createdThrough;
    }

    public void setCreatedThrough(String createdThrough) {
        this.createdThrough = createdThrough;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getRefundedAmountCents() {
        return refundedAmountCents;
    }

    public void setRefundedAmountCents(Integer refundedAmountCents) {
        this.refundedAmountCents = refundedAmountCents;
    }

    public Boolean getIsStandalonePayment() {
        return isStandalonePayment;
    }

    public void setIsStandalonePayment(Boolean isStandalonePayment) {
        this.isStandalonePayment = isStandalonePayment;
    }

    public Object getGatewayResponse() {
        return gatewayResponse;
    }

    public void setGatewayResponse(Object gatewayResponse) {
        this.gatewayResponse = gatewayResponse;
    }

    public Object getSettlementReport() {
        return settlementReport;
    }

    public void setSettlementReport(Object settlementReport) {
        this.settlementReport = settlementReport;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public Object getParentTransaction() {
        return parentTransaction;
    }

    public void setParentTransaction(Object parentTransaction) {
        this.parentTransaction = parentTransaction;
    }

    public String getAcqResponseCode() {
        return acqResponseCode;
    }

    public void setAcqResponseCode(String acqResponseCode) {
        this.acqResponseCode = acqResponseCode;
    }

    public String getMerchantIban() {
        return merchantIban;
    }

    public void setMerchantIban(String merchantIban) {
        this.merchantIban = merchantIban;
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Boolean getIsBill() {
        return isBill;
    }

    public void setIsBill(Boolean isBill) {
        this.isBill = isBill;
    }

    public Object getBiller() {
        return biller;
    }

    public void setBiller(Object biller) {
        this.biller = biller;
    }

    public Object getBilledItems() {
        return billedItems;
    }

    public void setBilledItems(Object billedItems) {
        this.billedItems = billedItems;
    }

    public Object getBillReference() {
        return billReference;
    }

    public void setBillReference(Object billReference) {
        this.billReference = billReference;
    }

    public Object getReconciliationReference() {
        return reconciliationReference;
    }

    public void setReconciliationReference(Object reconciliationReference) {
        this.reconciliationReference = reconciliationReference;
    }

    public Boolean getHideShipping() {
        return hideShipping;
    }

    public void setHideShipping(Boolean hideShipping) {
        this.hideShipping = hideShipping;
    }

    public Boolean getIsVoided() {
        return isVoided;
    }

    public void setIsVoided(Boolean isVoided) {
        this.isVoided = isVoided;
    }

    public String getVoidedAt() {
        return voidedAt;
    }

    public void setVoidedAt(String voidedAt) {
        this.voidedAt = voidedAt;
    }

    public Boolean getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Boolean isAuth) {
        this.isAuth = isAuth;
    }

    public Boolean getIsRefunded() {
        return isRefunded;
    }

    public void setIsRefunded(Boolean isRefunded) {
        this.isRefunded = isRefunded;
    }

    public Boolean getIsStandalonePaymentFlag() {
        return isStandalonePaymentFlag;
    }

    public void setIsStandalonePaymentFlag(Boolean isStandalonePaymentFlag) {
        this.isStandalonePaymentFlag = isStandalonePaymentFlag;
    }

    public Boolean getIsVoidedFlag() {
        return isVoidedFlag;
    }

    public void setIsVoidedFlag(Boolean isVoidedFlag) {
        this.isVoidedFlag = isVoidedFlag;
    }

    public Boolean getErrorOccurred() {
        return errorOccurred;
    }

    public void setErrorOccurred(Boolean errorOccurred) {
        this.errorOccurred = errorOccurred;
    }

    public Boolean getHasParentTransaction() {
        return hasParentTransaction;
    }

    public void setHasParentTransaction(Boolean hasParentTransaction) {
        this.hasParentTransaction = hasParentTransaction;
    }

    public Object getOrder() {
        return order;
    }

    public void setOrder(Object order) {
        this.order = order;
    }

    public String getCurrencyFlag() {
        return currencyFlag;
    }

    public void setCurrencyFlag(String currencyFlag) {
        this.currencyFlag = currencyFlag;
    }

    public Integer getOwnerFlag() {
        return ownerFlag;
    }

    public void setOwnerFlag(Integer ownerFlag) {
        this.ownerFlag = ownerFlag;
    }

    public Object getSettlementReportFlag() {
        return settlementReportFlag;
    }

    public void setSettlementReportFlag(Object settlementReportFlag) {
        this.settlementReportFlag = settlementReportFlag;
    }

    public Object getSourceDataFlag() {
        return sourceDataFlag;
    }

    public void setSourceDataFlag(Object sourceDataFlag) {
        this.sourceDataFlag = sourceDataFlag;
    }

    public Object getTransactionProcessedCallbackResponsesFlag() {
        return transactionProcessedCallbackResponsesFlag;
    }

    public void setTransactionProcessedCallbackResponsesFlag(Object transactionProcessedCallbackResponsesFlag) {
        this.transactionProcessedCallbackResponsesFlag = transactionProcessedCallbackResponsesFlag;
    }

    public String getCreatedAtFlag() {
        return createdAtFlag;
    }

    public void setCreatedAtFlag(String createdAtFlag) {
        this.createdAtFlag = createdAtFlag;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRetrievedAt() {
        return retrievedAt;
    }

    public void setRetrievedAt(String retrievedAt) {
        this.retrievedAt = retrievedAt;
    }
}
