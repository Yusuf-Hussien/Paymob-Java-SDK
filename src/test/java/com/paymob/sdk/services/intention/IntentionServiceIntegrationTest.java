package com.paymob.sdk.services.intention;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.models.common.BillingData;
import com.paymob.sdk.models.common.Item;
import com.paymob.sdk.models.enums.Currency;
import com.paymob.sdk.utils.TestConfigUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class IntentionServiceIntegrationTest {
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        client = TestConfigUtils.createClientFromEnv();
    }

    @Test
    void testCreateIntentionSuccess() {
        IntentionResponse response = createTestIntention(10000);

        assertNotNull(response);
        assertNotNull(response.getClientSecret());
        assertNotNull(response.getId());
        assertNotNull(response.getIntentionOrderId());

        // Final verify URL generation
        String checkoutUrl = client.intentions().getUnifiedCheckoutUrl(response);
        assertTrue(checkoutUrl.contains(response.getClientSecret()));
        assertTrue(checkoutUrl.contains(client.getConfig().getPublicKey()));
    }

    @Test
    void testUpdateIntentionSuccess() {
        // 1. Create
        IntentionResponse createResponse = createTestIntention(10000);
        assertNotNull(createResponse.getClientSecret());

        // 2. Prepare Update Request
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

        // 3. Execute Update
        IntentionResponse updateResponse = client.intentions().updateIntention(createResponse.getClientSecret(),
                updateRequest);

        // 4. Assert
        assertNotNull(updateResponse);
        assertEquals(newAmount, updateResponse.getAmount());
    }

    @Test
    void testRetrieveIntentionSuccess() {
        // 1. Create
        IntentionResponse createResponse = createTestIntention(10000);

        // 2. Retrieve
        IntentionResponse retrievedResponse = client.intentions().retrieveIntention(createResponse.getClientSecret());

        // 3. Assert
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
                .paymentMethods(Collections.singletonList(TestConfigUtils.getIntegrationId()))
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
