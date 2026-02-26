package com.paymob.sdk.webhook.parser;

import com.paymob.sdk.webhook.WebhookEvent;
import com.paymob.sdk.webhook.WebhookEventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class TransactionWebhookEventParserTest {

    private final TransactionWebhookEventParser parser = new TransactionWebhookEventParser();

    // ── canParse ──────────────────────────────────────────────────────────
    @Test
    @DisplayName("canParse returns true for TRANSACTION payload")
    void canParse_transaction_returnsTrue() {
        assertTrue(parser.canParse("{\"type\":\"TRANSACTION\",\"obj\":{\"id\":1}}"));
    }

    @Test
    @DisplayName("canParse returns false for TOKEN payload")
    void canParse_token_returnsFalse() {
        assertFalse(parser.canParse("{\"type\":\"TOKEN\",\"obj\":{\"id\":1}}"));
    }

    @Test
    @DisplayName("canParse returns false for null")
    void canParse_null_returnsFalse() {
        assertFalse(parser.canParse(null));
    }

    // ── parse ─────────────────────────────────────────────────────────────
    @Test
    @DisplayName("parse: successful transaction → TRANSACTION_SUCCESSFUL")
    void parse_successful_transaction() {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"id\":1,\"success\":true,\"is_refunded\":false,\"is_voided\":false}}";

        WebhookEvent event = parser.parse(payload);

        assertNotNull(event);
        assertEquals(WebhookEventType.TRANSACTION_SUCCESSFUL, event.getType());
        assertTrue(event.getSuccess());
    }

    @Test
    @DisplayName("parse: failed transaction → TRANSACTION_FAILED")
    void parse_failed_transaction() {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"id\":2,\"success\":false,\"is_refunded\":false,\"is_voided\":false}}";

        WebhookEvent event = parser.parse(payload);

        assertEquals(WebhookEventType.TRANSACTION_FAILED, event.getType());
        assertFalse(event.getSuccess());
    }

    @Test
    @DisplayName("parse: refunded transaction → TRANSACTION_REFUNDED")
    void parse_refunded_transaction() {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"id\":3,\"success\":true,\"is_refunded\":true,\"is_voided\":false}}";

        WebhookEvent event = parser.parse(payload);

        assertEquals(WebhookEventType.TRANSACTION_REFUNDED, event.getType());
    }

    @Test
    @DisplayName("parse: voided transaction → TRANSACTION_VOIDED")
    void parse_voided_transaction() {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"id\":4,\"success\":true,\"is_refunded\":false,\"is_voided\":true}}";

        WebhookEvent event = parser.parse(payload);

        assertEquals(WebhookEventType.TRANSACTION_VOIDED, event.getType());
    }

    @Test
    @DisplayName("parse: missing obj returns event without type")
    void parse_missingObj() {
        String payload = "{\"type\":\"TRANSACTION\"}";

        WebhookEvent event = parser.parse(payload);

        assertNotNull(event);
        assertNull(event.getType());
    }
}
