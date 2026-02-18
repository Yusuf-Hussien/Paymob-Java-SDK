package com.paymob.sdk.services.quicklink;

/**
 * Request for creating shareable payment links.
 */
public class QuickLinkRequest {
    private int amountCents;
    private int paymentMethods; // Integration ID
    private boolean isLive;
    private String customerEmail;
    private String customerPhone;
    private String merchantOrderId;

    public QuickLinkRequest(int amountCents, int paymentMethods, boolean isLive) {
        this.amountCents = amountCents;
        this.paymentMethods = paymentMethods;
        this.isLive = isLive;
    }

    // Getters and setters
    public int getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(int amountCents) {
        this.amountCents = amountCents;
    }

    public int getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(int paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
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

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }
}
