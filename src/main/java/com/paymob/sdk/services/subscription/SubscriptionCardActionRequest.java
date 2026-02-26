package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request for card actions on a subscription (e.g., delete-card,
 * change-primary-card).
 */
public class SubscriptionCardActionRequest {
    @JsonProperty("card")
    private long cardId;

    public SubscriptionCardActionRequest() {
    }

    public SubscriptionCardActionRequest(long cardId) {
        this.cardId = cardId;
    }

    public long getCardId() {
        return cardId;
    }

    public void setCardId(long cardId) {
        this.cardId = cardId;
    }
}
