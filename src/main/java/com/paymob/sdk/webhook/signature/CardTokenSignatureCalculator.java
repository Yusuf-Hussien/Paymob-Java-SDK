package com.paymob.sdk.webhook.signature;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymob.sdk.webhook.WebhookSignatureCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature calculator for Card Token webhooks.
 * <p>
 * Concatenates fields in lexicographical order:
 * card_subtype, created_at, email, id, masked_pan, merchant_id, order_id, token
 */
public class CardTokenSignatureCalculator extends WebhookSignatureCalculator {

    @Override
    public boolean canHandle(String payload) {
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
    protected String buildConcatenatedString(String payload) throws Exception {
        JsonNode root = objectMapper.readTree(payload);
        JsonNode obj = root.get("obj");

        if (obj == null) {
            throw new IllegalArgumentException("Invalid payload: missing 'obj' field");
        }

        List<String> fields = new ArrayList<>();

        // Sort order: card_subtype, created_at, email, id, masked_pan, merchant_id,
        // order_id, token
        fields.add(text(obj, "card_subtype"));
        fields.add(text(obj, "created_at"));
        fields.add(text(obj, "email"));
        fields.add(text(obj, "id"));
        fields.add(text(obj, "masked_pan"));
        fields.add(text(obj, "merchant_id"));
        fields.add(text(obj, "order_id"));
        fields.add(text(obj, "token"));

        return String.join("", fields);
    }

    private String text(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field == null || field.isNull())
            return "";
        return field.asText();
    }
}
