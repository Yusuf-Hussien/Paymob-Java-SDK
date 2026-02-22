package com.paymob.sdk.webhook;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Represents a Paymob webhook event.
 * <p>
 * This is a simple data holder. Event type detection is handled by
 * {@link WebhookEventParser} implementations, not in this class.
 */
public class WebhookEvent {
    private final String rawPayload;
    private WebhookEventType type;
    private Object data;
    private Boolean success;
    private JsonNode obj;
    private JsonNode root;

    public WebhookEvent(String rawPayload) {
        this.rawPayload = rawPayload;
    }

    public String getRawPayload() {
        return rawPayload;
    }

    public JsonNode getRoot() {
        return root;
    }

    public void setRoot(JsonNode root) {
        this.root = root;
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

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public JsonNode getObj() {
        return obj;
    }

    public void setObj(JsonNode obj) {
        this.obj = obj;
    }
}
