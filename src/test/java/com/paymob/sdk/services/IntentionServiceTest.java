package com.paymob.sdk.services;

import com.paymob.sdk.Paymob;
import com.paymob.sdk.client.PaymobClient;
import com.paymob.sdk.models.Region;
import com.paymob.sdk.models.intention.IntentionRequest;
import com.paymob.sdk.models.intention.IntentionResponse;
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
class IntentionServiceTest {

    @Mock
    private PaymobClient paymobClient;

    @InjectMocks
    private IntentionService intentionService;

    @BeforeAll
    static void setup() {
        Paymob.init("api_key", "secret_key", "public_key", Region.EGYPT);
    }

    @Test
    void createIntention_shouldReturnResponse_whenSuccessful() {
        // Arrange
        IntentionResponse mockResponse = new IntentionResponse();
        mockResponse.setId("pi_123");
        mockResponse.setClientSecret("cs_123");

        when(paymobClient.postWithSecretKey(eq("/v1/intention/"), any(IntentionRequest.class),
                eq(IntentionResponse.class)))
                .thenReturn(mockResponse);

        // Act
        IntentionRequest request = new IntentionRequest();
        IntentionResponse response = intentionService.createIntention(request);

        // Assert
        assertNotNull(response);
        assertEquals("pi_123", response.getId());
        assertEquals("cs_123", response.getClientSecret());
    }

    @Test
    void retrieveIntention_shouldReturnResponse_whenSuccessful() {
        // Arrange
        IntentionResponse mockResponse = new IntentionResponse();
        mockResponse.setId("pi_123");

        // Expected URL: /v1/intention/element/{PublicKey}/{ClientSecret}/
        String expectedEndpoint = "/v1/intention/element/public_key/cs_123/";

        when(paymobClient.get(eq(expectedEndpoint), eq(IntentionResponse.class)))
                .thenReturn(mockResponse);

        // Act
        IntentionResponse response = intentionService.retrieveIntention("cs_123");

        // Assert
        assertNotNull(response);
        assertEquals("pi_123", response.getId());
    }

    @Test
    void getUnifiedCheckoutUrl_shouldReturnCorrectUrl() {
        // Act
        String url = intentionService.getUnifiedCheckoutUrl("cs_123");

        // Assert
        String expectedUrl = "https://accept.paymob.com/unifiedcheckout/?publicKey=public_key&clientSecret=cs_123";
        assertEquals(expectedUrl, url);
    }
}
