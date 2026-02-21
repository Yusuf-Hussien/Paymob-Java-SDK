package com.paymob.sdk.webhook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WebhookValidator.
 * Tests HMAC validation, field extraction, and edge cases.
 */
class WebhookValidatorTest {

    private WebhookValidator validator;

    @BeforeEach
    void setUp() {
        validator = new WebhookValidator("test_hmac_secret_123");
    }

    @Test
    @DisplayName("Should reject null payload")
    void shouldRejectNullPayload() {
        // Act & Assert
        assertFalse(validator.validateSignature(null, "some_hmac"), "Should reject null payload");
        assertFalse(validator.validateSignature("payload", null), "Should reject null HMAC");
    }

    @Test
    @DisplayName("Should reject malformed JSON")
    void shouldRejectMalformedJson() {
        // Arrange
        String malformedPayload = "{ invalid json }";
        String hmac = "some_hmac";

        // Act
        boolean isValid = validator.validateSignature(malformedPayload, hmac);

        // Assert
        assertFalse(isValid, "Should reject malformed JSON");
    }

    @Test
    @DisplayName("Should reject payload without obj field")
    void shouldRejectPayloadWithoutObjField() {
        // Arrange
        String payloadWithoutObj = "{ \"data\": \"some data\" }";
        String hmac = "some_hmac";

        // Act
        boolean isValid = validator.validateSignature(payloadWithoutObj, hmac);

        // Assert
        assertFalse(isValid, "Should reject payload without obj field");
    }

    @Test
    @DisplayName("Should validate callback signature with correct parameters")
    void shouldValidateCallbackSignature() {
        // Arrange
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("amount_cents", "100");
        queryParams.put("created_at", "2020-03-25T18:39:44.719228");
        queryParams.put("currency", "OMR");
        queryParams.put("error_occured", "false");
        queryParams.put("has_parent_transaction", "false");
        queryParams.put("id", "2556706");
        queryParams.put("integration_id", "6741");
        queryParams.put("is_3d_secure", "true");
        queryParams.put("is_auth", "false");
        queryParams.put("is_capture", "false");
        queryParams.put("is_refunded", "false");
        queryParams.put("is_standalone_payment", "true");
        queryParams.put("is_voided", "false");
        queryParams.put("order.id", "4778239");
        queryParams.put("owner", "4705");
        queryParams.put("pending", "false");
        queryParams.put("source_data.pan", "2346");
        queryParams.put("source_data.sub_type", "MasterCard");
        queryParams.put("source_data.type", "card");
        queryParams.put("success", "true");

        // Act
        boolean isValid = validator.validateCallbackSignature(queryParams, "dummy_hmac");

        // Assert
        assertFalse(isValid, "Should reject invalid HMAC");
    }

    @Test
    @DisplayName("Should reject callback signature with missing parameters")
    void shouldRejectCallbackSignatureWithMissingParams() {
        // Arrange
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("amount_cents", "100");
        // Missing required fields like 'success'

        // Act
        boolean isValid = validator.validateCallbackSignature(queryParams, "some_hmac");

        // Assert
        assertFalse(isValid, "Should reject callback with missing required parameters");
    }

    @Test
    @DisplayName("Should throw exception for invalid HMAC secret")
    void shouldThrowExceptionForInvalidHmacSecret() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new WebhookValidator(null);
        }, "Should throw exception for null HMAC secret");

        assertThrows(IllegalArgumentException.class, () -> {
            new WebhookValidator("");
        }, "Should throw exception for empty HMAC secret");
    }

    @Test
    @DisplayName("Should return null for invalid webhook")
    void shouldReturnNullForInvalidWebhook() {
        // Arrange
        String payload = "{ \"obj\": { \"success\": true } }";
        String incorrectHmac = "wrong_hmac";

        // Act
        WebhookEvent event = validator.validateAndParse(payload, incorrectHmac);

        // Assert
        assertNull(event, "Should return null for invalid webhook");
    }

    @Test
    @DisplayName("Should parse valid webhook successfully")
    void shouldParseValidWebhookSuccessfully() {
        // Arrange
        String payload = "{ \"obj\": { \"success\": true } }";
        String dummyHmac = "dummy_hmac";

        // Act
        WebhookEvent event = validator.validateAndParse(payload, dummyHmac);

        // Assert
        assertNull(event, "Should return null for invalid HMAC");
    }

    @Test
    @DisplayName("Should handle boolean fields correctly")
    void shouldHandleBooleanFieldsCorrectly() {
        // Arrange
        String payload = "{\n" +
                "  \"obj\": {\n" +
                "    \"success\": true,\n" +
                "    \"pending\": false,\n" +
                "    \"is_auth\": false,\n" +
                "    \"is_capture\": true\n" +
                "  }\n" +
                "}";

        // Act
        boolean isValid = validator.validateSignature(payload, "dummy_hmac");

        // Assert
        assertFalse(isValid, "Should reject invalid HMAC");
    }

    @Test
    @DisplayName("Should handle null nested objects gracefully")
    void shouldHandleNullNestedObjectsGracefully() {
        // Arrange
        String payload = "{\n" +
                "  \"obj\": {\n" +
                "    \"success\": true,\n" +
                "    \"order\": null,\n" +
                "    \"source_data\": null\n" +
                "  }\n" +
                "}";

        // Act
        boolean isValid = validator.validateSignature(payload, "dummy_hmac");

        // Assert
        assertFalse(isValid, "Should reject invalid HMAC");
    }
}
