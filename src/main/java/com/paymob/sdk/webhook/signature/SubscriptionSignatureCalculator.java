package com.paymob.sdk.webhook.signature;

import com.fasterxml.jackson.databind.JsonNode;
import com.paymob.sdk.webhook.WebhookSignatureCalculator;

/**
 * Signature calculator for subscription webhooks.
 * <p>
 * Concatenation formula: {@code "{trigger_type}for{subscription_data.id}"}
 */
public class SubscriptionSignatureCalculator extends WebhookSignatureCalculator {

    @Override
    public boolean canHandle(String payload) {
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
    protected String buildConcatenatedString(String payload) throws Exception {
        JsonNode root = objectMapper.readTree(payload);
        JsonNode subscriptionData = root.get("subscription_data");
        JsonNode triggerType = root.get("trigger_type");

        if (subscriptionData == null || triggerType == null) {
            throw new IllegalArgumentException(
                    "Invalid payload: missing 'subscription_data' or 'trigger_type'");
        }

        JsonNode idNode = subscriptionData.get("id");
        if (idNode == null || idNode.isNull()) {
            throw new IllegalArgumentException(
                    "Invalid payload: missing 'subscription_data.id'");
        }

        return triggerType.asText("") + "for" + idNode.asText("");
    }
}
