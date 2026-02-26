package com.paymob.sdk.integration.services;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.services.subscription.SubscriptionListRequest;
import com.paymob.sdk.services.subscription.SubscriptionTransactionsPage;
import com.paymob.sdk.services.subscription.SubscriptionsPage;
import com.paymob.sdk.testutil.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class SubscriptionServiceIT {
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        client = IntegrationTestConfig.createClientFromEnv();
    }

    @Test
    void testSubscriptionRetrievalWithInvalidId() {
        long invalidSubscriptionId = 9999999L;
        assertThrows(Exception.class, () -> client.subscriptions().get(invalidSubscriptionId));
    }

    @Test
    void testListSubscriptions() {
        SubscriptionListRequest filters = SubscriptionListRequest.builder()
                .state("active")
                .build();
        SubscriptionsPage page = client.subscriptions().list(filters);
        assertNotNull(page);
        assertNotNull(page.getResults());
    }

    @Test
    void testListTransactionsWithInvalidId() {
        long invalidId = 9999999L;
        SubscriptionTransactionsPage page = client.subscriptions().listTransactions(invalidId);
        assertNotNull(page);
        assertTrue(page.getResults() == null || page.getResults().isEmpty());
    }
}
