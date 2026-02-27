package com.paymob.sdk.http.interceptors;

import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RetryInterceptorTest {

    private Interceptor.Chain chain;
    private RetryInterceptor interceptor;
    private Request request;

    @BeforeEach
    void setUp() {
        chain = mock(Interceptor.Chain.class);
        interceptor = new RetryInterceptor();
        request = new Request.Builder().url("https://api.paymob.com/test").build();
        when(chain.request()).thenReturn(request);
    }

    private Response createResponse(int code) {
        return new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message("Message")
                .body(ResponseBody.create("", MediaType.parse("application/json")))
                .build();
    }

    @Test
    void intercept_successOnFirstAttempt_noRetry() throws IOException {
        Response successResponse = createResponse(200);
        when(chain.proceed(any(Request.class))).thenReturn(successResponse);

        Response response = interceptor.intercept(chain);

        assertEquals(200, response.code());
        verify(chain, times(1)).proceed(any(Request.class));
    }

    @Test
    void intercept_fail500ThenSuccess_retries() throws IOException {
        Response errorResponse = createResponse(500);
        Response successResponse = createResponse(200);

        when(chain.proceed(any(Request.class)))
                .thenReturn(errorResponse)
                .thenReturn(successResponse);

        Response response = interceptor.intercept(chain);

        assertEquals(200, response.code());
        verify(chain, times(2)).proceed(any(Request.class));
    }

    @Test
    void intercept_fail400_noRetry() throws IOException {
        Response errorResponse = createResponse(400);

        when(chain.proceed(any(Request.class))).thenReturn(errorResponse);

        Response response = interceptor.intercept(chain);

        assertEquals(400, response.code());
        verify(chain, times(1)).proceed(any(Request.class));
    }

    @Test
    void intercept_ioExceptionThenSuccess_retries() throws IOException {
        Response successResponse = createResponse(200);

        when(chain.proceed(any(Request.class)))
                .thenThrow(new IOException("Timeout"))
                .thenReturn(successResponse);

        Response response = interceptor.intercept(chain);

        assertEquals(200, response.code());
        verify(chain, times(2)).proceed(any(Request.class));
    }

    @Test
    void intercept_maxRetriesExceeded_throwsException() throws IOException {
        when(chain.proceed(any(Request.class)))
                .thenThrow(new IOException("Persistent error"));

        assertThrows(IOException.class, () -> interceptor.intercept(chain));
        verify(chain, times(4)).proceed(any(Request.class)); // 1 initial + 3 retries
    }
}
