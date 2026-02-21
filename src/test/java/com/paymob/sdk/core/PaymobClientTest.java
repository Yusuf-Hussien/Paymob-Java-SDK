package com.paymob.sdk.core;

import com.paymob.sdk.http.HttpClient;
import com.paymob.sdk.services.intention.IntentionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;

class PaymobClientTest {
    @Mock
    private HttpClient mockHttpClient;
    
    private PaymobConfig config;
    private PaymobClient client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        config = PaymobConfig.builder()
                .secretKey("sk_test_123")
                .apiKey("ak_test_123")
                .publicKey("pk_test_123")
                .region(PaymobRegion.EGYPT)
                .build();
        
        client = PaymobClient.builder()
                .config(config)
                .httpClient(mockHttpClient)
                .build();
    }

    @Test
    void testBuilderWithConfig() {
        assertNotNull(client);
        assertEquals(config, client.getConfig());
    }

    @Test
    void testBuilderWithoutConfigThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            PaymobClient.builder().build();
        });
    }

    @Test
    void testGetIntentionService() {
        IntentionService intentionService = client.intentions();
        assertNotNull(intentionService);
    }

    @Test
    void testGetAllServices() {
        assertNotNull(client.transactions());
        assertNotNull(client.inquiry());
        assertNotNull(client.savedCards());
        assertNotNull(client.subscriptions());
        assertNotNull(client.quickLinks());
    }

    @Test
    void testServicesAreSingletons() {
        IntentionService service1 = client.intentions();
        IntentionService service2 = client.intentions();
        
        assertSame(service1, service2);
    }

    @Test
    void testBuilderWithDefaultHttpClient() {
        PaymobClient clientWithDefault = PaymobClient.builder()
                .config(config)
                .build();
        
        assertNotNull(clientWithDefault);
        assertEquals(config, clientWithDefault.getConfig());
    }

    @Test
    void testConfigValues() {
        assertEquals("sk_test_123", config.getSecretKey());
        assertEquals("ak_test_123", config.getApiKey());
        assertEquals("pk_test_123", config.getPublicKey());
        assertEquals(PaymobRegion.EGYPT, config.getRegion());
        assertEquals(Duration.ofSeconds(60), config.getTimeout());
    }

    @Test
    void testBuilderWithTimeout() {
        PaymobConfig configWithTimeout = PaymobConfig.builder()
                .secretKey("sk_test_123")
                .apiKey("ak_test_123")
                .timeout(Duration.ofSeconds(30))
                .build();
        
        PaymobClient clientWithTimeout = PaymobClient.builder()
                .config(configWithTimeout)
                .httpClient(mockHttpClient)
                .build();
        
        assertEquals(Duration.ofSeconds(30), clientWithTimeout.getConfig().getTimeout());
    }

    @Test
    void testBuilderWithAllOptions() {
        PaymobConfig fullConfig = PaymobConfig.builder()
                .secretKey("sk_test_123")
                .apiKey("ak_test_123")
                .publicKey("pk_test_123")
                .hmacSecret("hmac_secret")
                .region(PaymobRegion.KSA)
                .timeout(Duration.ofSeconds(45))
                .enableLogging(true)
                .build();
        
        PaymobClient fullClient = PaymobClient.builder()
                .config(fullConfig)
                .httpClient(mockHttpClient)
                .build();
        
        PaymobConfig actualConfig = fullClient.getConfig();
        assertEquals("sk_test_123", actualConfig.getSecretKey());
        assertEquals("ak_test_123", actualConfig.getApiKey());
        assertEquals("pk_test_123", actualConfig.getPublicKey());
        assertEquals("hmac_secret", actualConfig.getHmacSecret());
        assertEquals(PaymobRegion.KSA, actualConfig.getRegion());
        assertEquals(Duration.ofSeconds(45), actualConfig.getTimeout());
        assertTrue(actualConfig.isEnableLogging());
    }
}
