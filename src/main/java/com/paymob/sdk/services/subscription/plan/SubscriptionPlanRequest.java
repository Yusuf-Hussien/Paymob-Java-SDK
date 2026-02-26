package com.paymob.sdk.services.subscription.plan;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for creating subscription plans.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionPlanRequest {

    @JsonProperty("frequency")
    private int frequency;

    @JsonProperty("name")
    private String name;

    @JsonProperty("reminder_days")
    @JsonAlias({ "reminderDays" })
    private int reminderDays;

    @JsonProperty("retrial_days")
    @JsonAlias({ "retrialDays" })
    private int retrialDays;

    @JsonProperty("plan_type")
    @JsonAlias({ "planType" })
    private String planType;

    @JsonProperty("number_of_deductions")
    @JsonAlias({ "numberOfDeductions" })
    private Integer numberOf_deductions;

    @JsonProperty("amount_cents")
    @JsonAlias({ "amountCents" })
    private int amountCents;

    @JsonProperty("use_transaction_amount")
    @JsonAlias({ "useTransactionAmount" })
    private boolean useTransactionAmount;

    @JsonProperty("is_active")
    @JsonAlias({ "isActive" })
    private Boolean isActive;

    @JsonProperty("integration")
    private int integration;

    @JsonProperty("webhook_url")
    @JsonAlias({ "webhookUrl" })
    private String webhookUrl;

    public SubscriptionPlanRequest() {
    }

    private SubscriptionPlanRequest(Builder builder) {
        this.frequency = builder.frequency;
        this.name = builder.name;
        this.reminderDays = builder.reminderDays;
        this.retrialDays = builder.retrialDays;
        this.planType = builder.planType;
        this.numberOf_deductions = builder.numberOfDeductions;
        this.amountCents = builder.amountCents;
        this.useTransactionAmount = builder.useTransactionAmount;
        this.isActive = builder.isActive;
        this.integration = builder.integration;
        this.webhookUrl = builder.webhookUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

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
        return numberOf_deductions;
    }

    public void setNumberOfDeductions(Integer numberOf_deductions) {
        this.numberOf_deductions = numberOf_deductions;
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

    @Override
    public String toString() {
        return "SubscriptionPlanRequest{" +
                "frequency=" + frequency +
                ", name='" + name + '\'' +
                ", amountCents=" + amountCents +
                ", isActive=" + isActive +
                ", integration=" + integration +
                ", planType='" + planType + '\'' +
                '}';
    }

    public static class Builder {
        private int frequency;
        private String name;
        private int reminderDays;
        private int retrialDays;
        private String planType;
        private Integer numberOfDeductions;
        private int amountCents;
        private boolean useTransactionAmount;
        private Boolean isActive = true;
        private int integration;
        private String webhookUrl;

        public Builder frequency(int frequency) {
            this.frequency = frequency;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder reminderDays(int reminderDays) {
            this.reminderDays = reminderDays;
            return this;
        }

        public Builder retrialDays(int retrialDays) {
            this.retrialDays = retrialDays;
            return this;
        }

        public Builder planType(String planType) {
            this.planType = planType;
            return this;
        }

        public Builder numberOfDeductions(Integer numberOfDeductions) {
            this.numberOfDeductions = numberOfDeductions;
            return this;
        }

        public Builder amountCents(int amountCents) {
            this.amountCents = amountCents;
            return this;
        }

        public Builder useTransactionAmount(boolean useTransactionAmount) {
            this.useTransactionAmount = useTransactionAmount;
            return this;
        }

        public Builder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder integration(int integration) {
            this.integration = integration;
            return this;
        }

        public Builder webhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
            return this;
        }

        public SubscriptionPlanRequest build() {
            return new SubscriptionPlanRequest(this);
        }
    }
}
