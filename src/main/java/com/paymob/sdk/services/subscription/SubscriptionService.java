package com.paymob.sdk.services.subscription;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;
import com.paymob.sdk.services.intention.IntentionRequest;
import com.paymob.sdk.services.intention.IntentionResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Service for managing subscription lifecycle and recurring billing.
 * Full subscription management with configurable billing cycles.
 */
public class SubscriptionService {
    private final HttpClient httpClient;
    private final AuthStrategy secretKeyAuth;
    private final AuthStrategy bearerTokenAuth;
    private final PaymobConfig config;

    public SubscriptionService(HttpClient httpClient, AuthStrategy secretKeyAuth, AuthStrategy bearerTokenAuth, PaymobConfig config) {
        this.httpClient = httpClient;
        this.secretKeyAuth = secretKeyAuth;
        this.bearerTokenAuth = bearerTokenAuth;
        this.config = config;
    }

    /**
     * Creates a customer subscription (initial enrollment) using Intention API.
     * Required: subscription_plan_id must be set in the intention request.
     */
    public IntentionResponse subscribe(IntentionRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/v1/intention/", request, IntentionResponse.class, secretKeyAuth);
    }

    /**
     * Adds a secondary card to an existing subscription using Intention API.
     * Required: subscriptionv2_id must be set in the intention request.
     */
    public IntentionResponse addSecondaryCard(IntentionRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/v1/intention/", request, IntentionResponse.class, secretKeyAuth);
    }

    public SubscriptionResponse get(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.get("/api/acceptance/subscriptions/" + subscriptionId, SubscriptionResponse.class, bearerTokenAuth);
    }

    public List<SubscriptionResponse> list(SubscriptionListRequest filters) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        String endpoint = "/api/acceptance/subscriptions";
        String query = buildListQuery(filters);
        if (!query.isBlank()) {
            endpoint = endpoint + "?" + query;
        }

        SubscriptionResponse[] response = httpClient.get(endpoint, SubscriptionResponse[].class, bearerTokenAuth);
        return response == null ? List.of() : Arrays.asList(response);
    }

    public SubscriptionResponse update(long subscriptionId, SubscriptionUpdateRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.put("/api/acceptance/subscriptions/" + subscriptionId, request, SubscriptionResponse.class, bearerTokenAuth);
    }

    /**
     * Suspends an active subscription.
     * @param subscriptionId The subscription to suspend
     * @return Updated subscription status
     */
    public SubscriptionResponse suspend(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/subscriptions/" + subscriptionId + "/suspend", new Object(), SubscriptionResponse.class, bearerTokenAuth);
    }

    /**
     * Resumes a suspended subscription.
     * @param subscriptionId The subscription to resume
     * @return Updated subscription status
     */
    public SubscriptionResponse resume(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/subscriptions/" + subscriptionId + "/resume", new Object(), SubscriptionResponse.class, bearerTokenAuth);
    }

    /**
     * Cancels a subscription permanently.
     * @param subscriptionId The subscription to cancel
     * @return Updated subscription status
     */
    public SubscriptionResponse cancel(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/subscriptions/" + subscriptionId + "/cancel", new Object(), SubscriptionResponse.class, bearerTokenAuth);
    }

    public SubscriptionTransactionResponse getLastTransaction(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.get("/api/acceptance/subscriptions/" + subscriptionId + "/last-transaction", SubscriptionTransactionResponse.class, bearerTokenAuth);
    }

    public SubscriptionTransactionsPage listTransactions(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.get("/api/acceptance/subscriptions/" + subscriptionId + "/transactions", SubscriptionTransactionsPage.class, bearerTokenAuth);
    }

    public List<SubscriptionCardResponse> listCards(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        SubscriptionCardResponse[] response = httpClient.get("/api/acceptance/subscriptions/" + subscriptionId + "/card-tokens", SubscriptionCardResponse[].class, bearerTokenAuth);
        return response == null ? List.of() : Arrays.asList(response);
    }

    public SubscriptionResponse deleteCard(long subscriptionId, long cardId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        SubscriptionCardActionRequest request = new SubscriptionCardActionRequest(cardId);
        return httpClient.post("/api/acceptance/subscriptions/" + subscriptionId + "/delete-card", request, SubscriptionResponse.class, bearerTokenAuth);
    }

    public SubscriptionResponse changePrimaryCard(long subscriptionId, long cardId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        SubscriptionCardActionRequest request = new SubscriptionCardActionRequest(cardId);
        return httpClient.post("/api/acceptance/subscriptions/" + subscriptionId + "/change-primary-card", request, SubscriptionResponse.class, bearerTokenAuth);
    }

    private static String buildListQuery(SubscriptionListRequest filters) {
        if (filters == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        append(sb, "transaction", filters.getTransaction());
        append(sb, "plan_id", filters.getPlanId());
        append(sb, "state", filters.getState());
        append(sb, "starts_at", filters.getStartsAt());
        append(sb, "next_billing", filters.getNextBilling());
        append(sb, "reminder_date", filters.getReminderDate());
        append(sb, "ends_at", filters.getEndsAt());
        return sb.toString();
    }

    private static void append(StringBuilder sb, String key, Object value) {
        if (value == null) {
            return;
        }
        String encodedValue = URLEncoder.encode(String.valueOf(value), StandardCharsets.UTF_8);
        if (!sb.isEmpty()) {
            sb.append("&");
        }
        sb.append(key).append("=").append(encodedValue);
    }
}
