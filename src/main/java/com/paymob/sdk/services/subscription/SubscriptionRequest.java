package com.paymob.sdk.services.subscription;

/**
 * Request for creating customer subscriptions.
 */
public class SubscriptionRequest {
    private long planId;
    private String customerEmail;
    private String customerPhone;
    private String cardToken; // for saved card payments
    private String merchantOrderId;

    public SubscriptionRequest(long planId, String customerEmail, String customerPhone) {
        this.planId = planId;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
    }

    // Getters and setters
    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }
}
