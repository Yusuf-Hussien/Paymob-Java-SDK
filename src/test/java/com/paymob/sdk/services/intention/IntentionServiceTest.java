package com.paymob.sdk.services.intention;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class IntentionServiceTest {

    private HttpClient httpClient;
    private AuthStrategy authStrategy;
    private PaymobConfig config;
    private IntentionService service;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        authStrategy = mock(AuthStrategy.class);
        config = PaymobConfig.builder()
                .secretKey("sk_test")
                .apiKey("ak_test")
                .publicKey("pk_test")
                .region(PaymobRegion.EGYPT)
                .build();
        service = new IntentionService(httpClient, authStrategy, config);
    }

    @Test
    void createIntention_usesCorrectEndpointAndAuth() {
        when(httpClient.post(anyString(), any(), eq(IntentionResponse.class), eq(authStrategy)))
                .thenReturn(new IntentionResponse());

        service.createIntention(IntentionRequest.builder().amount(1000).build());

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).post(eq("/v1/intention/"), any(IntentionRequest.class),
                eq(IntentionResponse.class), same(authStrategy));
    }

    @Test
    void updateIntention_usesCorrectEndpointWithClientSecret() {
        when(httpClient.put(anyString(), any(), eq(IntentionResponse.class), eq(authStrategy)))
                .thenReturn(new IntentionResponse());

        service.updateIntention("cs_test_123", IntentionRequest.builder().amount(2000).build());

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).put(eq("/v1/intention/cs_test_123"), any(IntentionRequest.class),
                eq(IntentionResponse.class), same(authStrategy));
    }

    @Test
    void retrieveIntention_usesCorrectEndpointWithPublicKeyAndSecret() {
        when(httpClient.get(anyString(), eq(IntentionResponse.class), eq(authStrategy)))
                .thenReturn(new IntentionResponse());

        service.retrieveIntention("cs_test_456");

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).get(eq("/v1/intention/element/pk_test/cs_test_456/"),
                eq(IntentionResponse.class), same(authStrategy));
    }

    @Test
    void getUnifiedCheckoutUrl_fromClientSecret_containsPublicKeyAndSecret() {
        String url = service.getUnifiedCheckoutUrl("cs_secret_789");

        assertTrue(url.contains("pk_test"));
        assertTrue(url.contains("cs_secret_789"));
        assertTrue(url.startsWith(PaymobRegion.EGYPT.getBaseUrl()));
    }

    @Test
    void getUnifiedCheckoutUrl_fromResponse_delegatesToStringVersion() {
        IntentionResponse response = new IntentionResponse();
        response.setClientSecret("cs_from_response");

        String url = service.getUnifiedCheckoutUrl(response);

        assertTrue(url.contains("cs_from_response"));
        assertTrue(url.contains("pk_test"));
    }

    @Test
    void getUnifiedCheckoutUrl_fromNullResponse_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getUnifiedCheckoutUrl((IntentionResponse) null));
    }
}
