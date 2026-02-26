package com.paymob.sdk.core;

import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.core.auth.SecretKeyAuthStrategy;
import com.paymob.sdk.core.auth.BearerTokenAuthStrategy;
import com.paymob.sdk.http.HttpClient;
import com.paymob.sdk.http.OkHttpClientAdapter;
import com.paymob.sdk.services.intention.IntentionService;
import com.paymob.sdk.services.transaction.TransactionService;
import com.paymob.sdk.services.inquiry.TransactionInquiryService;
import com.paymob.sdk.services.savedcard.SavedCardService;
import com.paymob.sdk.services.subscription.SubscriptionService;
import com.paymob.sdk.services.subscription.plan.SubscriptionPlanService;
import com.paymob.sdk.services.quicklink.QuickLinkService;

/**
 * Main entry point for the Paymob Java SDK.
 * Builder pattern for configuration and access to all services.
 */
public class PaymobClient {
    private final PaymobConfig config;
    private final AuthStrategy secretKeyAuth;
    private final AuthStrategy bearerTokenAuth;

    private final IntentionService intentionService;
    private final TransactionService transactionService;
    private final TransactionInquiryService inquiryService;
    private final SavedCardService savedCardService;
    private final SubscriptionService subscriptionService;
    private final SubscriptionPlanService subscriptionPlanService;
    private final QuickLinkService quickLinkService;

    /**
     * Initializes the client with a direct configuration.
     * Uses the default HTTP client implementation.
     * 
     * @param config The SDK configuration
     */
    public PaymobClient(PaymobConfig config) {
        this(config, createDefaultHttpClient(config));
    }

    private PaymobClient(PaymobConfig config, HttpClient httpClient) {
        this.config = config;
        this.secretKeyAuth = new SecretKeyAuthStrategy(config.getSecretKey());
        this.bearerTokenAuth = new BearerTokenAuthStrategy(config.getApiKey(), config.getRegion().getBaseUrl(),
                httpClient);

        // Initialize services
        this.intentionService = new IntentionService(httpClient, secretKeyAuth, config);
        this.transactionService = new TransactionService(httpClient, secretKeyAuth, config);
        this.inquiryService = new TransactionInquiryService(httpClient, bearerTokenAuth, config);
        this.savedCardService = new SavedCardService(httpClient, secretKeyAuth, config);
        this.subscriptionPlanService = new SubscriptionPlanService(httpClient, bearerTokenAuth, config);
        this.subscriptionService = new SubscriptionService(httpClient, secretKeyAuth, bearerTokenAuth, config);
        this.quickLinkService = new QuickLinkService(httpClient, bearerTokenAuth, config);
    }

    private static HttpClient createDefaultHttpClient(PaymobConfig config) {
        HttpClient client = new OkHttpClientAdapter();
        client.setTimeout((int) config.getTimeout().getSeconds());
        client.setLogLevel(config.getLogLevel());
        return client;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Service accessors
    public IntentionService intentions() {
        return intentionService;
    }

    public TransactionService transactions() {
        return transactionService;
    }

    public TransactionInquiryService inquiry() {
        return inquiryService;
    }

    public SavedCardService savedCards() {
        return savedCardService;
    }

    public SubscriptionService subscriptions() {
        return subscriptionService;
    }

    public SubscriptionPlanService subscriptionPlans() {
        return subscriptionPlanService;
    }

    public QuickLinkService quickLinks() {
        return quickLinkService;
    }

    public PaymobConfig getConfig() {
        return config;
    }

    public static class Builder {
        private PaymobConfig config;
        private HttpClient httpClient;

        public Builder config(PaymobConfig config) {
            this.config = config;
            return this;
        }

        public Builder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public PaymobClient build() {
            if (config == null) {
                throw new IllegalArgumentException("Config is required");
            }
            if (httpClient == null) {
                httpClient = createDefaultHttpClient(config);
            }
            return new PaymobClient(config, httpClient);
        }
    }
}
