package com.paymob.sdk.services;

import com.paymob.sdk.client.PaymobClient;
import com.paymob.sdk.models.orders.OrderRequest;
import com.paymob.sdk.models.orders.OrderResponse;
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
class OrderServiceTest {

    @Mock
    private PaymobClient paymobClient;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_shouldReturnOrder_whenSuccessful() {
        // Arrange
        OrderResponse mockResponse = new OrderResponse();
        mockResponse.setId(12345L);
        mockResponse.setMerchantOrderId("ORD-001");

        when(paymobClient.post(eq("/ecommerce/orders"), any(OrderRequest.class), eq(OrderResponse.class)))
                .thenReturn(mockResponse);

        // Act
        OrderResponse response = orderService.createOrder("auth_token", "1000", "EGP", "ORD-001");

        // Assert
        assertNotNull(response);
        assertEquals(12345L, response.getId());
        assertEquals("ORD-001", response.getMerchantOrderId());
    }
}
