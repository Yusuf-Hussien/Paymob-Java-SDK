package com.paymob.sdk.services.subscription;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.utils.TestConfigUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        List<SubscriptionResponse> list = client.subscriptions().list(filters);
        assertNotNull(list);
    }

    @Test
    void testListTransactionsWithInvalidId() {
        long invalidId = 9999999L;
        assertThrows(Exception.class, () -> {
            client.subscriptions().listTransactions(invalidId);
        });
    }
}
