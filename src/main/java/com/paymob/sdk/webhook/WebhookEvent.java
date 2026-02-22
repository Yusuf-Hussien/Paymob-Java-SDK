package com.paymob.sdk.webhook;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Represents a Paymob webhook event.
 * Contains transaction data and event type information.
 */
public class WebhookEvent {
    private final String rawPayload;
    private WebhookEventType type;
    private Object data;
    private Boolean success;
    private JsonNode obj;

    public WebhookEvent(String rawPayload) {
        this.rawPayload = rawPayload;

        if (rawPayload == null) {
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(rawPayload);
            JsonNode objNode = root.get("obj");
            this.obj = objNode;
            this.data = objNode;

            if (objNode == null) {
                return;
            }

            JsonNode successNode = objNode.get("success");
            if (successNode != null && successNode.isBoolean()) {
                this.success = successNode.asBoolean();
            }

            boolean isRefunded = getBoolean(objNode, "is_refunded");
            boolean isVoided = getBoolean(objNode, "is_voided");

            if (isRefunded) {
                this.type = WebhookEventType.TRANSACTION_REFUNDED;
            } else if (isVoided) {
                this.type = WebhookEventType.TRANSACTION_VOIDED;
            } else if (Boolean.TRUE.equals(this.success)) {
                this.type = WebhookEventType.TRANSACTION_SUCCESSFUL;
            } else if (Boolean.FALSE.equals(this.success)) {
                this.type = WebhookEventType.TRANSACTION_FAILED;
            }
        } catch (Exception ignored) {
        }
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

    public Boolean getSuccess() {
        return success;
    }

    public JsonNode getObj() {
        return obj;
    }

    private static boolean getBoolean(JsonNode node, String fieldName) {
        if (node == null) {
            return false;
        }
        JsonNode field = node.get(fieldName);
        return field != null && field.isBoolean() && field.asBoolean();
    }
}
