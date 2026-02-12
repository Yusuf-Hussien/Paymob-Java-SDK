package com.paymob.sdk.services;

import com.paymob.sdk.client.PaymobClient;
import com.paymob.sdk.models.payments.PaymentKeyRequest;
import com.paymob.sdk.models.payments.PaymentKeyResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymobClient paymobClient;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void requestPaymentKey_shouldReturnToken_whenSuccessful() {
        // Arrange
        PaymentKeyResponse mockResponse = new PaymentKeyResponse();
        mockResponse.setToken("payment_key_token");

        when(paymobClient.post(eq("/acceptance/payment_keys"), any(PaymentKeyRequest.class),
                eq(PaymentKeyResponse.class)))
                .thenReturn(mockResponse);

        // Act
        PaymentKeyRequest request = new PaymentKeyRequest();
        PaymentKeyResponse response = paymentService.requestPaymentKey(request);

        // Assert
        assertNotNull(response);
        assertEquals("payment_key_token", response.getToken());
    }
}
