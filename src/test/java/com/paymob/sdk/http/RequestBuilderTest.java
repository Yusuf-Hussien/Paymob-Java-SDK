package com.paymob.sdk.http;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okio.Buffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RequestBuilderTest {

    private OkHttpClient client;
    private RequestBuilder builder;

    @BeforeEach
    void setUp() {
        client = mock(OkHttpClient.class);
        builder = new RequestBuilder(client);
    }

    @Test
    void build_getWithHeaders() throws Exception {
        Request request = builder
                .baseUrl("https://api.com")
                .get("/v1/test")
                .headers(Collections.singletonMap("X-Test", "Value"))
                .build();

        assertEquals("https://api.com/v1/test", request.url().toString());
        assertEquals("GET", request.method());
        assertEquals("Value", request.header("X-Test"));
    }

    @Test
    void build_postWithJsonBody() throws Exception {
        TestBody bodyObj = new TestBody("test-name");
        Request request = builder
                .baseUrl("https://api.com")
                .post("/v1/create")
                .body(bodyObj)
                .build();

        assertEquals("POST", request.method());
        assertEquals("application/json; charset=utf-8", request.body().contentType().toString());

        Buffer buffer = new Buffer();
        request.body().writeTo(buffer);
        assertEquals("{\"name\":\"test-name\"}", buffer.readUtf8());
    }

    @Test
    void build_missingBaseUrl_throwsException() {
        builder.get("/test");
        assertThrows(IllegalArgumentException.class, () -> builder.build());
    }

    @Test
    void build_missingEndpoint_throwsException() {
        builder.baseUrl("https://api.com");
        assertThrows(IllegalArgumentException.class, () -> builder.build());
    }

    @Test
    void build_missingMethod_throwsException() {
        builder.baseUrl("https://api.com");
        // No method/endpoint set
        assertThrows(IllegalArgumentException.class, () -> builder.build());
    }

    static class TestBody {
        public String name;

        public TestBody(String name) {
            this.name = name;
        }
    }
}
