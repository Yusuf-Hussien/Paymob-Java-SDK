package com.paymob.sdk.webhook;

import com.paymob.sdk.webhook.parser.TransactionWebhookEventParser;
import com.paymob.sdk.webhook.signature.TransactionSignatureCalculator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WebhookEventTest {

    @Test
    void parsesTransactionSuccess() {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"success\":true,\"is_refunded\":false,\"is_voided\":false}}";
        TransactionWebhookEventParser parser = new TransactionWebhookEventParser();
        assertTrue(parser.canParse(payload));

        WebhookEvent event = parser.parse(payload);
        assertEquals(WebhookEventType.TRANSACTION_SUCCESSFUL, event.getType());
        assertEquals(Boolean.TRUE, event.getSuccess());
        assertNotNull(event.getObj());
        assertNotNull(event.getData());
    }

    @Test
    void parsesTransactionFailed() {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"success\":false,\"is_refunded\":false,\"is_voided\":false}}";
        WebhookEvent event = new TransactionWebhookEventParser().parse(payload);

        assertEquals(WebhookEventType.TRANSACTION_FAILED, event.getType());
        assertEquals(Boolean.FALSE, event.getSuccess());
    }

    @Test
    void parsesTransactionRefunded() {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"success\":true,\"is_refunded\":true,\"is_voided\":false}}";
        WebhookEvent event = new TransactionWebhookEventParser().parse(payload);

        assertEquals(WebhookEventType.TRANSACTION_REFUNDED, event.getType());
        assertEquals(Boolean.TRUE, event.getSuccess());
    }

    @Test
    void parsesTransactionVoided() {
        String payload = "{\"type\":\"TRANSACTION\",\"obj\":{\"success\":true,\"is_refunded\":false,\"is_voided\":true}}";
        WebhookEvent event = new TransactionWebhookEventParser().parse(payload);

        assertEquals(WebhookEventType.TRANSACTION_VOIDED, event.getType());
        assertEquals(Boolean.TRUE, event.getSuccess());
    }

    @Test
    void handlesMissingObj() {
        String payload = "{\"type\":\"TRANSACTION\"}";
        WebhookEvent event = new TransactionWebhookEventParser().parse(payload);

        assertNull(event.getType());
        assertNull(event.getSuccess());
        assertNull(event.getObj());
        assertNull(event.getData());
    }
}
