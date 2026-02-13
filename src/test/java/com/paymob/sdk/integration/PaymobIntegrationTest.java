package com.paymob.sdk.integration;

import com.paymob.sdk.Paymob;
import com.paymob.sdk.models.auth.AuthResponse;
import com.paymob.sdk.models.orders.OrderResponse;
import com.paymob.sdk.models.payments.PaymentKeyRequest;
import com.paymob.sdk.models.payments.PaymentKeyResponse;
import com.paymob.sdk.services.AuthService;
import com.paymob.sdk.services.OrderService;
import com.paymob.sdk.services.PaymentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import static org.junit.jupiter.api.Assertions.*;

@EnabledIfEnvironmentVariable(named = "PAYMOB_API_KEY", matches = ".*")
@EnabledIfEnvironmentVariable(named = "PAYMOB_INTEGRATION_ID", matches = ".*")
class PaymobIntegrationTest {

    private static String apiKey;
    private static int integrationId;

    @BeforeAll
    static void setup() {
        apiKey = System.getenv("PAYMOB_API_KEY");
        String integrationIdStr = System.getenv("PAYMOB_INTEGRATION_ID");
        if (integrationIdStr != null) {
            integrationId = Integer.parseInt(integrationIdStr);
        }
        Paymob.init(apiKey);
    }

    @Test
    void testFullPaymentFlow() {
        // 1. Authenticate
        AuthService authService = new AuthService();
        AuthResponse authResponse = authService.authenticate();
        assertNotNull(authResponse);
        String token = authResponse.getToken();
        assertNotNull(token, "Auth token should not be null");
        System.out.println("Authenticated: Token received.");

        // 2. Create Order
        OrderService orderService = new OrderService();
        String merchantOrderId = "IT_" + System.currentTimeMillis();
        OrderResponse orderResponse = orderService.createOrder(token, "1000", "EGP", merchantOrderId);
        assertNotNull(orderResponse);
        assertTrue(orderResponse.getId() > 0, "Order ID should be positive");
        System.out.println("Order Created: ID " + orderResponse.getId());

        // 3. Request Payment Key
        PaymentService paymentService = new PaymentService();
        PaymentKeyRequest keyRequest = new PaymentKeyRequest();
        keyRequest.setAuthToken(token);
        keyRequest.setAmountCents("1000");
        keyRequest.setExpiration(3600);
        keyRequest.setOrderId(String.valueOf(orderResponse.getId()));
        keyRequest.setCurrency("EGP");
        keyRequest.setIntegrationId(integrationId);
        keyRequest.setLockOrderWhenPaid(false);

        PaymentKeyRequest.BillingData billingData = new PaymentKeyRequest.BillingData();
        billingData.firstName = "Test";
        billingData.lastName = "User";
        billingData.email = "test@example.com";
        billingData.phoneNumber = "+201000000000";
        billingData.street = "Test St";
        billingData.building = "1";
        billingData.floor = "1";
        billingData.apartment = "1";
        billingData.city = "Cairo";
        billingData.country = "EG";
        keyRequest.setBillingData(billingData);

        PaymentKeyResponse keyResponse = paymentService.requestPaymentKey(keyRequest);
        assertNotNull(keyResponse);
        assertNotNull(keyResponse.getToken(), "Payment key token should not be null");
        System.out.println("Payment Key Generated: " + keyResponse.getToken().substring(0, 10) + "...");
    }
}
