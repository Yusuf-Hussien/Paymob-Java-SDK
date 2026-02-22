package com.paymob.sdk.webhook;

import com.paymob.sdk.webhook.parser.SubscriptionWebhookEventParser;
import com.paymob.sdk.webhook.signature.SubscriptionSignatureCalculator;
import com.paymob.sdk.webhook.signature.TransactionSignatureCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for subscription webhook signature calculation and event parsing.
 */
class SubscriptionWebhookTest {

    private static final String HMAC_SECRET = "test_hmac_secret_123";

    // ── Signature Calculator Tests ──

    @Test
    @DisplayName("Should detect subscription payload")
    void shouldDetectSubscriptionPayload() {
        SubscriptionSignatureCalculator calculator = new SubscriptionSignatureCalculator();

        String subscriptionPayload = "{\"subscription_data\":{\"id\":1264},\"trigger_type\":\"suspended\",\"hmac\":\"abc\"}";
        String transactionPayload = "{\"obj\":{\"success\":true}}";

        assertTrue(calculator.canHandle(subscriptionPayload));
        assertFalse(calculator.canHandle(transactionPayload));
        assertFalse(calculator.canHandle(null));
        assertFalse(calculator.canHandle("invalid json"));
    }

    @Test
    @DisplayName("Same payload should produce same HMAC")
    void shouldProduceConsistentHmac() throws Exception {
        SubscriptionSignatureCalculator calculator = new SubscriptionSignatureCalculator();

        String payload = "{\"subscription_data\":{\"id\":1264},\"trigger_type\":\"suspended\"}";
        String hmac1 = calculator.calculateExpectedHmac(payload, HMAC_SECRET);
        String hmac2 = calculator.calculateExpectedHmac(payload, HMAC_SECRET);
        assertEquals(hmac1, hmac2);
    }

    @Test
    @DisplayName("Different trigger types produce different HMACs")
    void shouldProduceDifferentHmacForDifferentTriggers() throws Exception {
        SubscriptionSignatureCalculator calculator = new SubscriptionSignatureCalculator();

        String suspended = "{\"subscription_data\":{\"id\":1264},\"trigger_type\":\"suspended\"}";
        String canceled = "{\"subscription_data\":{\"id\":1264},\"trigger_type\":\"canceled\"}";

        assertNotEquals(
                calculator.calculateExpectedHmac(suspended, HMAC_SECRET),
                calculator.calculateExpectedHmac(canceled, HMAC_SECRET));
    }

    @Test
    @DisplayName("Different subscription IDs produce different HMACs")
    void shouldProduceDifferentHmacForDifferentIds() throws Exception {
        SubscriptionSignatureCalculator calculator = new SubscriptionSignatureCalculator();

        String id1 = "{\"subscription_data\":{\"id\":1264},\"trigger_type\":\"suspended\"}";
        String id2 = "{\"subscription_data\":{\"id\":9999},\"trigger_type\":\"suspended\"}";

        assertNotEquals(
                calculator.calculateExpectedHmac(id1, HMAC_SECRET),
                calculator.calculateExpectedHmac(id2, HMAC_SECRET));
    }

