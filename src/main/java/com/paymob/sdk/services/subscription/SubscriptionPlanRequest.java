package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for creating subscription plans.
 */
public class SubscriptionPlanRequest {

    @JsonProperty("frequency")
    private int frequency;

    @JsonProperty("name")
    private String name;

    @JsonProperty("reminder_days")
    @JsonAlias({"reminderDays"})
    private int reminderDays;

    @JsonProperty("retrial_days")
    @JsonAlias({"retrialDays"})
    private int retrialDays;

    @JsonProperty("plan_type")
    @JsonAlias({"planType"})
    private String planType;

    @JsonProperty("number_of_deductions")
    @JsonAlias({"numberOfDeductions"})
    private Integer numberOfDeductions;

    @JsonProperty("amount_cents")
    @JsonAlias({"amountCents"})
    private int amountCents;

    @JsonProperty("use_transaction_amount")
    @JsonAlias({"useTransactionAmount"})
    private boolean useTransactionAmount;

    @JsonProperty("is_active")
    @JsonAlias({"isActive"})
    private Boolean isActive;

    @JsonProperty("integration")
    private int integration;

    @JsonProperty("webhook_url")
    @JsonAlias({"webhookUrl"})
    private String webhookUrl;

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReminderDays() {
        return reminderDays;
    }

    public void setReminderDays(int reminderDays) {
        this.reminderDays = reminderDays;
    }

    public int getRetrialDays() {
        return retrialDays;
    }

    public void setRetrialDays(int retrialDays) {
        this.retrialDays = retrialDays;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public Integer getNumberOfDeductions() {
        return numberOfDeductions;
    }

    public void setNumberOfDeductions(Integer numberOfDeductions) {
        this.numberOfDeductions = numberOfDeductions;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(int amountCents) {
        this.amountCents = amountCents;
    }

    public boolean isUseTransactionAmount() {
        return useTransactionAmount;
    }

    public void setUseTransactionAmount(boolean useTransactionAmount) {
        this.useTransactionAmount = useTransactionAmount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public int getIntegration() {
        return integration;
    }

    public void setIntegration(int integration) {
        this.integration = integration;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
}
