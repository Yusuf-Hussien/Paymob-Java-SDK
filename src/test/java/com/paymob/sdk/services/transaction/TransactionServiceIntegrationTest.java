package com.paymob.sdk.services.transaction;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.exceptions.PaymobException;
import com.paymob.sdk.exceptions.ResourceNotFoundException;
import com.paymob.sdk.utils.TestConfigUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
class TransactionServiceIntegrationTest {
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        client = TestConfigUtils.createClientFromEnv();
    }

    @Test
    void testRefundTransactionNotFound() {
        RefundRequest request = RefundRequest.builder()
                .transactionId(1L)
                .amountCents(100)
                .build();

        // Refund returns 422 for invalid IDs
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

        // Void returns 422 for invalid IDs
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

        // Capture returns 404 for invalid IDs
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            client.transactions().captureTransaction(request);
        });

        assertNotNull(exception.getErrorBody());
    }
}
