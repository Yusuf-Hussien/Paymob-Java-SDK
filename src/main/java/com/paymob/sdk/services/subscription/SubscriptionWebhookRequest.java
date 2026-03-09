package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request payload for registering or updating subscription webhook URL.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionWebhookRequest {
    @JsonProperty("url")
    private String url;

    public SubscriptionWebhookRequest() {
    }

    public SubscriptionWebhookRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}