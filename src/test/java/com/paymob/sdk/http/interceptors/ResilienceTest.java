package com.paymob.sdk.http.interceptors;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ResilienceTest {

    private RetryInterceptor interceptor;
    private Interceptor.Chain chain;
    private Request request;

    @BeforeEach
    void setUp() {
        interceptor = new RetryInterceptor();
        chain = mock(Interceptor.Chain.class);
        request = new Request.Builder().url("https://api.paymob.com/test").build();
        when(chain.request()).thenReturn(request);
    }

    @Test
    void intercept_retriesOnSocketTimeout() throws IOException {
        AtomicInteger attempts = new AtomicInteger(0);

        when(chain.proceed(any(Request.class))).thenAnswer(invocation -> {
            if (attempts.incrementAndGet() <= 2) {
                throw new SocketTimeoutException("timeout");
            }
            return new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(200)
                    .message("OK")
                    .body(ResponseBody.create("{}", MediaType.parse("application/json")))
                    .build();
        });

        Response response = interceptor.intercept(chain);

        assertEquals(200, response.code());
        assertEquals(3, attempts.get(), "Should have attempted 3 times (2 failures + 1 success)");
        verify(chain, times(3)).proceed(any(Request.class));
    }

    @Test
    void intercept_retriesOn503ServiceUnavailable() throws IOException {
        AtomicInteger attempts = new AtomicInteger(0);

        when(chain.proceed(any(Request.class))).thenAnswer(invocation -> {
            int attempt = attempts.incrementAndGet();
            int statusCode = (attempt <= 2) ? 503 : 200;
            return new Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(statusCode)
                    .message(statusCode == 503 ? "Service Unavailable" : "OK")
                    .body(ResponseBody.create("{}", MediaType.parse("application/json")))
                    .build();
        });

        Response response = interceptor.intercept(chain);

        assertEquals(200, response.code());
        assertEquals(3, attempts.get());
        verify(chain, times(3)).proceed(any(Request.class));
    }

    @Test
    void intercept_exhaustsRetries_throwsLastException() throws IOException {
        when(chain.proceed(any(Request.class))).thenThrow(new SocketTimeoutException("persistent timeout"));

        assertThrows(SocketTimeoutException.class, () -> {
            interceptor.intercept(chain);
        });

        // 1 initial attempt + 3 retries = 4 total
        verify(chain, times(4)).proceed(any(Request.class));
    }

    @Test
    void intercept_doesNotRetryOn401Unauthorized() throws IOException {
        when(chain.proceed(any(Request.class))).thenReturn(new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(401)
                .message("Unauthorized")
                .body(ResponseBody.create("{}", MediaType.parse("application/json")))
                .build());

        Response response = interceptor.intercept(chain);

        assertEquals(401, response.code());
        verify(chain, times(1)).proceed(any(Request.class));
    }
}
