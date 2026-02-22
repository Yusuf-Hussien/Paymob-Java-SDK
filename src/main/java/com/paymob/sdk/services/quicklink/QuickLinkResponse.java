package com.paymob.sdk.services.quicklink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response for QuickLink operations.
 */
public class QuickLinkResponse {
    @JsonProperty("id")
    private long id;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("client_info")
    private ClientInfo clientInfo;

    @JsonProperty("reference_id")
    @JsonAlias({"referenceId"})
    private String referenceId;

    @JsonProperty("shorten_url")
    @JsonAlias({"shortenUrl"})
    private String shortenUrl;

    private boolean success;
    private String message;

    @JsonProperty("client_url")
    @JsonAlias({"clientUrl"})
    private String clientUrl;

    @JsonProperty("payment_link_id")
    @JsonAlias({"link_id", "linkId"})
    private String linkId;

    @JsonProperty("amount_cents")
    @JsonAlias({"amountCents"})
    private int amountCents;

    @JsonProperty("payment_link_image")
    @JsonAlias({"paymentLinkImage"})
    private String paymentLinkImage;

    @JsonProperty("description")
    private String description;

    @JsonProperty("created_at")
    @JsonAlias({"createdAt"})
    private String createdAt;

    @JsonProperty("expires_at")
    @JsonAlias({"expiresAt"})
    private String expiresAt;

    @JsonProperty("origin")
    private Integer origin;

    @JsonProperty("merchant_staff_tag")
    @JsonAlias({"merchantStaffTag"})
    private String merchantStaffTag;

    @JsonProperty("state")
    private String state;

    @JsonProperty("paid_at")
    @JsonAlias({"paidAt"})
    private String paidAt;

    @JsonProperty("redirection_url")
    @JsonAlias({"redirectionUrl"})
    private String redirectionUrl;

    @JsonProperty("notification_url")
    @JsonAlias({"notificationUrl"})
    private String notificationUrl;

    @JsonProperty("order")
    private Long order;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getShortenUrl() {
        return shortenUrl;
    }

    public void setShortenUrl(String shortenUrl) {
        this.shortenUrl = shortenUrl;
    }

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

    public String getClientUrl() {
        return clientUrl;
    }

    public void setClientUrl(String clientUrl) {
        this.clientUrl = clientUrl;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(int amountCents) {
        this.amountCents = amountCents;
    }

    public String getPaymentLinkImage() {
        return paymentLinkImage;
    }

    public void setPaymentLinkImage(String paymentLinkImage) {
        this.paymentLinkImage = paymentLinkImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }

    public String getMerchantStaffTag() {
        return merchantStaffTag;
    }

    public void setMerchantStaffTag(String merchantStaffTag) {
        this.merchantStaffTag = merchantStaffTag;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public String getRedirectionUrl() {
        return redirectionUrl;
    }

    public void setRedirectionUrl(String redirectionUrl) {
        this.redirectionUrl = redirectionUrl;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public static class ClientInfo {
        @JsonProperty("email")
        private String email;

        @JsonProperty("full_name")
        @JsonAlias({"fullName"})
        private String fullName;

        @JsonProperty("phone_number")
        @JsonAlias({"phoneNumber"})
        private String phoneNumber;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }
}
