package com.paymob.sdk.models.payments;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentKeyRequest {
    @JsonProperty("auth_token")
    private String authToken;
    @JsonProperty("amount_cents")
    private String amountCents;
    @JsonProperty("expiration")
    private int expiration; // seconds
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("billing_data")
    private BillingData billingData;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("integration_id")
    private int integrationId;
    @JsonProperty("lock_order_when_paid")
    private boolean lockOrderWhenPaid;

    // Billing Data Helper Class
    public static class BillingData {
        @JsonProperty("apartment")
        public String apartment;
        @JsonProperty("email")
        public String email;
        @JsonProperty("floor")
        public String floor;
        @JsonProperty("first_name")
        public String firstName;
        @JsonProperty("street")
        public String street;
        @JsonProperty("building")
        public String building;
        @JsonProperty("phone_number")
        public String phoneNumber;
        @JsonProperty("shipping_method")
        public String shippingMethod;
        @JsonProperty("postal_code")
        public String postalCode;
        @JsonProperty("city")
        public String city;
        @JsonProperty("country")
        public String country;
        @JsonProperty("last_name")
        public String lastName;
        @JsonProperty("state")
        public String state;

        public BillingData() {
            this.shippingMethod = "NA"; // Default often needed
            this.apartment = "NA";
            this.floor = "NA";
            this.building = "NA";
            this.postalCode = "NA";
            this.state = "NA";
        }
    }

    // Constructors, Getters, Setters
    public PaymentKeyRequest() {
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(String amountCents) {
        this.amountCents = amountCents;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BillingData getBillingData() {
        return billingData;
    }

    public void setBillingData(BillingData billingData) {
        this.billingData = billingData;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(int integrationId) {
        this.integrationId = integrationId;
    }

    public boolean isLockOrderWhenPaid() {
        return lockOrderWhenPaid;
    }

    public void setLockOrderWhenPaid(boolean lockOrderWhenPaid) {
        this.lockOrderWhenPaid = lockOrderWhenPaid;
    }
}
