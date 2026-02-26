package com.paymob.sdk.services.subscription.plan;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;

/**
 * Service for managing subscription plans.
 */
public class SubscriptionPlanService {
    private final HttpClient httpClient;
    private final AuthStrategy authStrategy;
    private final PaymobConfig config;

    public SubscriptionPlanService(HttpClient httpClient, AuthStrategy authStrategy, PaymobConfig config) {
        this.httpClient = httpClient;
        this.authStrategy = authStrategy;
        this.config = config;
    }

    /**
     * Creates a new subscription plan.
     * 
     * @param request The plan details
     * @return Created plan details
     */
    public SubscriptionPlanResponse create(SubscriptionPlanRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/subscription-plans", request, SubscriptionPlanResponse.class,
                authStrategy);
    }

    /**
     * Lists all subscription plans.
     * 
     * @return Paginated list of all plans
     */
    public SubscriptionPlansPage list() {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.get("/api/acceptance/subscription-plans", SubscriptionPlansPage.class, authStrategy);
    }

    /**
     * Updates an existing subscription plan.
     * Only certain fields are updatable.
     * 
     * @param planId  The ID of the plan to update
     * @param request The fields to update
     * @return Updated plan details
     */
    public SubscriptionPlanResponse update(long planId, SubscriptionPlanUpdateRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.put("/api/acceptance/subscription-plans/" + planId, request, SubscriptionPlanResponse.class,
                authStrategy);
    }

    /**
     * Suspends a subscription plan.
     * Prevents new subscriptions from being created under this plan.
     * 
     * @param planId The ID of the plan to suspend
     * @return Updated plan details
     */
    public SubscriptionPlanResponse suspend(long planId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/subscription-plans/" + planId + "/suspend",
                java.util.Collections.emptyMap(),
                SubscriptionPlanResponse.class, authStrategy);
    }

    /**
     * Resumes a suspended subscription plan.
     * 
     * @param planId The ID of the plan to resume
     * @return Updated plan details
     */
    public SubscriptionPlanResponse resume(long planId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/subscription-plans/" + planId + "/resume",
                java.util.Collections.emptyMap(),
                SubscriptionPlanResponse.class, authStrategy);
    }
}
