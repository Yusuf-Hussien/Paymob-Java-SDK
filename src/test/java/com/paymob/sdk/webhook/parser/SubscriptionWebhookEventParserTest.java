package com.paymob.sdk.webhook.parser;

import com.paymob.sdk.webhook.WebhookEvent;
import com.paymob.sdk.webhook.WebhookEventType;
import com.paymob.sdk.services.subscription.SubscriptionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionWebhookEventParserTest {

    private final SubscriptionWebhookEventParser parser = new SubscriptionWebhookEventParser();

    @Test
    @DisplayName("canParse returns true for subscription payload")
    void canParse_subscription_returnsTrue() {
        assertTrue(parser.canParse("{\"subscription_data\":{\"id\":1},\"trigger_type\":\"suspended\"}"));
    }

    @Test
    @DisplayName("canParse returns false for TRANSACTION payload")
    void canParse_transaction_returnsFalse() {
        assertFalse(parser.canParse("{\"type\":\"TRANSACTION\",\"obj\":{}}"));
    }

    @Test
    @DisplayName("canParse returns false for null")
    void canParse_null_returnsFalse() {
        assertFalse(parser.canParse(null));
    }

    @Test
    @DisplayName("parse: suspended → SUBSCRIPTION_SUSPENDED")
    void parse_suspended() {
        String payload = "{\"subscription_data\":{\"id\":100,\"state\":\"suspended\"},\"trigger_type\":\"suspended\"}";

        WebhookEvent event = parser.parse(payload);

        assertEquals(WebhookEventType.SUBSCRIPTION_SUSPENDED, event.getType());
        assertTrue(event.getSuccess());

        SubscriptionResponse sub = event.getData(SubscriptionResponse.class);
        assertNotNull(sub);
        assertEquals(100, sub.getId());
        assertEquals("suspended", sub.getState());
    }

    @Test
    @DisplayName("parse: resumed → SUBSCRIPTION_RESUMED")
    void parse_resumed() {
        String payload = "{\"subscription_data\":{\"id\":200,\"state\":\"active\"},\"trigger_type\":\"resumed\"}";

        WebhookEvent event = parser.parse(payload);

        assertEquals(WebhookEventType.SUBSCRIPTION_RESUMED, event.getType());
    }

    @Test
    @DisplayName("parse: canceled → SUBSCRIPTION_CANCELED")
    void parse_canceled() {
        String payload = "{\"subscription_data\":{\"id\":300,\"state\":\"canceled\"},\"trigger_type\":\"canceled\"}";

        WebhookEvent event = parser.parse(payload);

        assertEquals(WebhookEventType.SUBSCRIPTION_CANCELED, event.getType());
    }

    @Test
    @DisplayName("parse: missing subscription_data still returns event")
    void parse_missingData() {
        String payload = "{\"trigger_type\":\"suspended\"}";
        // canParse would return false, but parse shouldn't crash
        WebhookEvent event = parser.parse(payload);
        assertNotNull(event);
    }
}
