package com.paymob.sdk.webhook.signature;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class TransactionSignatureCalculatorTest {

    private final TransactionSignatureCalculator calculator = new TransactionSignatureCalculator();

    // ── canHandle ─────────────────────────────────────────────────────────
    @Test
    @DisplayName("canHandle returns true for TRANSACTION payload")
    void canHandle_transactionPayload_returnsTrue() {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"id\":1}}";
        assertTrue(calculator.canHandle(payload));
    }

    @Test
    @DisplayName("canHandle returns true for legacy payload without type")
    void canHandle_noType_returnsTrue() {
        String payload = "{\"obj\":{\"id\":1}}";
        assertTrue(calculator.canHandle(payload));
    }

    @Test
    @DisplayName("canHandle returns false for TOKEN type")
    void canHandle_tokenType_returnsFalse() {
        String payload = "{\"type\":\"TOKEN\",\"obj\":{\"id\":1}}";
        assertFalse(calculator.canHandle(payload));
    }

    @Test
    @DisplayName("canHandle returns false for subscription payload")
    void canHandle_subscriptionPayload_returnsFalse() {
        String payload = "{\"subscription_data\":{\"id\":1},\"obj\":{}}";
        assertFalse(calculator.canHandle(payload));
    }

    @Test
    @DisplayName("canHandle returns false for null")
    void canHandle_null_returnsFalse() {
        assertFalse(calculator.canHandle(null));
    }

    @Test
    @DisplayName("canHandle returns false for invalid JSON")
    void canHandle_invalidJson_returnsFalse() {
        assertFalse(calculator.canHandle("not json"));
    }

    // ── HMAC calculation ──────────────────────────────────────────────────
    @Test
    @DisplayName("calculateExpectedHmac produces deterministic result")
    void calculateExpectedHmac_sameInput_sameOutput() throws Exception {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"id\":123,\"success\":true,\"amount_cents\":1000,\"currency\":\"EGP\",\"created_at\":\"2024-01-01\"}}";
        String secret = "test_secret";

        String hmac1 = calculator.calculateExpectedHmac(payload, secret);
        String hmac2 = calculator.calculateExpectedHmac(payload, secret);

        assertNotNull(hmac1);
        assertFalse(hmac1.isEmpty());
        assertEquals(hmac1, hmac2);
    }

    @Test
    @DisplayName("Different secret produces different HMAC")
    void calculateExpectedHmac_differentSecret_differentResult() throws Exception {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"id\":123,\"success\":true}}";

        String hmac1 = calculator.calculateExpectedHmac(payload, "secret1");
        String hmac2 = calculator.calculateExpectedHmac(payload, "secret2");

        assertNotEquals(hmac1, hmac2);
    }
}
