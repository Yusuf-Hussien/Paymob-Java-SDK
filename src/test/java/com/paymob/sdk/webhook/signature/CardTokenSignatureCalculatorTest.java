package com.paymob.sdk.webhook.signature;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class CardTokenSignatureCalculatorTest {

    private final CardTokenSignatureCalculator calculator = new CardTokenSignatureCalculator();

    @Test
    @DisplayName("canHandle returns true for TOKEN payload")
    void canHandle_tokenPayload_returnsTrue() {
        String payload = "{\"type\":\"TOKEN\",\"obj\":{\"id\":1}}";
        assertTrue(calculator.canHandle(payload));
    }

    @Test
    @DisplayName("canHandle returns false for TRANSACTION type")
    void canHandle_transactionType_returnsFalse() {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"id\":1}}";
        assertFalse(calculator.canHandle(payload));
    }

    @Test
    @DisplayName("canHandle returns false for null")
    void canHandle_null_returnsFalse() {
        assertFalse(calculator.canHandle(null));
    }

    @Test
    @DisplayName("canHandle returns false when no type field")
    void canHandle_noType_returnsFalse() {
        String payload = "{\"obj\":{\"id\":1}}";
        assertFalse(calculator.canHandle(payload));
    }

    @Test
    @DisplayName("calculateExpectedHmac produces deterministic result")
    void calculateExpectedHmac_sameInput_sameOutput() throws Exception {
        String payload = "{\"type\":\"TOKEN\",\"obj\":{\"id\":789,\"token\":\"abc\",\"card_subtype\":\"Visa\",\"created_at\":\"2024-01-01\",\"email\":\"a@b.com\",\"masked_pan\":\"xxxx-1234\",\"merchant_id\":100,\"order_id\":\"200\"}}";
        String secret = "test_secret";

        String hmac1 = calculator.calculateExpectedHmac(payload, secret);
        String hmac2 = calculator.calculateExpectedHmac(payload, secret);

        assertNotNull(hmac1);
        assertEquals(hmac1, hmac2);
    }

    @Test
    @DisplayName("Different secrets produce different HMACs")
    void calculateExpectedHmac_differentSecret_differentResult() throws Exception {
        String payload = "{\"type\":\"TOKEN\",\"obj\":{\"id\":1,\"token\":\"t\"}}";

        assertNotEquals(
                calculator.calculateExpectedHmac(payload, "s1"),
                calculator.calculateExpectedHmac(payload, "s2"));
    }
}
