package com.paymob.sdk.services.intention;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.paymob.sdk.models.common.BillingData;
import com.paymob.sdk.models.common.Item;
import com.paymob.sdk.models.enums.Currency;
import java.util.List;
import java.util.Map;

/**
 * Request object for creating or updating payment intentions.
 */
public class IntentionRequest {
    @JsonProperty("amount")
    private int amount; // in cents, must equal sum of items amounts

    @JsonProperty("currency")
    private Currency currency;

    @JsonProperty("payment_methods")
    private List<Integer> paymentMethods; // Integration IDs

    @JsonProperty("accept_order_id")
    @JsonAlias({ "acceptOrderId" })
    private Long acceptOrderId;

    @JsonProperty("subscription_plan_id")
    @JsonAlias({ "subscriptionPlanId" })
    private Long subscriptionPlanId;

    @JsonProperty("subscriptionv2_id")
    @JsonAlias({ "subscriptionv2Id" })
    private Long subscriptionv2Id;

    @JsonProperty("items")
    private List<Item> items;

    @JsonProperty("billing_data")
    private BillingData billingData;

    @JsonProperty("special_reference")
    private String specialReference;

    @JsonProperty("notification_url")
    private String notificationUrl;

    @JsonProperty("redirection_url")
    private String redirectionUrl;

    @JsonProperty("card_tokens")
    private List<String> cardTokens;

    @JsonProperty("extras")
    private Map<String, Object> extras;

    public IntentionRequest() {
    }

    public IntentionRequest(int amount, Currency currency, List<Item> items, BillingData billingData) {
        this.amount = amount;
        this.currency = currency;
        this.items = items;
        this.billingData = billingData;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and setters
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public List<Integer> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<Integer> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public Long getAcceptOrderId() {
        return acceptOrderId;
    }

    public void setAcceptOrderId(Long acceptOrderId) {
        this.acceptOrderId = acceptOrderId;
    }

    public Long getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(Long subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public Long getSubscriptionv2Id() {
        return subscriptionv2Id;
    }

    public void setSubscriptionv2Id(Long subscriptionv2Id) {
        this.subscriptionv2Id = subscriptionv2Id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public BillingData getBillingData() {
        return billingData;
    }

    public void setBillingData(BillingData billingData) {
        this.billingData = billingData;
    }

    public String getSpecialReference() {
        return specialReference;
    }

    public void setSpecialReference(String specialReference) {
        this.specialReference = specialReference;
    }

    public String getNotificationUrl() {
        return notificationUrl;
    }

    public void setNotificationUrl(String notificationUrl) {
        this.notificationUrl = notificationUrl;
    }

    public String getRedirectionUrl() {
        return redirectionUrl;
    }

    public void setRedirectionUrl(String redirectionUrl) {
        this.redirectionUrl = redirectionUrl;
    }

    public List<String> getCardTokens() {
        return cardTokens;
    }

    public void setCardTokens(List<String> cardTokens) {
        this.cardTokens = cardTokens;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    /**
     * Validates that the total amount equals the sum of all item amounts.
     * 
     * @throws IllegalArgumentException if validation fails
     */
    public void validateAmount() {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items list cannot be null or empty");
        }

        int itemsTotal = items.stream()
                .mapToInt(Item::getTotalAmount)
                .sum();

        if (amount != itemsTotal) {
            throw new IllegalArgumentException(
                    String.format("Amount (%d) must equal sum of item amounts (%d)", amount, itemsTotal));
        }
    }

    public static class Builder {
        private int amount;
        private Currency currency;
        private List<Integer> paymentMethods;
        private Long subscriptionPlanId;
        private Long subscriptionv2Id;
        private List<Item> items;
        private BillingData billingData;
        private String specialReference;
        private String notificationUrl;
        private String redirectionUrl;
        private Map<String, Object> extras;
        private List<String> cardTokens;
        private Long acceptOrderId;

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(Currency currency) {
            this.currency = currency;
            return this;
        }

        public Builder paymentMethods(List<Integer> paymentMethods) {
            this.paymentMethods = paymentMethods;
            return this;
        }

        public Builder subscriptionPlanId(Long subscriptionPlanId) {
            this.subscriptionPlanId = subscriptionPlanId;
            return this;
        }

        public Builder subscriptionv2Id(Long subscriptionv2Id) {
            this.subscriptionv2Id = subscriptionv2Id;
            return this;
        }

        public Builder items(List<Item> items) {
            this.items = items;
            return this;
        }

        public Builder billingData(BillingData billingData) {
            this.billingData = billingData;
            return this;
        }

        public Builder specialReference(String specialReference) {
            this.specialReference = specialReference;
            return this;
        }

        public Builder notificationUrl(String notificationUrl) {
            this.notificationUrl = notificationUrl;
            return this;
        }

        public Builder redirectionUrl(String redirectionUrl) {
            this.redirectionUrl = redirectionUrl;
            return this;
        }

        public Builder extras(Map<String, Object> extras) {
            this.extras = extras;
            return this;
        }

        public Builder cardTokens(List<String> cardTokens) {
            this.cardTokens = cardTokens;
            return this;
        }

        public Builder acceptOrderId(Long acceptOrderId) {
            this.acceptOrderId = acceptOrderId;
            return this;
        }

        public IntentionRequest build() {
            IntentionRequest request = new IntentionRequest(amount, currency, items, billingData);
            request.setPaymentMethods(paymentMethods);
            request.setSubscriptionPlanId(subscriptionPlanId);
            request.setSubscriptionv2Id(subscriptionv2Id);
            request.setSpecialReference(specialReference);
            request.setNotificationUrl(notificationUrl);
            request.setRedirectionUrl(redirectionUrl);
            request.setExtras(extras);
            request.setCardTokens(cardTokens);
            request.setAcceptOrderId(acceptOrderId);
            return request;
        }
    }
}
