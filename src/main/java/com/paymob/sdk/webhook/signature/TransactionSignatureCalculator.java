package com.paymob.sdk.webhook.signature;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymob.sdk.webhook.WebhookSignatureCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Signature calculator for transaction webhooks.
 * <p>
 * Concatenates 20 fields from {@code obj} in Paymob's required order.
 */
public class TransactionSignatureCalculator extends WebhookSignatureCalculator {

    @Override
    public boolean canHandle(String payload) {
        if (payload == null)
            return false;
        try {
            JsonNode root = objectMapper.readTree(payload);
            return root.has("obj") && !root.has("subscription_data");
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

        fields.add(text(obj, "amount_cents"));
        fields.add(text(obj, "created_at"));
        fields.add(text(obj, "currency"));
        fields.add(text(obj, "error_occured"));
        fields.add(text(obj, "has_parent_transaction"));
        fields.add(text(obj, "id"));
        fields.add(text(obj, "integration_id"));
        fields.add(text(obj, "is_3d_secure"));
        fields.add(text(obj, "is_auth"));
        fields.add(text(obj, "is_capture"));
        fields.add(text(obj, "is_refunded"));
        fields.add(text(obj, "is_standalone_payment"));
        fields.add(text(obj, "is_voided"));

        JsonNode order = obj.get("order");
        fields.add(order != null ? text(order, "id") : "");

        fields.add(text(obj, "owner"));
        fields.add(text(obj, "pending"));

        JsonNode sourceData = obj.get("source_data");
        fields.add(sourceData != null ? text(sourceData, "pan") : "");
        fields.add(sourceData != null ? text(sourceData, "sub_type") : "");
        fields.add(sourceData != null ? text(sourceData, "type") : "");

        fields.add(text(obj, "success"));

        return String.join("", fields);
    }

    private String text(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field == null)
            return "";
        if (field.isBoolean())
            return field.asBoolean() ? "true" : "false";
        return field.asText();
    }
}
