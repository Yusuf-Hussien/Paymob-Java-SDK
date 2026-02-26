package com.paymob.sdk.services.subscription;

/**
 * Service for managing subscription lifecycle and recurring billing.
 * Full subscription management with configurable billing cycles.
 */
public class SubscriptionService {
    private final com.paymob.sdk.http.HttpClient httpClient;
    private final com.paymob.sdk.core.auth.AuthStrategy secretKeyAuth;
    private final com.paymob.sdk.core.auth.AuthStrategy bearerTokenAuth;
    private final com.paymob.sdk.core.PaymobConfig config;

    public SubscriptionService(com.paymob.sdk.http.HttpClient httpClient,
            com.paymob.sdk.core.auth.AuthStrategy secretKeyAuth,
            com.paymob.sdk.core.auth.AuthStrategy bearerTokenAuth,
            com.paymob.sdk.core.PaymobConfig config) {
        this.httpClient = httpClient;
        this.secretKeyAuth = secretKeyAuth;
        this.bearerTokenAuth = bearerTokenAuth;
        this.config = config;
    }

    /**
     * Creates a customer subscription (initial enrollment) using Intention API.
     * Required: subscription_plan_id must be set in the intention request.
     * 
     * @param request The intention request for subscription
     * @return Intention response containing client_secret
     */
    public com.paymob.sdk.services.intention.IntentionResponse subscribe(
            com.paymob.sdk.services.intention.IntentionRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/v1/intention/", request, com.paymob.sdk.services.intention.IntentionResponse.class,
                secretKeyAuth);
    }

    /**
     * Adds a secondary card to an existing subscription using Intention API.
     * Required: subscriptionv2_id must be set in the intention request.
     * 
     * @param request The intention request for adding a card
     * @return Intention response containing client_secret
     */
    public com.paymob.sdk.services.intention.IntentionResponse addSecondaryCard(
            com.paymob.sdk.services.intention.IntentionRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/v1/intention/", request, com.paymob.sdk.services.intention.IntentionResponse.class,
                secretKeyAuth);
    }

    /**
     * Retrieves subscription details.
     * 
     * @param subscriptionId The subscription ID
     * @return Subscription details
     */
    public SubscriptionResponse get(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.get("/api/acceptance/subscriptions/" + subscriptionId, SubscriptionResponse.class,
                bearerTokenAuth);
    }

    /**
     * Lists subscriptions with optional filters.
     * 
     * @param filters Query filters
     * @return Paginated list of subscriptions
     */
    public SubscriptionsPage list(SubscriptionListRequest filters) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        String endpoint = "/api/acceptance/subscriptions";
        String query = buildListQuery(filters);
        if (!query.isBlank()) {
            endpoint = endpoint + "?" + query;
        }

        return httpClient.get(endpoint, SubscriptionsPage.class, bearerTokenAuth);
    }

    /**
     * Updates an existing subscription.
     * 
     * @param subscriptionId The subscription to update
     * @param request        The update details
     * @return Updated subscription status
     */
    public SubscriptionResponse update(long subscriptionId, SubscriptionUpdateRequest request) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.put("/api/acceptance/subscriptions/" + subscriptionId, request, SubscriptionResponse.class,
                bearerTokenAuth);
    }

    /**
     * Suspends an active subscription.
     * 
     * @param subscriptionId The subscription to suspend
     * @return Updated subscription status
     */
    public SubscriptionResponse suspend(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/subscriptions/" + subscriptionId + "/suspend",
                java.util.Collections.emptyMap(),
                SubscriptionResponse.class, bearerTokenAuth);
    }

    /**
     * Resumes a suspended subscription.
     * 
     * @param subscriptionId The subscription to resume
     * @return Updated subscription status
     */
    public SubscriptionResponse resume(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/subscriptions/" + subscriptionId + "/resume",
                java.util.Collections.emptyMap(),
                SubscriptionResponse.class, bearerTokenAuth);
    }

    /**
     * Cancels a subscription permanently.
     * 
     * @param subscriptionId The subscription to cancel
     * @return Updated subscription status
     */
    public SubscriptionResponse cancel(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.post("/api/acceptance/subscriptions/" + subscriptionId + "/cancel",
                java.util.Collections.emptyMap(),
                SubscriptionResponse.class, bearerTokenAuth);
    }

    /**
     * Retrieves the last transaction for a subscription.
     * 
     * @param subscriptionId The subscription ID
     * @return Last transaction details
     */
    public SubscriptionTransactionResponse getLastTransaction(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.get("/api/acceptance/subscriptions/" + subscriptionId + "/last-transaction",
                SubscriptionTransactionResponse.class, bearerTokenAuth);
    }

    /**
     * Lists all transactions for a subscription.
     * 
     * @param subscriptionId The subscription ID
     * @return Paginated list of transactions
     */
    public SubscriptionTransactionsPage listTransactions(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        return httpClient.get("/api/acceptance/subscriptions/" + subscriptionId + "/transactions",
                SubscriptionTransactionsPage.class, bearerTokenAuth);
    }

    /**
     * Lists cards saved for a subscription.
     * 
     * @param subscriptionId The subscription ID
     * @return List of card tokens
     */
    public java.util.List<SubscriptionCardResponse> listCards(long subscriptionId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        SubscriptionCardResponse[] response = httpClient.get(
                "/api/acceptance/subscriptions/" + subscriptionId + "/card-tokens", SubscriptionCardResponse[].class,
                bearerTokenAuth);
        return response == null ? java.util.List.of() : java.util.Arrays.asList(response);
    }

    /**
     * Deletes a secondary card from a subscription.
     * 
     * @param subscriptionId The subscription ID
     * @param cardId         The card ID to delete
     * @return Updated subscription status
     */
    public SubscriptionResponse deleteCard(long subscriptionId, long cardId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        SubscriptionCardActionRequest request = new SubscriptionCardActionRequest(cardId);
        return httpClient.post("/api/acceptance/subscriptions/" + subscriptionId + "/delete-card", request,
                SubscriptionResponse.class, bearerTokenAuth);
    }

    /**
     * Changes the primary card for a subscription.
     * 
     * @param subscriptionId The subscription ID
     * @param cardId         The new primary card ID
     * @return Updated subscription status
     */
    public SubscriptionResponse changePrimaryCard(long subscriptionId, long cardId) {
        httpClient.setBaseUrl(config.getRegion().getBaseUrl());
        SubscriptionCardActionRequest request = new SubscriptionCardActionRequest(cardId);
        return httpClient.post("/api/acceptance/subscriptions/" + subscriptionId + "/change-primary-card", request,
                SubscriptionResponse.class, bearerTokenAuth);
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
        String encodedValue = java.net.URLEncoder.encode(String.valueOf(value),
                java.nio.charset.StandardCharsets.UTF_8);
        if (!sb.isEmpty()) {
            sb.append("&");
        }
        sb.append(key).append("=").append(encodedValue);
    }
}
