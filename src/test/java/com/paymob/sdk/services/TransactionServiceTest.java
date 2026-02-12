package com.paymob.sdk.services;

import com.paymob.sdk.client.PaymobClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private PaymobClient paymobClient;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getTransaction_shouldReturnTransactionObject_whenSuccessful() {
        // Arrange
        Object mockTransaction = new Object(); // In real scenario simpler to use map or specific object

        when(paymobClient.get(eq("/acceptance/transactions/123"), eq(Object.class)))
                .thenReturn(mockTransaction);

        // Act
        Object response = transactionService.getTransaction(123L, "auth_token");

        // Assert
        assertNotNull(response);
    }
}
