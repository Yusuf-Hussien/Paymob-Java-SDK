package com.paymob.sdk.models.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymob.sdk.models.enums.Currency;
import com.paymob.sdk.services.intention.IntentionRequest;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class DeepSerializationTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void intentionRequest_serialization_containsNestedStructures() throws Exception {
        IntentionRequest request = IntentionRequest.builder()
                .amount(1000)
                .currency(Currency.EGP)
                .paymentMethods(Collections.singletonList(123))
                .billingData(BillingData.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@doe.com")
                        .phoneNumber("+123")
                        .build())
                .items(Collections.singletonList(new Item("Item 1", 1000, "Desc", 1)))
                .notificationUrl("https://notify.com")
                .build();

        String json = mapper.writeValueAsString(request);

        // Verify snake_case for all levels
        assertTrue(json.contains("\"payment_methods\":[123]"));
        assertTrue(json.contains("\"billing_data\":{"));
        assertTrue(json.contains("\"first_name\":\"John\""));
        assertTrue(json.contains("\"notification_url\":\"https://notify.com\""));
    }

    @Test
    void item_serialization_isCorrect() throws Exception {
        Item item = new Item("Widget", 500, "Extra info", 2);
        String json = mapper.writeValueAsString(item);

        assertTrue(json.contains("\"name\":\"Widget\""));
        assertTrue(json.contains("\"amount\":500"));
        assertTrue(json.contains("\"description\":\"Extra info\""));
        assertTrue(json.contains("\"quantity\":2"));
    }
}
