package com.paymob.sdk.core;

import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.core.auth.SecretKeyAuthStrategy;
import com.paymob.sdk.core.auth.BearerTokenAuthStrategy;
import com.paymob.sdk.http.HttpClient;
import com.paymob.sdk.services.intention.IntentionService;
import com.paymob.sdk.services.transaction.TransactionService;
import com.paymob.sdk.services.inquiry.TransactionInquiryService;
import com.paymob.sdk.services.savedcard.SavedCardService;
import com.paymob.sdk.services.subscription.SubscriptionService;
import com.paymob.sdk.services.quicklink.QuickLinkService;

/**
 * Main entry point for the Paymob Java SDK.
 * Builder pattern for configuration and access to all services.
 */
public class PaymobClient {
    private final PaymobConfig config;
    private final HttpClient httpClient;
    private final AuthStrategy secretKeyAuth;
    private final AuthStrategy bearerTokenAuth;

    private final IntentionService intentionService;
    private final TransactionService transactionService;
    private final TransactionInquiryService inquiryService;
    private final SavedCardService savedCardService;
    private final SubscriptionService subscriptionService;
    private final QuickLinkService quickLinkService;

    private PaymobClient(Builder builder) {
        this.config = builder.config;
        this.httpClient = builder.httpClient;
        this.secretKeyAuth = new SecretKeyAuthStrategy(config.getSecretKey());
        this.bearerTokenAuth = new BearerTokenAuthStrategy(config.getApiKey());

        // Initialize services
        this.intentionService = new IntentionService(httpClient, secretKeyAuth, config);
        this.transactionService = new TransactionService(httpClient, secretKeyAuth, config);
        this.inquiryService = new TransactionInquiryService(httpClient, secretKeyAuth, config);
        this.savedCardService = new SavedCardService(httpClient, secretKeyAuth, config);
        this.subscriptionService = new SubscriptionService(httpClient, bearerTokenAuth, config);
        this.quickLinkService = new QuickLinkService(httpClient, secretKeyAuth, config);
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
                // Use default HTTP client implementation
                httpClient = new OkHttpClientAdapter();
                httpClient.setTimeout((int) config.getTimeout().getSeconds());
            }
            return new PaymobClient(this);
        }
    }
}
