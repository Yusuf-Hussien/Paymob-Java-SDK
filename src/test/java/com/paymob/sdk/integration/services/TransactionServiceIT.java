package com.paymob.sdk.integration.services;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.exceptions.PaymobException;
import com.paymob.sdk.exceptions.ResourceNotFoundException;
import com.paymob.sdk.services.transaction.CaptureRequest;
import com.paymob.sdk.services.transaction.RefundRequest;
import com.paymob.sdk.services.transaction.VoidRequest;
import com.paymob.sdk.testutil.IntegrationTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class TransactionServiceIT {
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        client = IntegrationTestConfig.createClientFromEnv();
    }

    @Test
    void testRefundTransactionNotFound() {
        RefundRequest request = RefundRequest.builder()
                .transactionId(1L)
                .amountCents(100)
                .build();

        PaymobException exception = assertThrows(PaymobException.class, () -> {
            client.transactions().refundTransaction(request);
        });

        assertNotNull(exception.getErrorBody());
    }

    @Test
    void testVoidTransactionNotFound() {
        VoidRequest request = VoidRequest.builder()
                .transactionId(1L)
                .build();

        PaymobException exception = assertThrows(PaymobException.class, () -> {
            client.transactions().voidTransaction(request);
        });

        assertNotNull(exception.getErrorBody());
    }

    @Test
    void testCaptureTransactionNotFound() {
        CaptureRequest request = CaptureRequest.builder()
                .transactionId(1L)
                .amountCents(100)
                .build();

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            client.transactions().captureTransaction(request);
        });

        assertNotNull(exception.getErrorBody());
    }
}
