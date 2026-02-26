package com.paymob.sdk.webhook.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymob.sdk.webhook.WebhookEvent;
import com.paymob.sdk.webhook.WebhookEventParser;
import com.paymob.sdk.webhook.WebhookEventType;

/**
 * Parses transaction webhook payloads into {@link WebhookEvent}.
 * Determines event type from {@code success}, {@code is_refunded},
 * {@code is_voided}.
 */
public class TransactionWebhookEventParser implements WebhookEventParser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean canParse(String payload) {
        if (payload == null)
            return false;
        try {
            JsonNode root = objectMapper.readTree(payload);
            if (!root.has("obj") || root.has("subscription_data")) {
                return false;
            }
            if (root.has("type")) {
                String type = root.get("type").asText();
                return "TRANSACTION".equals(type);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public WebhookEvent parse(String payload) {
        WebhookEvent event = new WebhookEvent(payload);

        try {
            JsonNode root = objectMapper.readTree(payload);
            JsonNode obj = root.get("obj");
            if (obj == null)
                return event;

            event.setRoot(root);
            event.setObj(obj);
            event.setData(obj); // Keep for backward compatibility

            JsonNode successNode = obj.get("success");
            if (successNode != null && !successNode.isNull()) {
                event.setSuccess(successNode.asBoolean());
            }

            // Only set type if it looks like a real transaction (has an ID)
            if (obj.has("id")) {
                boolean isRefunded = booleanField(obj, "is_refunded");
                boolean isVoided = booleanField(obj, "is_voided");

                if (isRefunded) {
                    event.setType(WebhookEventType.TRANSACTION_REFUNDED);
                } else if (isVoided) {
                    event.setType(WebhookEventType.TRANSACTION_VOIDED);
                } else if (Boolean.TRUE.equals(event.getSuccess())) {
                    event.setType(WebhookEventType.TRANSACTION_SUCCESSFUL);
                } else {
                    event.setType(WebhookEventType.TRANSACTION_FAILED);
                }
            }
        } catch (Exception ignored) {
        }

        return event;
    }

    private boolean booleanField(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && field.isBoolean() && field.asBoolean();
    }
}
