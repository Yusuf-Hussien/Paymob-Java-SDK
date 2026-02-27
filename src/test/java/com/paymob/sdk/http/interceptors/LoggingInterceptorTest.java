package com.paymob.sdk.http.interceptors;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

class LoggingInterceptorTest {

    @Test
    void intercept_delegatesToHttpLoggingInterceptor() throws IOException {
        Interceptor.Chain chain = mock(Interceptor.Chain.class);
        Request request = new Request.Builder().url("https://api.paymob.com/test").build();
        Response response = new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(ResponseBody.create("{}", MediaType.parse("application/json")))
                .build();

        when(chain.request()).thenReturn(request);
        when(chain.proceed(any(Request.class))).thenReturn(response);
        when(chain.connection()).thenReturn(mock(Connection.class));

        LoggingInterceptor interceptor = new LoggingInterceptor(HttpLoggingInterceptor.Level.BODY);
        interceptor.intercept(chain);

        verify(chain).proceed(any(Request.class));
    }
}
