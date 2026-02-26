package com.paymob.sdk.integration.services;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.models.common.BillingData;
import com.paymob.sdk.models.common.Item;
import com.paymob.sdk.models.enums.Currency;
import com.paymob.sdk.services.intention.IntentionRequest;
import com.paymob.sdk.services.intention.IntentionResponse;
import com.paymob.sdk.testutil.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class IntentionServiceIT {
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        client = IntegrationTestConfig.createClientFromEnv();
    }

    @Test
    void testCreateIntentionSuccess() {
        IntentionResponse response = createTestIntention(10000);

        assertNotNull(response);
        assertNotNull(response.getClientSecret());
        assertNotNull(response.getId());
        assertNotNull(response.getIntentionOrderId());

        String checkoutUrl = client.intentions().getUnifiedCheckoutUrl(response);
        assertTrue(checkoutUrl.contains(response.getClientSecret()));
        assertTrue(checkoutUrl.contains(client.getConfig().getPublicKey()));
    }

    @Test
    void testUpdateIntentionSuccess() {
        IntentionResponse createResponse = createTestIntention(10000);
        assertNotNull(createResponse.getClientSecret());

        int newAmount = 15000;
        Item newItem = Item.builder()
                .name("Updated Item")
                .amount(newAmount)
                .quantity(1)
                .build();

        IntentionRequest updateRequest = IntentionRequest.builder()
                .amount(newAmount)
                .items(Collections.singletonList(newItem))
                .billingData(createBillingData())
                .acceptOrderId(createResponse.getIntentionOrderId())
                .build();

        IntentionResponse updateResponse = client.intentions().updateIntention(createResponse.getClientSecret(),
                updateRequest);

        assertNotNull(updateResponse);
        assertEquals(newAmount, updateResponse.getAmount());
    }

    @Test
    void testRetrieveIntentionSuccess() {
        IntentionResponse createResponse = createTestIntention(10000);

        IntentionResponse retrievedResponse = client.intentions().retrieveIntention(createResponse.getClientSecret());

        assertNotNull(retrievedResponse);
        assertEquals(createResponse.getId(), retrievedResponse.getId());
        assertEquals(createResponse.getClientSecret(), retrievedResponse.getClientSecret());
        assertEquals(10000, retrievedResponse.getAmount());
    }

    private IntentionResponse createTestIntention(int amount) {
        Item item = Item.builder()
                .name("Integration Test Item")
                .amount(amount)
                .quantity(1)
                .build();

        IntentionRequest request = IntentionRequest.builder()
                .amount(amount)
                .currency(Currency.EGP)
                .items(Collections.singletonList(item))
                .billingData(createBillingData())
                .paymentMethods(Collections.singletonList(IntegrationTestConfig.getIntegrationId()))
                .build();

        return client.intentions().createIntention(request);
    }

    private BillingData createBillingData() {
        return BillingData.builder()
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .phoneNumber("01012345678")
                .country("EGY")
                .city("Cairo")
                .street("Test Street")
                .build();
    }
}
