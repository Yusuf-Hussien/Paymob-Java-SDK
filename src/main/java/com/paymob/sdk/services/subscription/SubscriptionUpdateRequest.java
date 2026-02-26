package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for updating subscription details.
 */
public class SubscriptionUpdateRequest {

    @JsonProperty("amount_cents")
    @JsonAlias({ "amountCents" })
    private Integer amountCents;

    @JsonProperty("ends_at")
    @JsonAlias({ "endsAt" })
    private String endsAt;

    public SubscriptionUpdateRequest() {
    }

    private SubscriptionUpdateRequest(Builder builder) {
        this.amountCents = builder.amountCents;
        this.endsAt = builder.endsAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(Integer amountCents) {
        this.amountCents = amountCents;
    }

    public String getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(String endsAt) {
        this.endsAt = endsAt;
    }

    public static class Builder {
        private Integer amountCents;
        private String endsAt;

        public Builder amountCents(Integer amountCents) {
            this.amountCents = amountCents;
            return this;
        }

        public Builder endsAt(String endsAt) {
            this.endsAt = endsAt;
            return this;
        }

        public SubscriptionUpdateRequest build() {
            return new SubscriptionUpdateRequest(this);
        }
    }
}
