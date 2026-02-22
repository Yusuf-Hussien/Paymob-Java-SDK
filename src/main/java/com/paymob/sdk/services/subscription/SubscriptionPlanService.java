package com.paymob.sdk.services.subscription;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;

import java.util.Arrays;
import java.util.List;

public class SubscriptionPlanService {
    private final HttpClient httpClient;
    private final AuthStrategy authStrategy;
    private final PaymobConfig config;

    public SubscriptionPlanService(HttpClient httpClient, AuthStrategy authStrategy, PaymobConfig config) {
        this.httpClient = httpClient;
        this.authStrategy = authStrategy;
        this.config = config;
    }

    public SubscriptionPlanResponse create(SubscriptionPlanRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/subscription-plans", request, SubscriptionPlanResponse.class, authStrategy);
    }

    public List<SubscriptionPlanResponse> list() {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        SubscriptionPlanResponse[] response = httpClient.get("/api/acceptance/subscription-plans", SubscriptionPlanResponse[].class, authStrategy);
        return response == null ? List.of() : Arrays.asList(response);
    }

    public SubscriptionPlanResponse update(long planId, SubscriptionPlanUpdateRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.put("/api/acceptance/subscription-plans/" + planId, request, SubscriptionPlanResponse.class, authStrategy);
    }

    public SubscriptionPlanResponse suspend(long planId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/subscription-plans/" + planId + "/suspend", new Object(), SubscriptionPlanResponse.class, authStrategy);
    }

    public SubscriptionPlanResponse resume(long planId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/subscription-plans/" + planId + "/resume", new Object(), SubscriptionPlanResponse.class, authStrategy);
    }
}
