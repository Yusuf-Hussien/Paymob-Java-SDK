package com.paymob.sdk.services.transaction;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    private HttpClient httpClient;
    private AuthStrategy authStrategy;
    private PaymobConfig config;
    private TransactionService service;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        authStrategy = mock(AuthStrategy.class);
        config = PaymobConfig.builder()
                .secretKey("sk_test")
                .apiKey("ak_test")
                .region(PaymobRegion.EGYPT)
                .build();
        service = new TransactionService(httpClient, authStrategy, config);
    }

    @Test
    void refundTransaction_usesCorrectEndpoint() {
        when(httpClient.post(anyString(), any(), eq(TransactionResponse.class), eq(authStrategy)))
                .thenReturn(new TransactionResponse());

        RefundRequest request = RefundRequest.builder()
                .transactionId(100L)
                .amountCents(500)
                .build();

        service.refundTransaction(request);

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).post(eq("/api/acceptance/void_refund/refund"),
                any(RefundRequest.class), eq(TransactionResponse.class), same(authStrategy));
    }

    @Test
    void voidTransaction_usesCorrectEndpoint() {
        when(httpClient.post(anyString(), any(), eq(TransactionResponse.class), eq(authStrategy)))
                .thenReturn(new TransactionResponse());

        VoidRequest request = VoidRequest.builder()
                .transactionId(200L)
                .build();

        service.voidTransaction(request);

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).post(eq("/api/acceptance/void_refund/void"),
                any(VoidRequest.class), eq(TransactionResponse.class), same(authStrategy));
    }

    @Test
    void captureTransaction_usesCorrectEndpoint() {
        when(httpClient.post(anyString(), any(), eq(TransactionResponse.class), eq(authStrategy)))
                .thenReturn(new TransactionResponse());

        CaptureRequest request = CaptureRequest.builder()
                .transactionId(300L)
                .amountCents(7500)
                .build();

        service.captureTransaction(request);

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).post(eq("/api/acceptance/capture"),
                any(CaptureRequest.class), eq(TransactionResponse.class), same(authStrategy));
    }
}
