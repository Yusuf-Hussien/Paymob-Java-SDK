package com.paymob.sdk.http;

import com.paymob.sdk.exceptions.*;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResponseHandlerTest {

    private ResponseHandler handler;

    @BeforeEach
    void setUp() {
        handler = new ResponseHandler();
    }

    private Response createResponse(int code, String bodyContent) {
        Request request = new Request.Builder().url("https://api.paymob.com/test").build();
        return new Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message("Message")
                .body(ResponseBody.create(bodyContent, MediaType.parse("application/json")))
                .build();
    }

    @Test
    void handleSuccess_withStringClass_returnsRawBody() throws IOException {
        Response response = createResponse(200, "raw text");
        String result = handler.handleSuccess(response, String.class);
        assertEquals("raw text", result);
    }

    @Test
    void handleSuccess_withDtoClass_parsesJson() throws IOException {
        Response response = createResponse(200, "{\"id\":123}");
        TestDto result = handler.handleSuccess(response, TestDto.class);
        assertEquals(123, result.id);
    }

    @Test
    void handleError_mapsCorrectExceptions() throws IOException {
        assertThrows(AuthenticationException.class, () -> {
            throw handler.handleError(createResponse(401, "unauthorized"));
        });
        assertThrows(ResourceNotFoundException.class, () -> {
            throw handler.handleError(createResponse(404, "not found"));
        });
        assertThrows(ValidationException.class, () -> {
            throw handler.handleError(createResponse(406, "invalid"));
        });
        assertThrows(PaymobServerException.class, () -> {
            throw handler.handleError(createResponse(500, "error"));
        });
        assertThrows(PaymobException.class, () -> {
            throw handler.handleError(createResponse(418, "teapot"));
        });
    }

    @Test
    void isTimeout_checksCodesAndMessages() {
        assertTrue(handler.isTimeout(createResponse(408, "")));
        assertTrue(handler.isTimeout(createResponse(429, "")));

        Response timeoutMsgResponse = new Response.Builder()
                .request(new Request.Builder().url("https://t.co").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("Request Timeout")
                .build();
        assertTrue(handler.isTimeout(timeoutMsgResponse));
    }

    static class TestDto {
        public int id;
    }
}
