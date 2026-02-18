package com.paymob.sdk.services.subscription;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;

/**
 * Service for managing subscription lifecycle and recurring billing.
 * Full subscription management with configurable billing cycles.
 */
public class SubscriptionService {
    private final HttpClient httpClient;
    private final AuthStrategy authStrategy;
    private final PaymobConfig config;

    public SubscriptionService(HttpClient httpClient, AuthStrategy authStrategy, PaymobConfig config) {
        this.httpClient = httpClient;
        this.authStrategy = authStrategy;
        this.config = config;
    }

    /**
     * Creates a new subscription plan.
     * @param request The plan creation request
     * @return Created plan details
     */
    public SubscriptionResponse createPlan(PlanRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/subscriptions/plans", request, SubscriptionResponse.class, authStrategy);
    }

    /**
     * Creates a customer subscription.
     * @param request The subscription request
     * @return Subscription details
     */
    public SubscriptionResponse createSubscription(SubscriptionRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/subscriptions", request, SubscriptionResponse.class, authStrategy);
    }

    /**
     * Suspends an active subscription.
     * @param subscriptionId The subscription to suspend
     * @return Updated subscription status
     */
    public SubscriptionResponse suspendSubscription(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        SubscriptionLifecycleRequest request = new SubscriptionLifecycleRequest();
        request.setAction("SUSPEND");
        return httpClient.post("/api/subscriptions/" + subscriptionId + "/lifecycle", request, SubscriptionResponse.class, authStrategy);
    }

    /**
     * Resumes a suspended subscription.
     * @param subscriptionId The subscription to resume
     * @return Updated subscription status
     */
    public SubscriptionResponse resumeSubscription(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        SubscriptionLifecycleRequest request = new SubscriptionLifecycleRequest();
        request.setAction("RESUME");
        return httpClient.post("/api/subscriptions/" + subscriptionId + "/lifecycle", request, SubscriptionResponse.class, authStrategy);
    }

    /**
     * Cancels a subscription permanently.
     * @param subscriptionId The subscription to cancel
     * @return Updated subscription status
     */
    public SubscriptionResponse cancelSubscription(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        SubscriptionLifecycleRequest request = new SubscriptionLifecycleRequest();
        request.setAction("CANCEL");
        return httpClient.post("/api/subscriptions/" + subscriptionId + "/lifecycle", request, SubscriptionResponse.class, authStrategy);
    }
}
