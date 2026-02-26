package com.paymob.sdk.services.subscription.plan;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for updating subscription plans.
 * Updatable fields: number_of_deductions, amount_cents, integration.
 */
public class SubscriptionPlanUpdateRequest {

    @JsonProperty("number_of_deductions")
    @JsonAlias({ "numberOfDeductions" })
    private Integer numberOfDeductions;

    @JsonProperty("amount_cents")
    @JsonAlias({ "amountCents" })
    private Integer amountCents;

    @JsonProperty("integration")
    private Integer integration;

    public SubscriptionPlanUpdateRequest() {
    }

    private SubscriptionPlanUpdateRequest(Builder builder) {
        this.numberOfDeductions = builder.numberOfDeductions;
        this.amountCents = builder.amountCents;
        this.integration = builder.integration;
    }

    public static Builder builder() {
        return new Builder();
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

    public Integer getIntegration() {
        return integration;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }

    public static class Builder {
        private Integer numberOfDeductions;
        private Integer amountCents;
        private Integer integration;

        public Builder numberOfDeductions(Integer numberOfDeductions) {
            this.numberOfDeductions = numberOfDeductions;
            return this;
        }

        public Builder amountCents(Integer amountCents) {
            this.amountCents = amountCents;
            return this;
        }

        public Builder integration(Integer integration) {
            this.integration = integration;
            return this;
        }

        public SubscriptionPlanUpdateRequest build() {
            return new SubscriptionPlanUpdateRequest(this);
        }
    }
}
