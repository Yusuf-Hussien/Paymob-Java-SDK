package com.paymob.sdk.services.subscription;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.services.subscription.plan.SubscriptionPlanRequest;
import com.paymob.sdk.services.subscription.plan.SubscriptionPlanResponse;
import com.paymob.sdk.services.subscription.plan.SubscriptionPlanUpdateRequest;
import com.paymob.sdk.services.subscription.plan.SubscriptionPlansPage;
import com.paymob.sdk.utils.TestConfigUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class SubscriptionPlanIntegrationTest {
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        client = TestConfigUtils.createClientFromEnv();
    }

    @Test
    void testPlanLifecycle() {
        // 1. Create a plan
        String planName = "Test Plan " + System.currentTimeMillis();
        SubscriptionPlanRequest request = SubscriptionPlanRequest.builder()
                .name(planName)
                .frequency(30)
                .amountCents(1000)
                .planType("rent")
                .isActive(true)
                .integration(TestConfigUtils.getIntegrationId())
                .build();

        SubscriptionPlanResponse createResponse = client.subscriptionPlans().create(request);
        assertNotNull(createResponse);
        assertEquals(planName, createResponse.getName());
        long planId = createResponse.getId();

        // 2. List plans and find the new one
        SubscriptionPlansPage page = client.subscriptionPlans().list();
        assertNotNull(page);
        assertTrue(page.getResults().stream().anyMatch(p -> p.getId() == planId));

        // 3. Update the plan
        SubscriptionPlanUpdateRequest updateRequest = SubscriptionPlanUpdateRequest.builder()
                .amountCents(2000)
                .build();
        SubscriptionPlanResponse updateResponse = client.subscriptionPlans().update(planId, updateRequest);
        assertEquals(2000, updateResponse.getAmountCents());

        // 4. Suspend the plan
        SubscriptionPlanResponse suspendResponse = client.subscriptionPlans().suspend(planId);
        assertFalse(suspendResponse.getIsActive());

        // 5. Resume the plan
        SubscriptionPlanResponse resumeResponse = client.subscriptionPlans().resume(planId);
        assertTrue(resumeResponse.getIsActive());
    }
}
