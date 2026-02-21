package com.paymob.sdk.core.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class SecretKeyAuthStrategyTest {
    private SecretKeyAuthStrategy authStrategy;
    private static final String SECRET_KEY = "sk_test_123456789";

    @BeforeEach
    void setUp() {
        authStrategy = new SecretKeyAuthStrategy(SECRET_KEY);
    }

    @Test
    void testConstructor() {
        assertEquals(SECRET_KEY, authStrategy.getSecretKey());
    }

    @Test
    void testGetType() {
        assertEquals("SECRET_KEY", authStrategy.getType());
    }

    @Test
    void testGetAuthorizationHeader() {
        String expected = "Token " + SECRET_KEY;
        assertEquals(expected, authStrategy.getAuthorizationHeader());
    }

    @Test
    void testGetAuthorizationHeaderFormat() {
        String header = authStrategy.getAuthorizationHeader();
        assertTrue(header.startsWith("Token "));
        assertTrue(header.contains(SECRET_KEY));
        assertEquals("Token " + SECRET_KEY, header);
    }

    @Test
    void testDifferentSecretKeys() {
        String key1 = "sk_test_abc";
        String key2 = "sk_live_xyz";
        
        SecretKeyAuthStrategy auth1 = new SecretKeyAuthStrategy(key1);
        SecretKeyAuthStrategy auth2 = new SecretKeyAuthStrategy(key2);
        
        assertEquals(key1, auth1.getSecretKey());
        assertEquals(key2, auth2.getSecretKey());
        
        assertEquals("Token " + key1, auth1.getAuthorizationHeader());
        assertEquals("Token " + key2, auth2.getAuthorizationHeader());
    }

    @Test
    void testEmptySecretKey() {
        String emptyKey = "";
        SecretKeyAuthStrategy auth = new SecretKeyAuthStrategy(emptyKey);
        
        assertEquals(emptyKey, auth.getSecretKey());
        assertEquals("Token ", auth.getAuthorizationHeader());
    }

    @Test
    void testNullSecretKey() {
        assertThrows(NullPointerException.class, () -> {
            new SecretKeyAuthStrategy(null);
        });
    }
}
