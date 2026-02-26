package com.paymob.sdk.integration.services;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.models.enums.Currency;
import com.paymob.sdk.services.quicklink.QuickLinkRequest;
import com.paymob.sdk.services.quicklink.QuickLinkResponse;
import com.paymob.sdk.testutil.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class QuickLinkServiceIT {
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        client = IntegrationTestConfig.createClientFromEnv();
    }

    @Test
    void testCreateAndCancelQuickLinkSuccess() {
        QuickLinkRequest request = QuickLinkRequest.builder()
                .amountCents(10000)
                .paymentMethods(IntegrationTestConfig.getIntegrationId())
                .currency(Currency.EGP)
                .isLive(false)
                .fullName("Integration Test User")
                .customerEmail("test@example.com")
                .customerPhone("+201012345678")
                .description("QuickLink Integration Test")
                .paymentLinkImage(new java.io.File("docs/paymob.jpg"))
                .build();

        QuickLinkResponse response = client.quickLinks().createPaymentLink(request);

        assertNotNull(response);
        assertTrue(response.getId() > 0 || response.getClientUrl() != null, "QuickLink creation failed");
        assertNotNull(response.getClientUrl());

        QuickLinkResponse cancelResponse = client.quickLinks().cancel(response);

        assertNotNull(cancelResponse);
        assertTrue(cancelResponse.isSuccess() || "Canceled by merchant".equals(cancelResponse.getMessage()),
                "QuickLink cancellation failed: " + cancelResponse.getMessage());
    }
}
