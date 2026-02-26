package com.paymob.sdk.webhook.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymob.sdk.webhook.WebhookEvent;
import com.paymob.sdk.webhook.WebhookEventParser;
import com.paymob.sdk.webhook.WebhookEventType;

/**
 * Parses subscription webhook payloads into {@link WebhookEvent}.
 * Maps {@code trigger_type} to the corresponding {@link WebhookEventType}.
 */
public class SubscriptionWebhookEventParser implements WebhookEventParser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean canParse(String payload) {
        if (payload == null)
            return false;
        try {
            JsonNode root = objectMapper.readTree(payload);
            return root.has("subscription_data") && root.has("trigger_type");
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public WebhookEvent parse(String payload) {
        WebhookEvent event = new WebhookEvent(payload);

        try {
            JsonNode root = objectMapper.readTree(payload);
            JsonNode subscriptionData = root.get("subscription_data");
            JsonNode triggerType = root.get("trigger_type");

            event.setRoot(root);
            if (subscriptionData != null) {
                event.setObj(subscriptionData);
                event.setData(subscriptionData);
            }

            if (triggerType != null && !triggerType.isNull()) {
                String trigger = triggerType.asText("");
                WebhookEventType type = WebhookEventType.fromTriggerType(trigger);
                event.setType(type);
                event.setSuccess(true); // Subscription events generally imply success of the trigger
            }
        } catch (Exception ignored) {
        }

        return event;
    }
}
