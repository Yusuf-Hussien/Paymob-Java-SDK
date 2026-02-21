package com.paymob.sdk.core;

import com.paymob.sdk.core.PaymobRegion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

class PaymobConfigTest {
    private PaymobConfig.Builder builder;

    @BeforeEach
    void setUp() {
        builder = PaymobConfig.builder()
                .secretKey("sk_test_123")
                .apiKey("ak_test_123")
                .publicKey("pk_test_123")
                .region(PaymobRegion.EGYPT);
    }

    @Test
    void testBuildValidConfig() {
        PaymobConfig config = builder.build();
        
        assertEquals("sk_test_123", config.getSecretKey());
        assertEquals("ak_test_123", config.getApiKey());
        assertEquals("pk_test_123", config.getPublicKey());
        assertEquals(PaymobRegion.EGYPT, config.getRegion());
        assertEquals(Duration.ofSeconds(60), config.getTimeout());
        assertFalse(config.isEnableLogging());
    }

    @Test
    void testBuildWithAllOptions() {
        PaymobConfig config = builder
                .hmacSecret("hmac_secret")
                .timeout(Duration.ofSeconds(30))
                .enableLogging(true)
                .build();
        
        assertEquals("hmac_secret", config.getHmacSecret());
        assertEquals(Duration.ofSeconds(30), config.getTimeout());
        assertTrue(config.isEnableLogging());
    }

    @Test
    void testBuildWithoutSecretKeyThrowsException() {
        PaymobConfig.Builder invalidBuilder = PaymobConfig.builder()
                .apiKey("ak_test_123")
                .region(PaymobRegion.EGYPT);
        
        assertThrows(IllegalArgumentException.class, invalidBuilder::build);
    }

    @Test
    void testDifferentRegions() {
        PaymobConfig egyptConfig = builder.region(PaymobRegion.EGYPT).build();
        PaymobConfig ksaConfig = builder.region(PaymobRegion.KSA).build();
        PaymobConfig uaeConfig = builder.region(PaymobRegion.UAE).build();
        PaymobConfig omanConfig = builder.region(PaymobRegion.OMAN).build();
        
        assertEquals(PaymobRegion.EGYPT, egyptConfig.getRegion());
        assertEquals(PaymobRegion.KSA, ksaConfig.getRegion());
        assertEquals(PaymobRegion.UAE, uaeConfig.getRegion());
        assertEquals(PaymobRegion.OMAN, omanConfig.getRegion());
    }

    @Test
    void testDefaultValues() {
        PaymobConfig config = PaymobConfig.builder()
                .secretKey("sk_test_123")
                .build();
        
        assertEquals(PaymobRegion.EGYPT, config.getRegion());
        assertEquals(Duration.ofSeconds(60), config.getTimeout());
        assertFalse(config.isEnableLogging());
        assertNull(config.getApiKey());
        assertNull(config.getPublicKey());
        assertNull(config.getHmacSecret());
    }
}
