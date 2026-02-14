package com.paymob.sdk.services;

import com.paymob.sdk.client.PaymobClient;
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
class TransactionServiceTest {

    @Mock
    private PaymobClient paymobClient;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getTransaction_shouldReturnTransactionObject_whenSuccessful() {
        // Arrange
        Object mockTransaction = new Object(); // In real scenario simpler to use map or specific object
        // getTransaction now uses getWithBearerToken
        when(paymobClient.getWithBearerToken(eq("/acceptance/transactions/123"), eq("auth_token"), eq(Object.class)))
                .thenReturn(mockTransaction);

        // Act
        Object response = transactionService.getTransaction(123L, "auth_token");

        // Assert
        assertNotNull(response);
    }

    @Test
    void voidTransaction_shouldReturnObject_whenSuccessful() {
        // Arrange
        Object mockResponse = new Object();
        when(paymobClient.postWithSecretKey(eq("/api/acceptance/void_refund/void"), any(), eq(Object.class)))
                .thenReturn(mockResponse);

        // Act
        Object response = transactionService.voidTransaction(123L);

        // Assert
        assertNotNull(response);
    }

    @Test
    void refundTransaction_shouldReturnObject_whenSuccessful() {
        // Arrange
        Object mockResponse = new Object();
        when(paymobClient.postWithSecretKey(eq("/api/acceptance/void_refund/refund"), any(), eq(Object.class)))
                .thenReturn(mockResponse);

        // Act
        Object response = transactionService.refundTransaction(123L, 100);

        // Assert
        assertNotNull(response);
    }

    @Test
    void captureTransaction_shouldReturnObject_whenSuccessful() {
        // Arrange
        Object mockResponse = new Object();
        when(paymobClient.postWithSecretKey(eq("/api/acceptance/capture"), any(), eq(Object.class)))
                .thenReturn(mockResponse);

        // Act
        Object response = transactionService.captureTransaction(123L, 100);

        // Assert
        assertNotNull(response);
    }
}
