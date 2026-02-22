package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for updating subscription plans.
 * Updatable fields: number_of_deductions, amount_cents, integration.
 */
public class SubscriptionPlanUpdateRequest {

    @JsonProperty("number_of_deductions")
    @JsonAlias({"numberOfDeductions"})
    private Integer numberOfDeductions;

    @JsonProperty("amount_cents")
    @JsonAlias({"amountCents"})
    private Integer amountCents;

    @JsonProperty("integration")
    private Integer integration;

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
}
