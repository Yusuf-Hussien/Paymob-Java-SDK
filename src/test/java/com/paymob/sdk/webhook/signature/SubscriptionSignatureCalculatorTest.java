package com.paymob.sdk.webhook.signature;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionSignatureCalculatorTest {

    private final SubscriptionSignatureCalculator calculator = new SubscriptionSignatureCalculator();

    @Test
    @DisplayName("canHandle returns true for subscription payload")
    void canHandle_subscriptionPayload_returnsTrue() {
        String payload = "{\"subscription_data\":{\"id\":1},\"trigger_type\":\"suspended\"}";
        assertTrue(calculator.canHandle(payload));
    }

    @Test
    @DisplayName("canHandle returns false when missing trigger_type")
    void canHandle_missingTrigger_returnsFalse() {
        String payload = "{\"subscription_data\":{\"id\":1}}";
        assertFalse(calculator.canHandle(payload));
    }

    @Test
    @DisplayName("canHandle returns false when missing subscription_data")
    void canHandle_missingSubscriptionData_returnsFalse() {
        String payload = "{\"trigger_type\":\"suspended\"}";
        assertFalse(calculator.canHandle(payload));
    }

    @Test
    @DisplayName("canHandle returns false for null")
    void canHandle_null_returnsFalse() {
        assertFalse(calculator.canHandle(null));
    }

    @Test
    @DisplayName("calculateExpectedHmac produces deterministic result")
    void calculateExpectedHmac_sameInput_sameOutput() throws Exception {
        String payload = "{\"subscription_data\":{\"id\":456},\"trigger_type\":\"activated\"}";
        String secret = "secret";

        String hmac1 = calculator.calculateExpectedHmac(payload, secret);
        String hmac2 = calculator.calculateExpectedHmac(payload, secret);

        assertNotNull(hmac1);
        assertEquals(hmac1, hmac2);
    }

    @Test
    @DisplayName("Different trigger types produce different HMACs")
    void calculateExpectedHmac_differentTrigger_differentHmac() throws Exception {
        String secret = "secret";
        String payload1 = "{\"subscription_data\":{\"id\":1},\"trigger_type\":\"activated\"}";
        String payload2 = "{\"subscription_data\":{\"id\":1},\"trigger_type\":\"suspended\"}";

        assertNotEquals(
                calculator.calculateExpectedHmac(payload1, secret),
                calculator.calculateExpectedHmac(payload2, secret));
    }
}
