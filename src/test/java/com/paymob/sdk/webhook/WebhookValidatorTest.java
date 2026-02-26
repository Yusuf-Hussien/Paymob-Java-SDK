package com.paymob.sdk.webhook;

import com.paymob.sdk.webhook.signature.CardTokenSignatureCalculator;
import com.paymob.sdk.webhook.signature.SubscriptionSignatureCalculator;
import com.paymob.sdk.webhook.signature.TransactionSignatureCalculator;
import com.paymob.sdk.services.subscription.SubscriptionResponse;
import com.paymob.sdk.services.transaction.TransactionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WebhookValidator.
 * Tests HMAC validation, type detection, and parsing for all webhook types.
 */
class WebhookValidatorTest {

    private WebhookValidator validator;
    private static final String HMAC_SECRET = "test_hmac_secret_123";

    @BeforeEach
    void setUp() {
        validator = new WebhookValidator(HMAC_SECRET);
    }

    @Test
    @DisplayName("Should validate and parse Transaction Success")
    void shouldValidateAndParseTransactionSuccess() throws Exception {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"id\":123,\"success\":true,\"amount_cents\":1000,\"currency\":\"EGP\",\"created_at\":\"2024-01-01\"}}";
        String hmac = new TransactionSignatureCalculator().calculateExpectedHmac(payload, HMAC_SECRET);

        WebhookEvent event = validator.validateAndParse(payload, hmac);

        assertNotNull(event);
        assertEquals(WebhookEventType.TRANSACTION_SUCCESSFUL, event.getType());
        assertTrue(event.getSuccess());

        TransactionResponse tx = event.getData(TransactionResponse.class);
        assertNotNull(tx);
        assertEquals(123, tx.getId());
    }

    @Test
    @DisplayName("Should validate and parse Subscription Suspended")
    void shouldValidateAndParseSubscriptionSuspended() throws Exception {
        String payload = "{\"trigger_type\":\"suspended\",\"subscription_data\":{\"id\":456,\"state\":\"suspended\"}}";
        String hmac = new SubscriptionSignatureCalculator().calculateExpectedHmac(payload, HMAC_SECRET);

        WebhookEvent event = validator.validateAndParse(payload, hmac);

        assertNotNull(event);
        assertEquals(WebhookEventType.SUBSCRIPTION_SUSPENDED, event.getType());

        SubscriptionResponse sub = event.getData(SubscriptionResponse.class);
        assertNotNull(sub);
        assertEquals(456, sub.getId());
    }

    @Test
    @DisplayName("Should validate and parse Card Token")
    void shouldValidateAndParseCardToken() throws Exception {
        String payload = "{\"type\":\"TOKEN\",\"obj\":{\"id\":789,\"token\":\"abc_token_123\"}}";
        String hmac = new CardTokenSignatureCalculator().calculateExpectedHmac(payload, HMAC_SECRET);

        WebhookEvent event = validator.validateAndParse(payload, hmac);

        assertNotNull(event);
        assertEquals(WebhookEventType.CARD_TOKEN, event.getType());
        assertEquals("abc_token_123", event.getData());
    }

    @Test
    @DisplayName("Should return null for payload no calculator handles")
    void shouldReturnNullForUnknownStructure() {
        String payload = "{\"completely_unknown\": true}";
        assertNull(validator.validateAndParse(payload, "any_hmac"));
    }

    @Test
    @DisplayName("Should return null for invalid signature even if structure is valid")
    void shouldReturnNullForInvalidSignature() {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"id\":123,\"success\":true}}";
        assertNull(validator.validateAndParse(payload, "wrong_hmac"));
    }
}
