package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for subscription plans.
 */
public class SubscriptionPlanResponse {

    @JsonProperty("id")
    private long id;

    @JsonProperty("frequency")
    private Integer frequency;

    @JsonProperty("created_at")
    @JsonAlias({"createdAt"})
    private String createdAt;

    @JsonProperty("updated_at")
    @JsonAlias({"updatedAt"})
    private String updatedAt;

    @JsonProperty("name")
    private String name;

    @JsonProperty("reminder_days")
    @JsonAlias({"reminderDays"})
    private Integer reminderDays;

    @JsonProperty("retrial_days")
    @JsonAlias({"retrialDays"})
    private Integer retrialDays;

    @JsonProperty("plan_type")
    @JsonAlias({"planType"})
    private String planType;

    @JsonProperty("number_of_deductions")
    @JsonAlias({"numberOfDeductions"})
    private Integer numberOfDeductions;

    @JsonProperty("amount_cents")
    @JsonAlias({"amountCents"})
    private Integer amountCents;

    @JsonProperty("use_transaction_amount")
    @JsonAlias({"useTransactionAmount"})
    private Boolean useTransactionAmount;

    @JsonProperty("is_active")
    @JsonAlias({"isActive"})
    private Boolean isActive;

    @JsonProperty("webhook_url")
    @JsonAlias({"webhookUrl"})
    private String webhookUrl;

    @JsonProperty("integration")
    private Integer integration;

    @JsonProperty("fee")
    private Object fee;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReminderDays() {
        return reminderDays;
    }

    public void setReminderDays(Integer reminderDays) {
        this.reminderDays = reminderDays;
    }

    public Integer getRetrialDays() {
        return retrialDays;
    }

    public void setRetrialDays(Integer retrialDays) {
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

    public Integer getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Integer amountCents) {
        this.amountCents = amountCents;
    }

    public Boolean getUseTransactionAmount() {
        return useTransactionAmount;
    }

    public void setUseTransactionAmount(Boolean useTransactionAmount) {
        this.useTransactionAmount = useTransactionAmount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public Integer getIntegration() {
        return integration;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }

    public Object getFee() {
        return fee;
    }

    public void setFee(Object fee) {
        this.fee = fee;
    }
}
