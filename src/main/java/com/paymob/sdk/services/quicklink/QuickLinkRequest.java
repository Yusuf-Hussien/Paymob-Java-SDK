package com.paymob.sdk.services.quicklink;

import java.io.File;

/**
 * Request for creating shareable payment links.
 */
public class QuickLinkRequest {
    private int amountCents;
    private int paymentMethods; // Integration ID
    private boolean isLive;
    private String fullName;
    private String customerEmail;
    private String customerPhone;
    private String description;
    private String referenceId;
    private String notificationUrl;
    private String expiresAt;
    private File paymentLinkImage;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public File getPaymentLinkImage() {
        return paymentLinkImage;
    }

    public void setPaymentLinkImage(File paymentLinkImage) {
        this.paymentLinkImage = paymentLinkImage;
    }
}
