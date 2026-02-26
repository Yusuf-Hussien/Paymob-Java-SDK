package com.paymob.sdk.webhook.parser;

import com.paymob.sdk.webhook.WebhookEvent;
import com.paymob.sdk.webhook.WebhookEventType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class CardTokenWebhookEventParserTest {

    private final CardTokenWebhookEventParser parser = new CardTokenWebhookEventParser();

    @Test
    @DisplayName("canParse returns true for TOKEN payload")
    void canParse_token_returnsTrue() {
        assertTrue(parser.canParse("{\"type\":\"TOKEN\",\"obj\":{\"id\":1}}"));
    }

    @Test
    @DisplayName("canParse returns false for TRANSACTION payload")
    void canParse_transaction_returnsFalse() {
        assertFalse(parser.canParse("{\"type\":\"TRANSACTION\",\"obj\":{\"id\":1}}"));
    }

    @Test
    @DisplayName("canParse returns false for null")
    void canParse_null_returnsFalse() {
        assertFalse(parser.canParse(null));
    }

    @Test
    @DisplayName("parse extracts token string as event data")
    void parse_extractsToken() {
        String payload = "{\"type\":\"TOKEN\",\"obj\":{\"id\":789,\"token\":\"tok_abc_123\"}}";

        WebhookEvent event = parser.parse(payload);

        assertEquals(WebhookEventType.CARD_TOKEN, event.getType());
        assertTrue(event.getSuccess());
        assertEquals("tok_abc_123", event.getData());
    }

    @Test
    @DisplayName("parse falls back to obj when token field is missing")
    void parse_noTokenField_fallsBackToObj() {
        String payload = "{\"type\":\"TOKEN\",\"obj\":{\"id\":789}}";

        WebhookEvent event = parser.parse(payload);

        assertEquals(WebhookEventType.CARD_TOKEN, event.getType());
        assertNotNull(event.getData());
    }

    @Test
    @DisplayName("parse: missing obj returns event without type")
    void parse_missingObj() {
        String payload = "{\"type\":\"TOKEN\"}";

        WebhookEvent event = parser.parse(payload);

        assertNotNull(event);
        assertNull(event.getType());
    }
}
