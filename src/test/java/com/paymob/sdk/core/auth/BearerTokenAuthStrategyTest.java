package com.paymob.sdk.core.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import com.paymob.sdk.http.HttpClient;

class BearerTokenAuthStrategyTest {
    private BearerTokenAuthStrategy authStrategy;
    private static final String API_KEY = "ak_test_123456789";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    private static final String BASE_URL = "https://accept.paymob.com";
    private HttpClient httpClient;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        authStrategy = new BearerTokenAuthStrategy(API_KEY, BASE_URL, httpClient);
    }

    @Test
    void testConstructor() {
        assertEquals(API_KEY, authStrategy.getApiKey());
        assertNull(authStrategy.getBearerToken());
        assertNull(authStrategy.getTokenExpiry());
        assertTrue(authStrategy.isTokenExpired());
    }

    @Test
    void testGetType() {
        assertEquals("BEARER_TOKEN", authStrategy.getType());
    }

    @Test
    void testSetBearerToken() {
        Instant future = Instant.now().plusSeconds(3600); // 1 hour from now
        authStrategy.setBearerToken(TOKEN, future);
        
        assertEquals(TOKEN, authStrategy.getBearerToken());
        assertEquals(future, authStrategy.getTokenExpiry());
        assertFalse(authStrategy.isTokenExpired());
    }

    @Test
    void testGetAuthorizationHeader() {
        Instant future = Instant.now().plusSeconds(3600);
        authStrategy.setBearerToken(TOKEN, future);
        
        String expected = "Bearer " + TOKEN;
        assertEquals(expected, authStrategy.getAuthorizationHeader());
    }

    @Test
    void testTokenExpired() {
        // Test with expired token
        Instant past = Instant.now().minusSeconds(3600); // 1 hour ago
        authStrategy.setBearerToken(TOKEN, past);
        
        assertTrue(authStrategy.isTokenExpired());
    }

    @Test
    void testTokenNearExpiry() {
        // Test with token expiring soon (within 5 minutes buffer)
        Instant nearFuture = Instant.now().plusSeconds(200); // ~3.3 minutes from now
        authStrategy.setBearerToken(TOKEN, nearFuture);
        
        assertTrue(authStrategy.isTokenExpired()); // Should be expired due to buffer
    }

    @Test
    void testTokenValid() {
        // Test with valid token (more than 5 minutes buffer)
        Instant validFuture = Instant.now().plusSeconds(600); // 10 minutes from now
        authStrategy.setBearerToken(TOKEN, validFuture);
        
        assertFalse(authStrategy.isTokenExpired());
    }

    @Test
    void testNoTokenSet() {
        assertTrue(authStrategy.isTokenExpired());

        BearerTokenAuthStrategy.TokenResponse tokenResponse = new BearerTokenAuthStrategy.TokenResponse();
        tokenResponse.token = TOKEN;
        when(httpClient.post(eq("/api/auth/tokens"), any(), eq(BearerTokenAuthStrategy.TokenResponse.class), isNull()))
                .thenReturn(tokenResponse);

        assertEquals("Bearer " + TOKEN, authStrategy.getAuthorizationHeader());
    }

    @Test
    void testConcurrentAccess() throws InterruptedException {
        Instant future = Instant.now().plusSeconds(3600);
        
        // Test thread safety with concurrent access
        Thread t1 = new Thread(() -> authStrategy.setBearerToken(TOKEN + "_1", future));
        Thread t2 = new Thread(() -> authStrategy.setBearerToken(TOKEN + "_2", future));
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        // Should have one of the tokens (order not guaranteed)
        String token = authStrategy.getBearerToken();
        assertTrue(token.equals(TOKEN + "_1") || token.equals(TOKEN + "_2"));
    }

    @Test
    void testNullApiKey() {
        assertThrows(NullPointerException.class, () -> {
            new BearerTokenAuthStrategy(null, BASE_URL, httpClient);
        });
    }

    @Test
    void testEmptyApiKey() {
        String emptyKey = "";
        BearerTokenAuthStrategy auth = new BearerTokenAuthStrategy(emptyKey, BASE_URL, httpClient);
        
        assertEquals(emptyKey, auth.getApiKey());
        assertEquals("BEARER_TOKEN", auth.getType());
    }
}
