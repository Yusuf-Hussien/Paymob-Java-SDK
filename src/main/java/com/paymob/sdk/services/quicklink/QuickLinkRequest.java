package com.paymob.sdk.services.quicklink;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paymob.sdk.models.enums.Currency;
import java.io.File;

/**
 * Request for creating shareable payment links.
 */
public class QuickLinkRequest {
    @JsonProperty("amount_cents")
    private int amountCents;

    @JsonProperty("payment_methods")
    private int paymentMethods; // Integration ID

    @JsonProperty("is_live")
    private boolean isLive;

    @JsonProperty("currency")
    private Currency currency;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("email")
    private String customerEmail;

    @JsonProperty("phone_number")
    private String customerPhone;

    @JsonProperty("description")
    private String description;

    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("notification_url")
    private String notificationUrl;

    @JsonProperty("expires_at")
    private String expiresAt;

    private File paymentLinkImage;

    public QuickLinkRequest() {
    }

    public QuickLinkRequest(int amountCents, int paymentMethods, Currency currency, boolean isLive) {
        this.amountCents = amountCents;
        this.paymentMethods = paymentMethods;
        this.currency = currency;
        this.isLive = isLive;
    }

    public static Builder builder() {
        return new Builder();
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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

    public static class Builder {
        private int amountCents;
        private int paymentMethods;
        private boolean isLive;
        private Currency currency;
        private String fullName;
        private String customerEmail;
        private String customerPhone;
        private String description;
        private String referenceId;
        private String notificationUrl;
        private String expiresAt;
        private File paymentLinkImage;

        public Builder amountCents(int amountCents) {
            this.amountCents = amountCents;
            return this;
        }

        public Builder paymentMethods(int paymentMethods) {
            this.paymentMethods = paymentMethods;
            return this;
        }

        public Builder isLive(boolean isLive) {
            this.isLive = isLive;
            return this;
        }

        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public Builder customerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
            return this;
        }

        public Builder customerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder referenceId(String referenceId) {
            this.referenceId = referenceId;
            return this;
        }

        public Builder notificationUrl(String notificationUrl) {
            this.notificationUrl = notificationUrl;
            return this;
        }

        public Builder expiresAt(String expiresAt) {
            this.expiresAt = expiresAt;
            return this;
        }

        public Builder paymentLinkImage(File paymentLinkImage) {
            this.paymentLinkImage = paymentLinkImage;
            return this;
        }

        public QuickLinkRequest build() {
            QuickLinkRequest request = new QuickLinkRequest(amountCents, paymentMethods, currency, isLive);
            request.setFullName(fullName);
            request.setCustomerEmail(customerEmail);
            request.setCustomerPhone(customerPhone);
            request.setDescription(description);
            request.setReferenceId(referenceId);
            request.setNotificationUrl(notificationUrl);
            request.setExpiresAt(expiresAt);
            request.setPaymentLinkImage(paymentLinkImage);
            return request;
        }
    }
}
