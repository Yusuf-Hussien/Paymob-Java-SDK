package com.paymob.sdk.webhook;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

/**
 * Template Method base class for webhook HMAC signature calculation.
 * <p>
 * Subclasses define how to build the concatenated string for their webhook
 * type.
 * The HMAC-SHA512 hashing is shared and final — cannot be overridden.
 * <p>
 * To support a new webhook type, extend this class and implement:
 * <ul>
 * <li>{@link #canHandle(String)} — detect the payload shape</li>
 * <li>{@link #buildConcatenatedString(String)} — define the concatenation
 * formula</li>
 * </ul>
 */
public abstract class WebhookSignatureCalculator {
    protected final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Returns true if this calculator can handle the given payload shape.
     */
    public abstract boolean canHandle(String payload);

    /**
     * Builds the concatenated string specific to this webhook type.
     * This is the only part that varies between webhook types.
     */
    protected abstract String buildConcatenatedString(String payload) throws Exception;

    /**
     * Template method — calculates HMAC-SHA512 using the subclass's concatenation
     * formula.
     * This method is final to ensure the hashing algorithm is consistent.
     */
    public final String calculateExpectedHmac(String payload, String hmacSecret) throws Exception {
        String concatenated = buildConcatenatedString(payload);

        Mac sha512Hmac = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(
                hmacSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        sha512Hmac.init(keySpec);
        byte[] macData = sha512Hmac.doFinal(concatenated.getBytes(StandardCharsets.UTF_8));

        return HexFormat.of().formatHex(macData).toLowerCase();
    }
}