    @Test
    @DisplayName("Should reject payload missing subscription_data")
    void shouldRejectMissingSubscriptionData() {
        SubscriptionSignatureCalculator calculator = new SubscriptionSignatureCalculator();

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateExpectedHmac(
                "{\"trigger_type\":\"suspended\"}", HMAC_SECRET));
    }

    @Test
    @DisplayName("Should reject payload missing subscription_data.id")
    void shouldRejectMissingId() {
        SubscriptionSignatureCalculator calculator = new SubscriptionSignatureCalculator();

        assertThrows(IllegalArgumentException.class, () -> calculator.calculateExpectedHmac(
                "{\"subscription_data\":{},\"trigger_type\":\"suspended\"}", HMAC_SECRET));
    }

    // ── HMAC Validation via WebhookValidator ──

    @Test
    @DisplayName("Should validate subscription webhook via WebhookValidator")
    void shouldValidateSubscriptionWebhookViaValidator() throws Exception {
        WebhookValidator validator = new WebhookValidator(HMAC_SECRET);
        SubscriptionSignatureCalculator calculator = new SubscriptionSignatureCalculator();

        String payload = "{\"subscription_data\":{\"id\":1264},\"trigger_type\":\"suspended\",\"hmac\":\"abc\"}";
        String expectedHmac = calculator.calculateExpectedHmac(payload, HMAC_SECRET);

        assertTrue(validator.validateSignature(payload, expectedHmac));
        assertFalse(validator.validateSignature(payload, "wrong_hmac"));
    }

    @Test
    @DisplayName("Should validateAndParse subscription webhook")
    void shouldValidateAndParseSubscriptionWebhook() throws Exception {
        WebhookValidator validator = new WebhookValidator(HMAC_SECRET);
        SubscriptionSignatureCalculator calculator = new SubscriptionSignatureCalculator();

        String payload = "{\"subscription_data\":{\"id\":1264,\"state\":\"suspended\"},\"trigger_type\":\"suspended\"}";
        String expectedHmac = calculator.calculateExpectedHmac(payload, HMAC_SECRET);

        WebhookEvent event = validator.validateAndParse(payload, expectedHmac);
        assertNotNull(event);
        assertEquals(WebhookEventType.SUBSCRIPTION_SUSPENDED, event.getType());
        assertNotNull(event.getData());
    }

    // ── Event Parser Detection ──

    @Test
    @DisplayName("Should detect subscription payload for parsing")
    void shouldDetectSubscriptionPayloadForParsing() {
        SubscriptionWebhookEventParser parser = new SubscriptionWebhookEventParser();

        assertTrue(parser.canParse("{\"subscription_data\":{\"id\":1},\"trigger_type\":\"suspended\"}"));
        assertFalse(parser.canParse("{\"obj\":{\"success\":true}}"));
        assertFalse(parser.canParse(null));
    }

    // ── Trigger Type Mappings ──

    @Test
    @DisplayName("Should map all subscription trigger types correctly")
    void shouldMapAllTriggerTypes() {
        SubscriptionWebhookEventParser parser = new SubscriptionWebhookEventParser();

        assertTriggerMapping(parser, "Subscription Created", WebhookEventType.SUBSCRIPTION_CREATED);
        assertTriggerMapping(parser, "suspended", WebhookEventType.SUBSCRIPTION_SUSPENDED);
        assertTriggerMapping(parser, "canceled", WebhookEventType.SUBSCRIPTION_CANCELED);
        assertTriggerMapping(parser, "resumed", WebhookEventType.SUBSCRIPTION_RESUMED);
        assertTriggerMapping(parser, "updated", WebhookEventType.SUBSCRIPTION_UPDATED);
        assertTriggerMapping(parser, "add_secondry_card", WebhookEventType.SUBSCRIPTION_ADD_SECONDARY_CARD);
        assertTriggerMapping(parser, "change_primary_card", WebhookEventType.SUBSCRIPTION_CHANGE_PRIMARY_CARD);
        assertTriggerMapping(parser, "delete_card", WebhookEventType.SUBSCRIPTION_DELETE_CARD);
        assertTriggerMapping(parser, "register_webhook", WebhookEventType.SUBSCRIPTION_REGISTER_WEBHOOK);
        assertTriggerMapping(parser, "Successful Transaction", WebhookEventType.SUBSCRIPTION_SUCCESSFUL_TRANSACTION);
        assertTriggerMapping(parser, "Failed Transaction", WebhookEventType.SUBSCRIPTION_FAILED_TRANSACTION);
        assertTriggerMapping(parser, "Failed Overdue Transaction",
                WebhookEventType.SUBSCRIPTION_FAILED_OVERDUE_TRANSACTION);
    }

    private void assertTriggerMapping(SubscriptionWebhookEventParser parser, String triggerType,
            WebhookEventType expected) {
        String payload = "{\"subscription_data\":{\"id\":1},\"trigger_type\":\"" + triggerType + "\"}";
        WebhookEvent event = parser.parse(payload);
        assertEquals(expected, event.getType(),
                "trigger_type '" + triggerType + "' should map to " + expected);
    }

    // ── Cross-type Detection Guards ──

    @Test
    @DisplayName("Transaction calculator should NOT handle subscription payloads")
    void transactionShouldNotHandleSubscription() {
        TransactionSignatureCalculator txCalc = new TransactionSignatureCalculator();
        assertFalse(txCalc.canHandle("{\"subscription_data\":{\"id\":1},\"trigger_type\":\"suspended\"}"));
    }

    @Test
    @DisplayName("Subscription calculator should NOT handle transaction payloads")
    void subscriptionShouldNotHandleTransaction() {
        SubscriptionSignatureCalculator subCalc = new SubscriptionSignatureCalculator();
        assertFalse(subCalc.canHandle("{\"obj\":{\"success\":true}}"));
    }
}
