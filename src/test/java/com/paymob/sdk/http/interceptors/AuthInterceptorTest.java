package com.paymob.sdk.http.interceptors;

import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.core.auth.BearerTokenAuthStrategy;
import com.paymob.sdk.core.auth.SecretKeyAuthStrategy;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class AuthInterceptorTest {

    private Interceptor.Chain chain;
    private AuthInterceptor interceptor;

    @BeforeEach
    void setUp() {
        chain = mock(Interceptor.Chain.class);
        interceptor = new AuthInterceptor();
    }

    @Test
    void intercept_withSecretKeyAuth_addsTokenHeader() throws IOException {
        AuthStrategy strategy = new SecretKeyAuthStrategy("sk_123");
        interceptor.setAuthStrategy(strategy);

        Request request = new Request.Builder().url("https://api.paymob.com/test").build();
        when(chain.request()).thenReturn(request);
        when(chain.proceed(any(Request.class))).thenReturn(mock(Response.class));

        interceptor.intercept(chain);

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(chain).proceed(requestCaptor.capture());

        assertEquals("Token sk_123", requestCaptor.getValue().header("Authorization"));
    }

    @Test
    void intercept_withBearerTokenAuth_addsBearerHeader() throws IOException {
        BearerTokenAuthStrategy strategy = mock(BearerTokenAuthStrategy.class);
        when(strategy.getAuthorizationHeader()).thenReturn("Bearer token_abc");
        interceptor.setAuthStrategy(strategy);

        Request request = new Request.Builder().url("https://api.paymob.com/test").build();
        when(chain.request()).thenReturn(request);
        when(chain.proceed(any(Request.class))).thenReturn(mock(Response.class));

        interceptor.intercept(chain);

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(chain).proceed(requestCaptor.capture());

        assertEquals("Bearer token_abc", requestCaptor.getValue().header("Authorization"));
    }

    @Test
    void intercept_withNullAuth_doesNotAddHeader() throws IOException {
        interceptor.setAuthStrategy(null);

        Request request = new Request.Builder().url("https://api.paymob.com/test").build();
        when(chain.request()).thenReturn(request);
        when(chain.proceed(any(Request.class))).thenReturn(mock(Response.class));

        interceptor.intercept(chain);

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(chain).proceed(requestCaptor.capture());

        assertNull(requestCaptor.getValue().header("Authorization"));
    }
}
