package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response for subscription operations.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionResponse {
    @JsonProperty("id")
    private long id;

    @JsonProperty("state")
    private String state;

    @JsonProperty("frequency")
    private int frequency;

    @JsonProperty("amount_cents")
    private int amountCents;

    @JsonProperty("plan_id")
    private long planId;

    @JsonProperty("starts_at")
    private String startsAt;

    @JsonProperty("next_billing")
    private String nextBilling;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(int amountCents) {
        this.amountCents = amountCents;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public String getNextBilling() {
        return nextBilling;
    }

    public void setNextBilling(String nextBilling) {
        this.nextBilling = nextBilling;
    }

    @Override
    public String toString() {
        return "SubscriptionResponse{" +
                "id=" + id +
                ", state='" + state + '\'' +
                ", planId=" + planId +
                ", amountCents=" + amountCents +
                '}';
    }
}
