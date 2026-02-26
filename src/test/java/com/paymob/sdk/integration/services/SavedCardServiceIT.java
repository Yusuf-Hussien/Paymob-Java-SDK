package com.paymob.sdk.integration.services;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.exceptions.PaymobException;
import com.paymob.sdk.services.savedcard.CitPaymentRequest;
import com.paymob.sdk.services.savedcard.MitPaymentRequest;
import com.paymob.sdk.testutil.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class SavedCardServiceIT {
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        client = IntegrationTestConfig.createClientFromEnv();
    }

    @Test
    void testCitPaymentWithInvalidToken() {
        CitPaymentRequest request = CitPaymentRequest.builder()
                .cardToken("invalid_token")
                .cvv("123")
                .amount(1000)
                .currency("EGP")
                .build();

        PaymobException exception = assertThrows(PaymobException.class, () -> {
            client.savedCards().processCitPayment(request);
        });

        assertNotNull(exception.getMessage());
    }

    @Test
    void testMitPaymentWithInvalidToken() {
        MitPaymentRequest request = MitPaymentRequest.builder()
                .cardToken("invalid_token")
                .amount(1000)
                .currency("EGP")
                .merchantOrderId("MST-" + System.currentTimeMillis())
                .build();

        PaymobException exception = assertThrows(PaymobException.class, () -> {
            client.savedCards().processMitPayment(request);
        });

        assertNotNull(exception.getMessage());
    }
}
