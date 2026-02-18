package com.paymob.sdk.webhook;

/**
 * Represents a Paymob webhook event.
 * Contains transaction data and event type information.
 */
public class WebhookEvent {
    private final String rawPayload;
    private WebhookEventType type;
    private Object data;

    public WebhookEvent(String rawPayload) {
        this.rawPayload = rawPayload;
    }

    public String getRawPayload() {
        return rawPayload;
    }

    public WebhookEventType getType() {
        return type;
    }

    public void setType(WebhookEventType type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
