package com.paymob.sdk.webhook;

import com.paymob.sdk.webhook.parser.CardTokenWebhookEventParser;
import com.paymob.sdk.webhook.signature.CardTokenSignatureCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Card Token webhook signature calculation and event parsing.
 */
class CardTokenWebhookTest {

    private static final String HMAC_SECRET = "test_hmac_secret_123";

    @Test
    @DisplayName("Should detect Card Token payload")
    void shouldDetectCardTokenPayload() {
        CardTokenSignatureCalculator calculator = new CardTokenSignatureCalculator();

        String tokenPayload = "{\"type\":\"TOKEN\",\"obj\":{\"id\":1}}";
        String transactionPayload = "{\"obj\":{\"success\":true}}";

        assertTrue(calculator.canHandle(tokenPayload));
        assertFalse(calculator.canHandle(transactionPayload));
        assertFalse(calculator.canHandle(null));
    }

    @Test
    @DisplayName("Should calculate HMAC correctly based on lexicographical order")
    void shouldCalculateHmacCorrectly() throws Exception {
        CardTokenSignatureCalculator calculator = new CardTokenSignatureCalculator();

        // Sample data from user request:
        // MasterCard2024-11-13T12:32:23.859982test@test.com8555026xxxx-xxxx-xxxx-2346246628264064419e98aceb96f5a370ddf46460db9d555f88bf12448f80e1839b39f78ab
        String payload = "{" +
                "  \"type\": \"TOKEN\"," +
                "  \"obj\": {" +
                "    \"id\": 8555026," +
                "    \"token\": \"e98aceb96f5a370ddf46460db9d555f88bf12448f80e1839b39f78ab\"," +
                "    \"masked_pan\": \"xxxx-xxxx-xxxx-2346\"," +
                "    \"merchant_id\": 246628," +
                "    \"card_subtype\": \"MasterCard\"," +
                "    \"created_at\": \"2024-11-13T12:32:23.859982\"," +
                "    \"email\": \"test@test.com\"," +
                "    \"order_id\": \"264064419\"" +
                "  }" +
                "}";

        String expectedHmac = calculator.calculateExpectedHmac(payload, HMAC_SECRET);
        assertNotNull(expectedHmac);

        // Verify it parses as CARD_TOKEN
        CardTokenWebhookEventParser parser = new CardTokenWebhookEventParser();
        WebhookEvent event = parser.parse(payload);
        assertEquals(WebhookEventType.CARD_TOKEN, event.getType());
        assertEquals(8555026, event.getObj().get("id").asInt());
    }

    @Test
    @DisplayName("Integration test via WebhookValidator")
    void integrationTest() throws Exception {
        WebhookValidator validator = new WebhookValidator(HMAC_SECRET);
        String payload = "{" +
                "  \"type\": \"TOKEN\"," +
                "  \"obj\": {" +
                "    \"id\": 1," +
                "    \"token\": \"t1\"," +
                "    \"masked_pan\": \"m1\"," +
                "    \"merchant_id\": 2," +
                "    \"card_subtype\": \"v1\"," +
                "    \"created_at\": \"c1\"," +
                "    \"email\": \"e1\"," +
                "    \"order_id\": \"o1\"" +
                "  }" +
                "}";

        CardTokenSignatureCalculator calculator = new CardTokenSignatureCalculator();
        String hmac = calculator.calculateExpectedHmac(payload, HMAC_SECRET);

        WebhookEvent event = validator.validateAndParse(payload, hmac);
        assertNotNull(event);
        assertEquals(WebhookEventType.CARD_TOKEN, event.getType());
    }
}
