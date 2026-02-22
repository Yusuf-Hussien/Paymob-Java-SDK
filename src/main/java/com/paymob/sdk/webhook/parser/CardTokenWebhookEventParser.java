package com.paymob.sdk.webhook.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymob.sdk.webhook.WebhookEvent;
import com.paymob.sdk.webhook.WebhookEventParser;
import com.paymob.sdk.webhook.WebhookEventType;

/**
 * Parses Card Token webhook payloads into {@link WebhookEvent}.
 */
public class CardTokenWebhookEventParser implements WebhookEventParser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean canParse(String payload) {
        if (payload == null)
            return false;
        try {
            JsonNode root = objectMapper.readTree(payload);
            return root.has("type") && "TOKEN".equals(root.get("type").asText());
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
            event.setType(WebhookEventType.CARD_TOKEN);

            if (obj.has("token")) {
                event.setData(obj.get("token").asText());
            } else {
                event.setData(obj); // Fallback to full object if token is missing
            }

        } catch (Exception ignored) {
        }

        return event;
    }
}
