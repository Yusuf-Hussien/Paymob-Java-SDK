package com.paymob.sdk.services.inquiry;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TransactionInquiryServiceTest {

    private HttpClient httpClient;
    private AuthStrategy authStrategy;
    private PaymobConfig config;
    private TransactionInquiryService service;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        authStrategy = mock(AuthStrategy.class);
        config = PaymobConfig.builder()
                .apiKey("test-api-key")
                .secretKey("test-secret-key")
                .region(PaymobRegion.EGYPT)
                .build();
        service = new TransactionInquiryService(httpClient, authStrategy, config);
    }

    @Test
    void byOrderId_usesCorrectEndpointAndBearerAuth() {
        service.byOrderId(123);

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).post(eq("/api/ecommerce/orders/transaction_inquiry"), any(InquiryRequest.class), eq(InquiryResponse.class), same(authStrategy));
    }

    @Test
    void byMerchantOrderId_usesCorrectEndpointAndBearerAuth() {
        service.byMerchantOrderId("my-ref");

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).post(eq("/api/ecommerce/orders/transaction_inquiry"), any(InquiryRequest.class), eq(InquiryResponse.class), same(authStrategy));
    }

    @Test
    void byTransactionId_usesCorrectEndpointAndBearerAuth() {
        service.byTransactionId(456L);

        verify(httpClient).setBaseUrl(PaymobRegion.EGYPT.getBaseUrl());
        verify(httpClient).get(eq("/api/acceptance/transactions/456"), eq(InquiryResponse.class), same(authStrategy));
    }
}
