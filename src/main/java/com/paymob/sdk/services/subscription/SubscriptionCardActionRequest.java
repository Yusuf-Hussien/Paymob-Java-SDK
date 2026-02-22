package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionCardActionRequest {

    @JsonProperty("card")
    private long card;

    public SubscriptionCardActionRequest() {
    }

    public SubscriptionCardActionRequest(long card) {
        this.card = card;
    }

    public long getCard() {
        return card;
    }

    public void setCard(long card) {
        this.card = card;
    }
}
