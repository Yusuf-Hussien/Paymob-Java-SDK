package com.paymob.sdk.services;

import com.paymob.sdk.Paymob;
import com.paymob.sdk.client.PaymobClient;
import com.paymob.sdk.models.auth.AuthRequest;
import com.paymob.sdk.models.auth.AuthResponse;
import org.junit.jupiter.api.BeforeAll;
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
class AuthServiceTest {

    @Mock
    private PaymobClient paymobClient;

    @InjectMocks
    private AuthService authService;

    @BeforeAll
    static void setup() {
        Paymob.init("test_api_key");
    }

    @Test
    void authenticate_shouldReturnToken_whenSuccessful() {
        // Arrange
        AuthResponse mockResponse = new AuthResponse();
        mockResponse.setToken("mock_token");

        when(paymobClient.post(eq("/auth/tokens"), any(AuthRequest.class), eq(AuthResponse.class)))
                .thenReturn(mockResponse);

        // Act
        AuthResponse response = authService.authenticate();

        // Assert
        assertNotNull(response);
        assertEquals("mock_token", response.getToken());
    }
}
