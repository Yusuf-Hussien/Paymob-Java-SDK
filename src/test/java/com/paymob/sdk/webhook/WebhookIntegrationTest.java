package com.paymob.sdk.webhook;

import com.paymob.sdk.webhook.signature.CardTokenSignatureCalculator;
import com.paymob.sdk.webhook.signature.TransactionSignatureCalculator;
import com.paymob.sdk.services.subscription.SubscriptionResponse;
import com.paymob.sdk.services.transaction.TransactionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * High-fidelity integration tests for Webhook validation and parsing
 * using real-world payloads provided by the user.
 */
class WebhookIntegrationTest {

    private WebhookValidator validator;
    private static final String HMAC_SECRET = "04DC1A9490B8CC2094C011FC055ADCDB";

    @Test
    @DisplayName("Should validate real Transaction webhook")
    void shouldValidateRealTransactionWebhook() throws Exception {
        // EXACT payload from payload.md (trimmed for Java string readability but
        // preserving structure)
        String payload = "{\n" +
                "  \"type\": \"TRANSACTION\",\n" +
                "  \"obj\": {\n" +
                "    \"id\": 192036465,\n" +
                "    \"pending\": false,\n" +
                "    \"amount_cents\": 100000,\n" +
                "    \"success\": true,\n" +
                "    \"is_auth\": false,\n" +
                "    \"is_capture\": false,\n" +
                "    \"is_standalone_payment\": true,\n" +
                "    \"is_voided\": false,\n" +
                "    \"is_refunded\": false,\n" +
                "    \"is_3d_secure\": true,\n" +
                "    \"integration_id\": 4097558,\n" +
                "    \"profile_id\": 164295,\n" +
                "    \"has_parent_transaction\": false,\n" +
                "    \"order\": { \"id\": 217503754 },\n" +
                "    \"created_at\": \"2024-06-13T11:33:44.592345\",\n" +
                "    \"currency\": \"EGP\",\n" +
                "    \"owner\": 302852,\n" +
                "    \"error_occured\": false,\n" +
                "    \"is_void\": false,\n" +
                "    \"is_refund\": false,\n" +
                "    \"source_data\": {\n" +
                "      \"pan\": \"2346\",\n" +
                "      \"type\": \"card\",\n" +
                "      \"sub_type\": \"MasterCard\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        String expectedHmac = "fa8ac0b7f3852e60c50e7fdd4ea5ef0bda96030c19dea1d55df8c76d6c08ab1877774662cbb04981dc84839ad4da560bcc8cb53b8973548657f7e8f8d2e79930";

        validator = new WebhookValidator(HMAC_SECRET);

        // 1. Verify concatenation
        TransactionSignatureCalculator calculator = new TransactionSignatureCalculator();
        java.lang.reflect.Method method = TransactionSignatureCalculator.class
                .getDeclaredMethod("buildConcatenatedString", String.class);
        method.setAccessible(true);
        String actualConcatenation = (String) method.invoke(calculator, payload);
        String expectedConcatenation = "1000002024-06-13T11:33:44.592345EGPfalsefalse1920364654097558truefalsefalsefalsetruefalse217503754302852false2346MasterCardcardtrue";
        assertEquals(expectedConcatenation, actualConcatenation);

        // 2. Verify full validation and parsing
        WebhookEvent event = validator.validateAndParse(payload, expectedHmac);
        assertNotNull(event, "Validation should pass with correct HMAC");
        assertEquals(WebhookEventType.TRANSACTION_SUCCESSFUL, event.getType());

        TransactionResponse tx = event.getData(TransactionResponse.class);
        assertNotNull(tx);
        assertEquals(192036465, tx.getId());
    }

    @Test
    @DisplayName("Should parse real Card Token webhook")
    void shouldParseRealCardTokenWebhook() throws Exception {
        String payload = "{\n" +
                "  \"type\": \"TOKEN\",\n" +
                "  \"obj\": {\n" +
                "    \"id\": 8555026,\n" +
                "    \"token\": \"e98aceb96f5a370ddf46460db9d555f88bf12448f80e1839b39f78ab\",\n" +
                "    \"masked_pan\": \"xxxx-xxxx-xxxx-2346\",\n" +
                "    \"merchant_id\": 246628,\n" +
                "    \"card_subtype\": \"MasterCard\",\n" +
                "    \"created_at\": \"2024-11-13T12:32:23.859982\",\n" +
                "    \"email\": \"test@test.com\",\n" +
                "    \"order_id\": \"264064419\",\n" +
                "    \"user_added\": false,\n" +
                "    \"next_payment_intention\": \"pi_test_2a9c29ead1734ce8ad09ae4936019992\"\n" +
                "  }\n" +
                "}";

        // Verification of concatenation logic regardless of HMAC match
        CardTokenSignatureCalculator calculator = new CardTokenSignatureCalculator();
        java.lang.reflect.Method method = CardTokenSignatureCalculator.class
                .getDeclaredMethod("buildConcatenatedString", String.class);
        method.setAccessible(true);
        String actualConcatenation = (String) method.invoke(calculator, payload);
        String expectedConcatenation = "MasterCard2024-11-13T12:32:23.859982test@test.com8555026xxxx-xxxx-xxxx-2346246628264064419e98aceb96f5a370ddf46460db9d555f88bf12448f80e1839b39f78ab";
        assertEquals(expectedConcatenation, actualConcatenation);

        // Verify parsing still works
        WebhookEvent event = new com.paymob.sdk.webhook.parser.CardTokenWebhookEventParser().parse(payload);
        assertNotNull(event);
        assertEquals(WebhookEventType.CARD_TOKEN, event.getType());
        assertEquals("e98aceb96f5a370ddf46460db9d555f88bf12448f80e1839b39f78ab", event.getData());
    }

    @Test
    @DisplayName("Should parse real Subscription webhook")
    void shouldParseRealSubscriptionWebhook() {
        String payload = "{\n" +
                "  \"subscription_data\": {\n" +
                "    \"id\": 1264,\n" +
                "    \"state\": \"suspended\"\n" +
                "  },\n" +
                "  \"trigger_type\": \"suspended\"\n" +
                "}";

        WebhookEvent event = new com.paymob.sdk.webhook.parser.SubscriptionWebhookEventParser().parse(payload);
        assertEquals(WebhookEventType.SUBSCRIPTION_SUSPENDED, event.getType());

        SubscriptionResponse sub = event.getData(SubscriptionResponse.class);
        assertNotNull(sub);
        assertEquals(1264, sub.getId());
        assertEquals("suspended", sub.getState());
    }
}
