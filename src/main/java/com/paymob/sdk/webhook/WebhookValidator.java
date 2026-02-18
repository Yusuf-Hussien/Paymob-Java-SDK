package com.paymob.sdk.webhook;

import com.paymob.sdk.core.PaymobConfig;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Validates Paymob webhook signatures using HMAC-SHA512.
 * Prevents spoofed callbacks by verifying request authenticity.
 */
public class WebhookValidator {
    private final String hmacSecret;

    public WebhookValidator(String hmacSecret) {
        if (hmacSecret == null || hmacSecret.isEmpty()) {
            throw new IllegalArgumentException("HMAC secret is required for webhook validation");
        }
        this.hmacSecret = hmacSecret;
    }

    /**
     * Validates the webhook signature.
     * @param payload The raw request body
     * @param receivedSignature The signature from the X-Paymob-Hmac header
     * @return true if signature is valid
     */
    public boolean validateSignature(String payload, String receivedSignature) {
        if (payload == null || receivedSignature == null) {
            return false;
        }

        try {
            String expectedSignature = calculateSignature(payload);
            return expectedSignature.equals(receivedSignature);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Calculates the HMAC-SHA512 signature for the payload.
     * @param payload The payload to sign
     * @return The calculated signature
     */
    private String calculateSignature(String payload) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha512Hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        sha512Hmac.init(keySpec);
        byte[] macData = sha512Hmac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(macData);
    }

    /**
     * Validates webhook and parses the event.
     * @param payload The raw request body
     * @param receivedSignature The signature from the X-Paymob-Hmac header
     * @return Parsed webhook event if valid, null otherwise
     */
    public WebhookEvent validateAndParse(String payload, String receivedSignature) {
        if (!validateSignature(payload, receivedSignature)) {
            return null;
        }

        // TODO: Parse JSON payload into WebhookEvent
        // This would require JSON parsing library integration
        return new WebhookEvent(payload);
    }
}
