package com.paymob.sdk.services.subscription;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.utils.TestConfigUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class SubscriptionIntegrationTest {
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        client = TestConfigUtils.createClientFromEnv();
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
        // API returns 200 OK with empty results for non-existent subscription
        SubscriptionTransactionsPage page = client.subscriptions().listTransactions(invalidId);
        assertNotNull(page);
        assertTrue(page.getResults() == null || page.getResults().isEmpty());
    }
}
