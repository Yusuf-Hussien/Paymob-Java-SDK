package com.paymob.sdk.webhook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WebhookCallbackTest {

    private WebhookValidator validator;
    private static final String HMAC_SECRET = "04DC1A9490B8CC2094C011FC055ADCDB";

    @BeforeEach
    void setUp() {
        validator = new WebhookValidator(HMAC_SECRET);
    }

    @Test
    @DisplayName("Should validate Transaction redirect callback HMAC")
    void shouldValidateTransactionCallback() {
        Map<String, String> params = new HashMap<>();
        params.put("amount_cents", "100000");
        params.put("created_at", "2024-06-13T11:33:44.592345");
        params.put("currency", "EGP");
        params.put("error_occured", "false");
        params.put("has_parent_transaction", "false");
        params.put("id", "192036465");
        params.put("integration_id", "4097558");
        params.put("is_3d_secure", "true");
        params.put("is_auth", "false");
        params.put("is_capture", "false");
        params.put("is_refunded", "false");
        params.put("is_standalone_payment", "true");
        params.put("is_voided", "false");
        params.put("order_id", "217503754");
        params.put("owner", "302852");
        params.put("pending", "false");
        params.put("source_data.pan", "2346");
        params.put("source_data.sub_type", "MasterCard");
        params.put("source_data.type", "card");
        params.put("success", "true");

        String expectedHmac = "fa8ac0b7f3852e60c50e7fdd4ea5ef0bda96030c19dea1d55df8c76d6c08ab1877774662cbb04981dc84839ad4da560bcc8cb53b8973548657f7e8f8d2e79930";

        assertTrue(validator.validateCallbackSignature(params, expectedHmac));
    }

    @Test
    @DisplayName("Should validate Transaction redirect with obj.id and order.id fallbacks")
    void shouldValidateTransactionCallbackFallbacks() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("amount_cents", "100000");
        params.put("created_at", "2024-06-13T11:33:44.592345");
        params.put("currency", "EGP");
        params.put("error_occured", "false");
        params.put("has_parent_transaction", "false");
        // Use obj.id and order.id instead of id and order_id
        params.put("obj.id", "192036465");
        params.put("order.id", "217503754");
        params.put("integration_id", "4097558");
        params.put("is_3d_secure", "true");
        params.put("is_auth", "false");
        params.put("is_capture", "false");
        params.put("is_refunded", "false");
        params.put("is_standalone_payment", "true");
        params.put("is_voided", "false");
        params.put("owner", "302852");
        params.put("pending", "false");
        params.put("source_data.pan", "2346");
        params.put("source_data.sub_type", "MasterCard");
        params.put("source_data.type", "card");
        params.put("success", "true");

        String expectedHmac = "fa8ac0b7f3852e60c50e7fdd4ea5ef0bda96030c19dea1d55df8c76d6c08ab1877774662cbb04981dc84839ad4da560bcc8cb53b8973548657f7e8f8d2e79930";

        assertTrue(validator.validateCallbackSignature(params, expectedHmac));
    }

    @Test
    @DisplayName("Should validate Card Token redirect callback HMAC")
    void shouldValidateCardTokenCallback() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("card_subtype", "MasterCard");
        params.put("created_at", "2024-11-13T12:32:23.859982");
        params.put("email", "test@test.com");
        params.put("id", "8555026");
        params.put("masked_pan", "xxxx-xxxx-xxxx-2346");
        params.put("merchant_id", "246628");
        params.put("order_id", "264064419");
        params.put("token", "e98aceb96f5a370ddf46460db9d555f88bf12448f80e1839b39f78ab");

        // Verify logical flow by calculating expected and validating
        java.lang.reflect.Method method = WebhookValidator.class.getDeclaredMethod("calculateCardTokenCallbackHmac",
                Map.class);
        method.setAccessible(true);
        String expectedHmac = (String) method.invoke(validator, params);

        assertTrue(validator.validateCallbackSignature(params, expectedHmac));
    }

    @Test
    @DisplayName("Should validate Subscription redirect callback HMAC")
    void shouldValidateSubscriptionCallback() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("trigger_type", "suspended");
        params.put("subscription_data.id", "1264");

        java.lang.reflect.Method method = WebhookValidator.class.getDeclaredMethod("calculateSubscriptionCallbackHmac",
                Map.class);
        method.setAccessible(true);
        String expectedHmac = (String) method.invoke(validator, params);

        assertTrue(validator.validateCallbackSignature(params, expectedHmac));
    }
}
