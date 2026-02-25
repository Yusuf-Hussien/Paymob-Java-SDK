package com.paymob.sdk.services.quicklink;

import com.paymob.sdk.models.enums.Currency;
import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import com.paymob.sdk.core.auth.AuthStrategy;
import com.paymob.sdk.http.HttpClient;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class QuickLinkServiceTest {

    private HttpClient httpClient;
    private AuthStrategy authStrategy;
    private PaymobConfig config;
    private QuickLinkService service;

    @BeforeEach
    void setUp() {
        httpClient = mock(HttpClient.class);
        authStrategy = mock(AuthStrategy.class);
        config = PaymobConfig.builder()
                .secretKey("sk_test_123")
                .apiKey("ak_test_123")
                .region(PaymobRegion.EGYPT)
                .build();
        service = new QuickLinkService(httpClient, authStrategy, config);
    }

    @Test
    void createPaymentLink_usesMultipartFormAndCorrectEndpoint() {
        when(httpClient.post(anyString(), any(), eq(QuickLinkResponse.class), eq(authStrategy)))
                .thenReturn(new QuickLinkResponse());

        QuickLinkRequest request = new QuickLinkRequest(10000, 12345, Currency.EGP, false);
        request.setFullName("John Doe");
        request.setCustomerEmail("john@example.com");
        request.setCustomerPhone("+201234567890");
        request.setDescription("Order payment");
        request.setReferenceId("order-123");
        request.setNotificationUrl("https://merchant.com/callback");
        request.setExpiresAt("2026-07-25T18:58:57");

        service.createPaymentLink(request);

        verify(httpClient).setBaseUrl("https://accept.paymob.com");

        ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(httpClient).post(eq("/api/ecommerce/payment-links"), bodyCaptor.capture(), eq(QuickLinkResponse.class),
                eq(authStrategy));

        Object body = bodyCaptor.getValue();
        assertInstanceOf(RequestBody.class, body);
        assertInstanceOf(MultipartBody.class, body);

        MultipartBody multipartBody = (MultipartBody) body;
        List<MultipartBody.Part> parts = multipartBody.parts();
        assertTrue(parts.stream().anyMatch(p -> headerContainsName(p, "amount_cents")));
        assertTrue(parts.stream().anyMatch(p -> headerContainsName(p, "payment_methods")));
        assertTrue(parts.stream().anyMatch(p -> headerContainsName(p, "is_live")));
        assertTrue(parts.stream().anyMatch(p -> headerContainsName(p, "full_name")));
        assertTrue(parts.stream().anyMatch(p -> headerContainsName(p, "email")));
        assertTrue(parts.stream().anyMatch(p -> headerContainsName(p, "phone_number")));
        assertTrue(parts.stream().anyMatch(p -> headerContainsName(p, "description")));
        assertTrue(parts.stream().anyMatch(p -> headerContainsName(p, "reference_id")));
        assertTrue(parts.stream().anyMatch(p -> headerContainsName(p, "notification_url")));
        assertTrue(parts.stream().anyMatch(p -> headerContainsName(p, "expires_at")));
    }

    @Test
    void cancel_usesMultipartFormAndCorrectEndpoint() {
        when(httpClient.post(anyString(), any(), eq(QuickLinkResponse.class), eq(authStrategy)))
                .thenReturn(new QuickLinkResponse());

        service.cancel(123);

        verify(httpClient).setBaseUrl("https://accept.paymob.com");

        ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(httpClient).post(eq("/api/ecommerce/payment-links/cancel"), bodyCaptor.capture(),
                eq(QuickLinkResponse.class), eq(authStrategy));

        Object body = bodyCaptor.getValue();
        assertInstanceOf(MultipartBody.class, body);

        MultipartBody multipartBody = (MultipartBody) body;
        assertTrue(multipartBody.parts().stream().anyMatch(p -> headerContainsName(p, "payment_link_id")));
    }

    private static boolean headerContainsName(MultipartBody.Part part, String name) {
        if (part.headers() == null) {
            return false;
        }
        String cd = part.headers().get("Content-Disposition");
        return cd != null && cd.contains("name=\"" + name + "\"");
    }
}
