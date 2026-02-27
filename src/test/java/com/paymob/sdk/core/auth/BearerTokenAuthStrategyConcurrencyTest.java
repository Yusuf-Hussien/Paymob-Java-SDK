package com.paymob.sdk.core.auth;

import com.paymob.sdk.http.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BearerTokenAuthStrategyConcurrencyTest {

    private HttpClient httpClient;
    private BearerTokenAuthStrategy strategy;
    private String apiKey = "test_api_key";
    private String baseUrl = "https://api.test";

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        strategy = new BearerTokenAuthStrategy(apiKey, baseUrl, httpClient);
    }

    @Test
    void getAuthorizationHeader_withConcurrentAccess_fetchesTokenOnlyOnce() throws Exception {
        // Prepare response with a slight delay to trigger concurrency issues if locking
        // fails
        BearerTokenAuthStrategy.TokenResponse response = new BearerTokenAuthStrategy.TokenResponse();
        response.token = "new_mock_token";

        when(httpClient.post(eq("/api/auth/tokens"), any(), eq(BearerTokenAuthStrategy.TokenResponse.class), isNull()))
                .thenAnswer((Answer<BearerTokenAuthStrategy.TokenResponse>) invocation -> {
                    Thread.sleep(100); // Simulate network latency
                    return response;
                });

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                latch.await(); // Wait for signal to start all at once
                return strategy.getAuthorizationHeader();
            }));
        }

        latch.countDown(); // Start all threads simultaneously

        for (Future<String> future : futures) {
            String header = future.get();
            assertEquals("Bearer new_mock_token", header);
        }

        // Verify that HTTP login was called exactly once despite 10 concurrent requests
        verify(httpClient, times(1)).post(anyString(), any(), any(), any());

        executor.shutdown();
    }
}
