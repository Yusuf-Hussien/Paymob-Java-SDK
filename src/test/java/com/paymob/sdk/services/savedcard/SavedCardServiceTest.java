package com.paymob.sdk.services.savedcard;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SavedCardServiceTest {

    private HttpClient httpClient;
    private AuthStrategy authStrategy;
    private PaymobConfig config;
    private SavedCardService service;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        authStrategy = mock(AuthStrategy.class);
        config = PaymobConfig.builder()
                .secretKey("sk_test")
                .apiKey("ak_test")
                .region(PaymobRegion.EGYPT)
                .build();
        service = new SavedCardService(httpClient, authStrategy, config);
    }

    @Test
    void processCitPayment_usesIntentionEndpoint() {
        when(httpClient.post(anyString(), any(), eq(TokenizedPaymentResponse.class), eq(authStrategy)))
                .thenReturn(new TokenizedPaymentResponse());

        CitPaymentRequest request = CitPaymentRequest.builder()
                .cardToken("tok_123")
                .cvv("456")
                .amount(1000)
                .currency("EGP")
                .build();

        service.processCitPayment(request);

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).post(eq("/v1/intention/"), any(CitPaymentRequest.class),
                eq(TokenizedPaymentResponse.class), same(authStrategy));
    }

    @Test
    void processMitPayment_usesIntentionEndpoint() {
        when(httpClient.post(anyString(), any(), eq(TokenizedPaymentResponse.class), eq(authStrategy)))
                .thenReturn(new TokenizedPaymentResponse());

        MitPaymentRequest request = MitPaymentRequest.builder()
                .cardToken("tok_789")
                .amount(2000)
                .currency("EGP")
                .merchantOrderId("order-1")
                .build();

        service.processMitPayment(request);

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).post(eq("/v1/intention/"), any(MitPaymentRequest.class),
                eq(TokenizedPaymentResponse.class), same(authStrategy));
    }
}
