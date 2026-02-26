package com.paymob.sdk.services.subscription.plan;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SubscriptionPlanServiceTest {

    private HttpClient httpClient;
    private AuthStrategy authStrategy;
    private PaymobConfig config;
    private SubscriptionPlanService service;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        authStrategy = mock(AuthStrategy.class);
        config = PaymobConfig.builder()
                .secretKey("sk_test")
                .apiKey("ak_test")
                .region(PaymobRegion.EGYPT)
                .build();
        service = new SubscriptionPlanService(httpClient, authStrategy, config);
    }

    @Test
    void create_usesCorrectEndpoint() {
        when(httpClient.post(anyString(), any(), eq(SubscriptionPlanResponse.class), eq(authStrategy)))
                .thenReturn(new SubscriptionPlanResponse());

        SubscriptionPlanRequest request = SubscriptionPlanRequest.builder()
                .name("Test Plan")
                .frequency(30)
                .amountCents(1000)
                .build();

        service.create(request);

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).post(eq("/api/acceptance/subscription-plans"),
                any(SubscriptionPlanRequest.class), eq(SubscriptionPlanResponse.class), same(authStrategy));
    }

    @Test
    void list_usesCorrectEndpoint() {
        when(httpClient.get(anyString(), eq(SubscriptionPlansPage.class), eq(authStrategy)))
                .thenReturn(new SubscriptionPlansPage());

        service.list();

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).get(eq("/api/acceptance/subscription-plans"),
                eq(SubscriptionPlansPage.class), same(authStrategy));
    }

    @Test
    void update_usesCorrectEndpointWithId() {
        when(httpClient.put(anyString(), any(), eq(SubscriptionPlanResponse.class), eq(authStrategy)))
                .thenReturn(new SubscriptionPlanResponse());

        SubscriptionPlanUpdateRequest request = SubscriptionPlanUpdateRequest.builder()
                .amountCents(2000)
                .build();

        service.update(5L, request);

        verify(httpClient).put(eq("/api/acceptance/subscription-plans/5"),
                any(SubscriptionPlanUpdateRequest.class), eq(SubscriptionPlanResponse.class), same(authStrategy));
    }

    @Test
    void suspend_usesCorrectEndpoint() {
        when(httpClient.post(anyString(), any(), eq(SubscriptionPlanResponse.class), eq(authStrategy)))
                .thenReturn(new SubscriptionPlanResponse());

        service.suspend(7L);

        verify(httpClient).post(eq("/api/acceptance/subscription-plans/7/suspend"),
                any(), eq(SubscriptionPlanResponse.class), same(authStrategy));
    }

    @Test
    void resume_usesCorrectEndpoint() {
        when(httpClient.post(anyString(), any(), eq(SubscriptionPlanResponse.class), eq(authStrategy)))
                .thenReturn(new SubscriptionPlanResponse());

        service.resume(7L);

        verify(httpClient).post(eq("/api/acceptance/subscription-plans/7/resume"),
                any(), eq(SubscriptionPlanResponse.class), same(authStrategy));
    }
}
