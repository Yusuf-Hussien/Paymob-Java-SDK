package com.paymob.sdk.integration.services;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.services.subscription.plan.SubscriptionPlanRequest;
import com.paymob.sdk.services.subscription.plan.SubscriptionPlanResponse;
import com.paymob.sdk.services.subscription.plan.SubscriptionPlanUpdateRequest;
import com.paymob.sdk.testutil.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class SubscriptionPlanServiceIT {
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        client = IntegrationTestConfig.createClientFromEnv();
    }

    @Test
    void testPlanLifecycle() {
        String planName = "Test Plan " + System.currentTimeMillis();
        SubscriptionPlanRequest request = SubscriptionPlanRequest.builder()
                .name(planName)
                .frequency(30)
                .amountCents(1000)
                .planType("rent")
                .isActive(true)
                .integration(IntegrationTestConfig.getIntegrationId())
                .build();

        SubscriptionPlanResponse createResponse = client.subscriptionPlans().create(request);
        assertNotNull(createResponse);
        assertEquals(planName, createResponse.getName());
        long planId = createResponse.getId();

        SubscriptionPlanResponse retrievedPlan = client.subscriptionPlans().retrieve(planId);
        assertNotNull(retrievedPlan);
        assertEquals(planId, retrievedPlan.getId());

        SubscriptionPlanUpdateRequest updateRequest = SubscriptionPlanUpdateRequest.builder()
                .amountCents(2000)
                .build();
        SubscriptionPlanResponse updateResponse = client.subscriptionPlans().update(planId, updateRequest);
        assertEquals(2000, updateResponse.getAmountCents());

        SubscriptionPlanResponse suspendResponse = client.subscriptionPlans().suspend(planId);
        assertFalse(suspendResponse.getIsActive());

        SubscriptionPlanResponse resumeResponse = client.subscriptionPlans().resume(planId);
        assertTrue(resumeResponse.getIsActive());
    }
}
