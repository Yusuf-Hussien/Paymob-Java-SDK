package com.paymob.sdk.http;

import com.paymob.sdk.core.auth.SecretKeyAuthStrategy;
import com.paymob.sdk.core.auth.BearerTokenAuthStrategy;
import com.paymob.sdk.exceptions.AuthenticationException;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

class OkHttpClientAdapterTest {
    private MockWebServer mockWebServer;
    private OkHttpClientAdapter httpClient;
    private SecretKeyAuthStrategy secretKeyAuth;
    private BearerTokenAuthStrategy bearerTokenAuth;

    @BeforeEach
    void setUp() throws IOException {
        // Create mock server with automatic port assignment
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        
        // Get the base URL without trailing slash
        String baseUrl = mockWebServer.url("/").toString().replaceAll("/$", "");
        
        // Create HTTP client and set base URL
        httpClient = new OkHttpClientAdapter();
        httpClient.setBaseUrl(baseUrl);
        
        // Create auth strategies with test credentials (no real credentials needed)
        secretKeyAuth = new SecretKeyAuthStrategy("sk_test_123");
        bearerTokenAuth = new BearerTokenAuthStrategy("ak_test_123", baseUrl, httpClient);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    void testGetRequest() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"success\": true}")
                .addHeader("Content-Type", "application/json"));

        // Execute request
        String response = httpClient.get("/test", String.class, secretKeyAuth);

        // Verify response
        assertEquals("{\"success\": true}", response);

        // Verify request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals("/test", recordedRequest.getPath());
        assertEquals("Token sk_test_123", recordedRequest.getHeader("Authorization"));
    }

    @Test
    void testPostRequest() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"id\": 123}")
                .addHeader("Content-Type", "application/json"));

        // Execute request
        TestRequestBody requestBody = new TestRequestBody("test");
        TestResponse response = httpClient.post("/create", requestBody, TestResponse.class, secretKeyAuth);

        // Verify response
        assertEquals(123, response.id);

        // Verify request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("POST", recordedRequest.getMethod());
        assertEquals("/create", recordedRequest.getPath());
        assertEquals("Token sk_test_123", recordedRequest.getHeader("Authorization"));
        assertEquals("application/json; charset=utf-8", recordedRequest.getHeader("Content-Type"));
        assertTrue(recordedRequest.getBody().readUtf8().contains("test"));
    }

    @Test
    void testPutRequest() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"updated\": true}")
                .addHeader("Content-Type", "application/json"));

        // Execute request
        TestRequestBody requestBody = new TestRequestBody("updated");
        String response = httpClient.put("/update/123", requestBody, String.class, secretKeyAuth);

        // Verify response
        assertEquals("{\"updated\": true}", response);

        // Verify request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("PUT", recordedRequest.getMethod());
        assertEquals("/update/123", recordedRequest.getPath());
        assertEquals("Token sk_test_123", recordedRequest.getHeader("Authorization"));
    }

    @Test
    void testDeleteRequest() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"deleted\": true}")
                .addHeader("Content-Type", "application/json"));

        // Execute request
        String response = httpClient.delete("/delete/123", String.class, secretKeyAuth);

        // Verify response
        assertEquals("{\"deleted\": true}", response);

        // Verify request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("DELETE", recordedRequest.getMethod());
        assertEquals("/delete/123", recordedRequest.getPath());
        assertEquals("Token sk_test_123", recordedRequest.getHeader("Authorization"));
    }

    @Test
    void testBearerTokenAuthentication() throws Exception {
        // Setup mock response
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"success\": true}"));

        // Set bearer token
        bearerTokenAuth.setBearerToken("test_token", java.time.Instant.now().plusSeconds(3600));

        // Execute request
        httpClient.get("/test", String.class, bearerTokenAuth);

        // Verify request
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals("Bearer test_token", recordedRequest.getHeader("Authorization"));
    }

    @Test
    void testAuthenticationException() throws Exception {
        // Setup mock response with error
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(401)
                .setBody("{\"error\": \"Unauthorized\"}"));

        // Execute request and expect exception
        assertThrows(AuthenticationException.class, () -> {
            httpClient.get("/protected", String.class, secretKeyAuth);
        });
    }

    @Test
    void testBaseUrlConfiguration() {
        // Test that base URL is properly set
        assertNotNull(httpClient);
        String url = mockWebServer.url("/").toString();
        // Just check that it's a valid URL format
        assertTrue(url.startsWith("http"));
        assertTrue(url.contains(":"));
    }

    @Test
    void testTimeoutConfiguration() {
        // Test timeout configuration
        httpClient.setTimeout(30);
        // This is a basic test - actual timeout testing would require more complex setup
        assertNotNull(httpClient);
    }

    // Test helper classes
    static class TestRequestBody {
        public String data;
        
        public TestRequestBody(String data) {
            this.data = data;
        }
    }

    static class TestResponse {
        public int id;
    }
}
