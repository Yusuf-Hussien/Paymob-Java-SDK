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
    private int amount; // in cents, must equal sum of items amounts
    private Currency currency;
    private List<Integer> paymentMethods; // Integration IDs

    @JsonProperty("subscription_plan_id")
    @JsonAlias({"subscriptionPlanId"})
    private Long subscriptionPlanId;

    @JsonProperty("subscriptionv2_id")
    @JsonAlias({"subscriptionv2Id"})
    private Long subscriptionv2Id;

    private List<Item> items;
    private BillingData billingData;
    private String specialReference;
    private String notificationUrl;
    private String redirectionUrl;
    private Map<String, Object> extras;

    public IntentionRequest(int amount, Currency currency, List<Item> items, BillingData billingData) {
        this.amount = amount;
        this.currency = currency;
        this.items = items;
        this.billingData = billingData;
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

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    /**
     * Validates that the total amount equals the sum of all item amounts.
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
}
