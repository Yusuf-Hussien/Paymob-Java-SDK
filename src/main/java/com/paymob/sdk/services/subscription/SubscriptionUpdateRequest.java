package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionUpdateRequest {

    @JsonProperty("amount_cents")
    @JsonAlias({"amountCents"})
    private Integer amountCents;

    @JsonProperty("ends_at")
    @JsonAlias({"endsAt"})
    private String endsAt;

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
}
