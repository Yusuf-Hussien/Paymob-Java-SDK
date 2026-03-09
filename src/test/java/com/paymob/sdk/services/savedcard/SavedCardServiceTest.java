package com.paymob.sdk.services.savedcard;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;
import com.paymob.sdk.services.transaction.TransactionResponse;

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

        @Test
        void executeMotoPayment_usesPayEndpointWithoutAuthHeader() {
                when(httpClient.post(anyString(), any(), eq(TransactionResponse.class), isNull()))
                                .thenReturn(new TransactionResponse());

                MotoCardPayRequest request = new MotoCardPayRequest("tok_456", "payment_token_123");
                service.executeMotoPayment(request);

                verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
                verify(httpClient).post(eq("/api/acceptance/payments/pay"), any(MotoCardPayRequest.class),
                                eq(TransactionResponse.class), isNull());
        }
}
