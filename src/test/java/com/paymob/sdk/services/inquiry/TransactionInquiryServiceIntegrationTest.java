package com.paymob.sdk.services.inquiry;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.exceptions.ResourceNotFoundException;
import com.paymob.sdk.models.common.BillingData;
import com.paymob.sdk.models.common.Item;
import com.paymob.sdk.models.enums.Currency;
import com.paymob.sdk.services.intention.IntentionRequest;
import com.paymob.sdk.services.intention.IntentionResponse;
import com.paymob.sdk.utils.TestConfigUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class TransactionInquiryServiceIntegrationTest {
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        client = TestConfigUtils.createClientFromEnv();
    }

    @Test
    void testInquiryByOrderIdNotFound() {
        // 1. Create an intention to get a real order ID
        IntentionResponse intention = createTestIntention();
        long orderId = intention.getIntentionOrderId();

        // 2. Inquire by Order ID
        // Since the order is freshly created and not paid, Paymob returns 404
        // "Transaction Not Found"
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            client.inquiry().byOrderId(orderId);
        });

        // 3. Assert
        assertNotNull(exception.getErrorBody());
        assertTrue(exception.getErrorBody().contains("Transaction Not Found")
                || exception.getErrorBody().contains("not_found"));
    }

    @Test
    void testInquiryByMerchantOrderIdNotFound() {
        // 1. Create an intention with a custom merchant order ID (special_reference)
        String merchantOrderId = "MST-" + UUID.randomUUID().toString().substring(0, 8);
        createTestIntention(merchantOrderId);

        // 2. Inquire by Merchant Order ID
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            client.inquiry().byMerchantOrderId(merchantOrderId);
        });

        // 3. Assert
        assertNotNull(exception.getErrorBody());
        assertTrue(exception.getErrorBody().contains("Transaction Not Found")
                || exception.getErrorBody().contains("not_found"));
    }

    private IntentionResponse createTestIntention() {
        return createTestIntention(null);
    }

    private IntentionResponse createTestIntention(String merchantOrderId) {
        Item item = Item.builder()
                .name("Inquiry Test Item")
                .amount(1000)
                .quantity(1)
                .build();

        IntentionRequest.Builder builder = IntentionRequest.builder()
                .amount(1000)
                .currency(Currency.EGP)
                .items(Collections.singletonList(item))
                .billingData(createBillingData())
                .paymentMethods(Collections.singletonList(TestConfigUtils.getIntegrationId()));

        if (merchantOrderId != null) {
            builder.specialReference(merchantOrderId);
        }

        return client.intentions().createIntention(builder.build());
    }

    private BillingData createBillingData() {
        return BillingData.builder()
                .firstName("Inquiry")
                .lastName("Tester")
                .email("inquiry@example.com")
                .phoneNumber("01012345678")
                .country("EGY")
                .city("Cairo")
                .street("Tester St")
                .build();
    }
}
