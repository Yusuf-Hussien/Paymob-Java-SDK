package com.paymob.sdk.webhook;

import com.paymob.sdk.core.PaymobConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Validates Paymob webhook signatures using HMAC-SHA512.
 * Prevents spoofed callbacks by verifying request authenticity.
 * 
 * Follows Paymob's official HMAC calculation methodology:
 * - Hex (base 16) lowercase encoding
 * - Specific field concatenation order
 * - Query parameter 'hmac' for signature
 */
public class WebhookValidator {
    private final String hmacSecret;
    private final ObjectMapper objectMapper;

    public WebhookValidator(String hmacSecret) {
        if (hmacSecret == null || hmacSecret.isEmpty()) {
            throw new IllegalArgumentException("HMAC secret is required for webhook validation");
        }
        this.hmacSecret = hmacSecret;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Validates the webhook signature using Paymob's official method.
     * @param payload The raw request body (JSON)
     * @param receivedHmac The HMAC from query parameter 'hmac'
     * @return true if signature is valid
     */
    public boolean validateSignature(String payload, String receivedHmac) {
        if (payload == null || receivedHmac == null) {
            return false;
        }

        try {
            String expectedHmac = calculateTransactionHmac(payload);
            return MessageDigest.isEqual(
                expectedHmac.getBytes(StandardCharsets.UTF_8), 
                receivedHmac.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Calculates the HMAC-SHA512 signature for transaction callbacks
     * following Paymob's official field order and hex encoding.
     * @param payload The JSON payload
     * @return Hex-encoded HMAC signature (lowercase)
     */
    private String calculateTransactionHmac(String payload) throws Exception {
        JsonNode rootNode = objectMapper.readTree(payload);
        JsonNode obj = rootNode.get("obj");
        
        if (obj == null) {
            throw new IllegalArgumentException("Invalid payload: missing 'obj' field");
        }

        // Extract fields in Paymob's required order
        List<String> fields = new ArrayList<>();
        
        // 1. amount_cents
        fields.add(getTextValue(obj, "amount_cents"));
        
        // 2. created_at
        fields.add(getTextValue(obj, "created_at"));
        
        // 3. currency
        fields.add(getTextValue(obj, "currency"));
        
        // 4. error_occured
        fields.add(getTextValue(obj, "error_occured"));
        
        // 5. has_parent_transaction
        fields.add(getTextValue(obj, "has_parent_transaction"));
        
        // 6. id (or obj.id)
        fields.add(getTextValue(obj, "id"));
        
        // 7. integration_id
        fields.add(getTextValue(obj, "integration_id"));
        
        // 8. is_3d_secure
        fields.add(getTextValue(obj, "is_3d_secure"));
        
        // 9. is_auth
        fields.add(getTextValue(obj, "is_auth"));
        
        // 10. is_capture
        fields.add(getTextValue(obj, "is_capture"));
        
        // 11. is_refunded
        fields.add(getTextValue(obj, "is_refunded"));
        
        // 12. is_standalone_payment
        fields.add(getTextValue(obj, "is_standalone_payment"));
        
        // 13. is_voided
        fields.add(getTextValue(obj, "is_voided"));
        
        // 14. order.id
        JsonNode order = obj.get("order");
        if (order != null) {
            fields.add(getTextValue(order, "id"));
        } else {
            fields.add("");
        }
        
        // 15. owner
        fields.add(getTextValue(obj, "owner"));
        
        // 16. pending
        fields.add(getTextValue(obj, "pending"));
        
        // 17. source_data.pan
        JsonNode sourceData = obj.get("source_data");
        if (sourceData != null) {
            fields.add(getTextValue(sourceData, "pan"));
        } else {
            fields.add("");
        }
        
        // 18. source_data.sub_type
        if (sourceData != null) {
            fields.add(getTextValue(sourceData, "sub_type"));
        } else {
            fields.add("");
        }
        
        // 19. source_data.type
        if (sourceData != null) {
            fields.add(getTextValue(sourceData, "type"));
        } else {
            fields.add("");
        }
        
        // 20. success
        fields.add(getTextValue(obj, "success"));

        // Concatenate all fields
        String concatenated = String.join("", fields);
        
        // Calculate HMAC-SHA512 and encode as hex (lowercase)
        Mac sha512Hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        sha512Hmac.init(keySpec);
        byte[] macData = sha512Hmac.doFinal(concatenated.getBytes(StandardCharsets.UTF_8));
        
        return HexFormat.of().formatHex(macData).toLowerCase();
    }

    /**
     * Helper method to safely extract text values from JSON nodes.
     */
    private String getTextValue(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        if (field == null) {
            return "";
        }
        if (field.isBoolean()) {
            return field.asBoolean() ? "true" : "false";
        }
        if (field.isNumber()) {
            return field.asText();
        }
        return field.asText();
    }

    /**
     * Validates webhook and parses the event.
     * @param payload The raw request body
     * @param receivedHmac The HMAC from query parameter 'hmac'
     * @return Parsed webhook event if valid, null otherwise
     */
    public WebhookEvent validateAndParse(String payload, String receivedHmac) {
        if (!validateSignature(payload, receivedHmac)) {
            return null;
        }

        // Parse JSON payload into WebhookEvent
        return new WebhookEvent(payload);
    }

    /**
     * Validates GET callback (user redirect) using query parameters.
     * @param queryParams Map of query parameters from callback URL
     * @param receivedHmac The HMAC from query parameter 'hmac'
     * @return true if signature is valid
     */
    public boolean validateCallbackSignature(java.util.Map<String, String> queryParams, String receivedHmac) {
        if (queryParams == null || receivedHmac == null) {
            return false;
        }

        try {
            String expectedHmac = calculateCallbackHmac(queryParams);
            return MessageDigest.isEqual(
                expectedHmac.getBytes(StandardCharsets.UTF_8), 
                receivedHmac.getBytes(StandardCharsets.UTF_8)
            );
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Calculates HMAC for GET callback using query parameters.
     * @param queryParams Map of query parameters
     * @return Hex-encoded HMAC signature (lowercase)
     */
    private String calculateCallbackHmac(java.util.Map<String, String> queryParams) throws Exception {
        String[] fields = {
            "amount_cents", "created_at", "currency", "error_occured", "has_parent_transaction",
            "id", "integration_id", "is_3d_secure", "is_auth", "is_capture", "is_refunded",
            "is_standalone_payment", "is_voided", "order", "owner", "pending",
            "source_data.pan", "source_data.sub_type", "source_data.type", "success"
        };

        StringBuilder concatenated = new StringBuilder();
        for (String field : fields) {
            String value = queryParams.getOrDefault(field, "");
            concatenated.append(value);
        }

        // Calculate HMAC-SHA512 and encode as hex (lowercase)
        Mac sha512Hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        sha512Hmac.init(keySpec);
        byte[] macData = sha512Hmac.doFinal(concatenated.toString().getBytes(StandardCharsets.UTF_8));
        
        return HexFormat.of().formatHex(macData).toLowerCase();
    }
}
