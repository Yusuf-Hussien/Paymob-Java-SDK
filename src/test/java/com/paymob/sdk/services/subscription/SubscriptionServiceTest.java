package com.paymob.sdk.services.subscription;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;
import com.paymob.sdk.services.intention.IntentionRequest;
import com.paymob.sdk.services.intention.IntentionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

        private HttpClient httpClient;
        private AuthStrategy secretKeyAuth;
        private AuthStrategy bearerTokenAuth;
        private PaymobConfig config;
        private SubscriptionService service;

        @BeforeEach
        void setUp() {
                httpClient = mock(HttpClient.class);
                secretKeyAuth = mock(AuthStrategy.class);
                bearerTokenAuth = mock(AuthStrategy.class);
                config = PaymobConfig.builder()
                                .secretKey("sk_test")
                                .apiKey("ak_test")
                                .region(PaymobRegion.EGYPT)
                                .build();
                service = new SubscriptionService(httpClient, secretKeyAuth, bearerTokenAuth, config);
        }

        @Test
        void subscribe_usesIntentionEndpoint() {
                when(httpClient.post(anyString(), any(), eq(IntentionResponse.class), eq(secretKeyAuth)))
                                .thenReturn(new IntentionResponse());

                IntentionRequest request = IntentionRequest.builder().amount(1000).build();
                service.subscribe(request);

                verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
                verify(httpClient).post(eq("/v1/intention/"), any(IntentionRequest.class),
                                eq(IntentionResponse.class), same(secretKeyAuth));
        }

        @Test
        void get_usesCorrectEndpoint() {
                when(httpClient.get(anyString(), eq(SubscriptionResponse.class), eq(bearerTokenAuth)))
                                .thenReturn(new SubscriptionResponse());

                service.get(42L);

                verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
                verify(httpClient).get(eq("/api/acceptance/subscriptions/42"),
                                eq(SubscriptionResponse.class), same(bearerTokenAuth));
        }

        @Test
        void suspend_usesCorrectEndpoint() {
                when(httpClient.post(anyString(), any(), eq(SubscriptionResponse.class), eq(bearerTokenAuth)))
                                .thenReturn(new SubscriptionResponse());

                service.suspend(10L);

                verify(httpClient).post(eq("/api/acceptance/subscriptions/10/suspend"),
                                any(), eq(SubscriptionResponse.class), same(bearerTokenAuth));
        }

        @Test
        void resume_usesCorrectEndpoint() {
                when(httpClient.post(anyString(), any(), eq(SubscriptionResponse.class), eq(bearerTokenAuth)))
                                .thenReturn(new SubscriptionResponse());

                service.resume(11L);

                verify(httpClient).post(eq("/api/acceptance/subscriptions/11/resume"),
                                any(), eq(SubscriptionResponse.class), same(bearerTokenAuth));
        }

        @Test
        void cancel_usesCorrectEndpoint() {
                when(httpClient.post(anyString(), any(), eq(SubscriptionResponse.class), eq(bearerTokenAuth)))
                                .thenReturn(new SubscriptionResponse());

                service.cancel(12L);

                verify(httpClient).post(eq("/api/acceptance/subscriptions/12/cancel"),
                                any(), eq(SubscriptionResponse.class), same(bearerTokenAuth));
        }

        @Test
        void listTransactions_usesCorrectEndpoint() {
                when(httpClient.get(anyString(), eq(SubscriptionTransactionsPage.class), eq(bearerTokenAuth)))
                                .thenReturn(new SubscriptionTransactionsPage());

                service.listTransactions(99L);

                verify(httpClient).get(eq("/api/acceptance/subscriptions/99/transactions"),
                                eq(SubscriptionTransactionsPage.class), same(bearerTokenAuth));
        }

        @Test
        void update_usesCorrectEndpoint() {
                when(httpClient.put(anyString(), any(), eq(SubscriptionResponse.class), eq(bearerTokenAuth)))
                                .thenReturn(new SubscriptionResponse());

                SubscriptionUpdateRequest request = SubscriptionUpdateRequest.builder().build();
                service.update(50L, request);

                verify(httpClient).put(eq("/api/acceptance/subscriptions/50"),
                                any(SubscriptionUpdateRequest.class), eq(SubscriptionResponse.class),
                                same(bearerTokenAuth));
        }
}
