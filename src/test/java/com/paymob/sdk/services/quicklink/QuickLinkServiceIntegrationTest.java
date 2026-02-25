package com.paymob.sdk.services.quicklink;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.models.enums.Currency;
import com.paymob.sdk.utils.TestConfigUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class QuickLinkServiceIntegrationTest {
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        client = TestConfigUtils.createClientFromEnv();
    }

    @Test
    void testCreateAndCancelQuickLinkSuccess() {
        // 1. Prepare request using Builder Pattern
        QuickLinkRequest request = QuickLinkRequest.builder()
                .amountCents(10000) // 100.00 EGP
                .paymentMethods(TestConfigUtils.getIntegrationId())
                .currency(Currency.EGP)
                .isLive(false)
                .fullName("Integration Test User")
                .customerEmail("test@example.com")
                .customerPhone("+201012345678")
                .description("QuickLink Integration Test")
                .paymentLinkImage(new java.io.File("docs/paymob.jpg"))
                .build();

        // 2. Execute Create
        QuickLinkResponse response = client.quickLinks().createPaymentLink(request);

        // System.out.println("client URL: " + response.getClientUrl());

        // 3. Assert Create
        assertNotNull(response);
        assertTrue(response.getId() > 0 || response.getClientUrl() != null, "QuickLink creation failed");
        assertNotNull(response.getClientUrl());
        // 4. Execute Cancel
        QuickLinkResponse cancelResponse = client.quickLinks().cancel(response);

        // 5. Assert Cancel
        assertNotNull(cancelResponse);
        assertTrue(cancelResponse.isSuccess() || "Canceled by merchant".equals(cancelResponse.getMessage()),
                "QuickLink cancellation failed: " + cancelResponse.getMessage());
    }
}
