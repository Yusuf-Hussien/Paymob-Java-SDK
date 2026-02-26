package com.paymob.sdk.webhook;

import com.paymob.sdk.webhook.parser.CardTokenWebhookEventParser;
import com.paymob.sdk.webhook.parser.SubscriptionWebhookEventParser;
import com.paymob.sdk.webhook.parser.TransactionWebhookEventParser;
import com.paymob.sdk.webhook.signature.CardTokenSignatureCalculator;
import com.paymob.sdk.webhook.signature.SubscriptionSignatureCalculator;
import com.paymob.sdk.webhook.signature.TransactionSignatureCalculator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.Map;

/**
 * Validates Paymob webhook signatures using HMAC-SHA512.
 * <p>
 * Supports multiple webhook types (transaction, subscription, card token, etc.)
 * by delegating
 * to registered {@link WebhookSignatureCalculator}s and
 * {@link WebhookEventParser}s.
 * <p>
 * To add a new webhook type:
 * <ol>
 * <li>Create a {@link WebhookSignatureCalculator} subclass</li>
 * <li>Create a {@link WebhookEventParser} implementation</li>
 * <li>Register them in this class's constructor</li>
 * </ol>
 */
public class WebhookValidator {
    private final String hmacSecret;
    private final List<WebhookSignatureCalculator> calculators;
    private final List<WebhookEventParser> parsers;

    public WebhookValidator(String hmacSecret) {
        if (hmacSecret == null || hmacSecret.isEmpty()) {
            throw new IllegalArgumentException("HMAC secret is required for webhook validation");
        }
        this.hmacSecret = hmacSecret;

        // Register built-in calculators and parsers
        this.calculators = new ArrayList<>();
        this.calculators.add(new TransactionSignatureCalculator());
        this.calculators.add(new SubscriptionSignatureCalculator());
        this.calculators.add(new CardTokenSignatureCalculator());

        this.parsers = new ArrayList<>();
        this.parsers.add(new TransactionWebhookEventParser());
        this.parsers.add(new SubscriptionWebhookEventParser());
        this.parsers.add(new CardTokenWebhookEventParser());
    }

    /**
     * Validates the webhook signature by auto-detecting the webhook type.
     *
     * @param payload      The raw request body (JSON)
     * @param receivedHmac The HMAC received with the webhook
     * @return true if signature is valid
     */
    public boolean validateSignature(String payload, String receivedHmac) {
        if (payload == null || receivedHmac == null) {
            return false;
        }

        try {
            for (WebhookSignatureCalculator calculator : calculators) {
                if (calculator.canHandle(payload)) {
                    String expectedHmac = calculator.calculateExpectedHmac(payload, hmacSecret);
                    return timingSafeHexEquals(expectedHmac, receivedHmac);
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Validates webhook signature and parses the event.
     *
     * @param payload      The raw request body
     * @param receivedHmac The HMAC received with the webhook
     * @return Parsed webhook event if valid, null otherwise
     */
    public WebhookEvent validateAndParse(String payload, String receivedHmac) {
        if (!validateSignature(payload, receivedHmac)) {
            return null;
        }

        for (WebhookEventParser parser : parsers) {
            if (parser.canParse(payload)) {
                return parser.parse(payload);
            }
        }

        // Fallback: valid signature but no parser matched
        return new WebhookEvent(payload);
    }

    /**
     * Validates GET callback (user redirect) using query parameters.
     * This is always a transaction callback — no strategy dispatch needed.
     *
     * @param queryParams  Map of query parameters from callback URL
     * @param receivedHmac The HMAC from query parameter 'hmac'
     * @return true if signature is valid
     */
    public boolean validateCallbackSignature(Map<String, String> queryParams, String receivedHmac) {
        if (queryParams == null || receivedHmac == null) {
            return false;
        }

        try {
            String expectedHmac = calculateCallbackHmac(queryParams);
            if (expectedHmac == null) {
                return false;
            }
            return timingSafeHexEquals(expectedHmac, receivedHmac);
        } catch (Exception e) {
            return false;
        }
    }

    private String calculateCallbackHmac(Map<String, String> queryParams) throws Exception {
        if (queryParams.containsKey("token") && queryParams.containsKey("masked_pan")) {
            return calculateCardTokenCallbackHmac(queryParams);
        } else if (queryParams.containsKey("subscription_data.id") || queryParams.containsKey("trigger_type")) {
            return calculateSubscriptionCallbackHmac(queryParams);
        } else {
            return calculateTransactionCallbackHmac(queryParams);
        }
    }

    private String calculateTransactionCallbackHmac(Map<String, String> queryParams) throws Exception {
        String[] fields = {
                "amount_cents", "created_at", "currency", "error_occured", "has_parent_transaction",
                "id", "integration_id", "is_3d_secure", "is_auth", "is_capture", "is_refunded",
                "is_standalone_payment", "is_voided", "order_id", "owner", "pending",
                "source_data.pan", "source_data.sub_type", "source_data.type", "success"
        };

        StringBuilder concatenated = new StringBuilder();
        for (String field : fields) {
            String value = queryParams.get(field);
            if (value == null && "order_id".equals(field)) {
                value = queryParams.get("order.id");
                if (value == null) {
                    value = queryParams.get("order");
                }
            }
            if (value == null && "id".equals(field)) {
                value = queryParams.get("obj.id");
            }
            if (value == null) {
                value = "";
            }
            concatenated.append(value);
        }
        return computeHmac(concatenated.toString());
    }

    private String calculateCardTokenCallbackHmac(Map<String, String> queryParams) throws Exception {
        String[] fields = {
                "card_subtype", "created_at", "email", "id", "masked_pan", "merchant_id", "order_id", "token"
        };

        StringBuilder concatenated = new StringBuilder();
        for (String field : fields) {
            String value = queryParams.get(field);
            if (value == null) {
                value = "";
            }
            concatenated.append(value);
        }
        return computeHmac(concatenated.toString());
    }

    private String calculateSubscriptionCallbackHmac(Map<String, String> queryParams) throws Exception {
        String triggerType = queryParams.getOrDefault("trigger_type", "");
        String subId = queryParams.get("subscription_data.id");
        if (subId == null) {
            subId = queryParams.get("subscription_id");
            if (subId == null) {
                subId = queryParams.get("id");
            }
        }
        if (subId == null)
            subId = "";

        // Matching POST logic: {trigger_type}for{subscription_data.id}
        String concatenated = triggerType + "for" + subId;
        return computeHmac(concatenated);
    }

    private String computeHmac(String concatenated) throws Exception {
        Mac sha512Hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(
                hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        sha512Hmac.init(keySpec);
        byte[] macData = sha512Hmac.doFinal(
                concatenated.getBytes(StandardCharsets.UTF_8));

        return HexFormat.of().formatHex(macData).toLowerCase();
    }

    private boolean timingSafeHexEquals(String expectedHex, String receivedHex) {
        if (expectedHex == null || receivedHex == null) {
            return false;
        }

        try {
            byte[] expectedBytes = HexFormat.of().parseHex(expectedHex);
            byte[] receivedBytes = HexFormat.of().parseHex(receivedHex.toLowerCase());
            return MessageDigest.isEqual(expectedBytes, receivedBytes);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
