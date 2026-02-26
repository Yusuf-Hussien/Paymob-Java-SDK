package com.paymob.sdk.testutil;

import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import com.paymob.sdk.models.common.BillingData;
import com.paymob.sdk.models.common.Item;
import com.paymob.sdk.models.enums.Currency;

import java.util.Arrays;
import java.util.UUID;

/**
 * Shared test fixtures: constants, factory methods, and helpers for unit tests.
 * For integration-test configuration (env-vars), see
 * {@link IntegrationTestConfig}.
 */
public final class TestFixtures {

    // ── Constants ────────────────────────────────────────────────────────────
    public static final String TEST_SECRET_KEY = "sk_test_123456789";
    public static final String TEST_API_KEY = "ak_test_123456789";
    public static final String TEST_PUBLIC_KEY = "pk_test_123456789";
    public static final String TEST_HMAC_SECRET = "hmac_test_secret_123456789";

    private TestFixtures() {
    }

    // ── Config Builders ──────────────────────────────────────────────────────
    public static PaymobConfig createTestConfig() {
        return createTestConfig(PaymobRegion.EGYPT);
    }

    public static PaymobConfig createTestConfig(PaymobRegion region) {
        return PaymobConfig.builder()
                .secretKey(TEST_SECRET_KEY)
                .apiKey(TEST_API_KEY)
                .publicKey(TEST_PUBLIC_KEY)
                .hmacSecret(TEST_HMAC_SECRET)
                .region(region)
                .build();
    }

    // ── Model Factories ──────────────────────────────────────────────────────
    public static BillingData createTestBillingData() {
        return new BillingData("Test", "User", "test@example.com", "+201234567890");
    }

    public static BillingData createTestBillingData(String firstName, String lastName,
            String email, String phone) {
        return new BillingData(firstName, lastName, email, phone);
    }

    public static Item createTestItem() {
        return new Item("Test Product", 10000, "Test Description", 1);
    }

    public static Item createTestItem(String name, int amount, String description, int quantity) {
        return new Item(name, amount, description, quantity);
    }

    // ── ID / Reference Generators ────────────────────────────────────────────
    public static String generateUniqueOrderId() {
        return "TEST-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generateUniqueEmail() {
        return "test-" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    public static String generateUniquePhone() {
        return "+201" + String.format("%08d", (int) (Math.random() * 100000000));
    }

    // ── Intention Helpers ────────────────────────────────────────────────────
    public static com.paymob.sdk.services.intention.IntentionRequest createTestIntentionRequest() {
        BillingData billingData = createTestBillingData();
        Item item = createTestItem();

        com.paymob.sdk.services.intention.IntentionRequest request = new com.paymob.sdk.services.intention.IntentionRequest(
                item.getTotalAmount(), Currency.EGP, Arrays.asList(item), billingData);
        request.setPaymentMethods(Arrays.asList(123456));
        request.setSpecialReference(generateUniqueOrderId());

        return request;
    }

    public static com.paymob.sdk.services.intention.IntentionRequest createInvalidIntentionRequest() {
        BillingData billingData = createTestBillingData();
        Item item = createTestItem();

        // Mismatched amount (should be 10000 but we use 5000)
        com.paymob.sdk.services.intention.IntentionRequest request = new com.paymob.sdk.services.intention.IntentionRequest(
                5000, Currency.EGP, Arrays.asList(item), billingData);
        request.setPaymentMethods(Arrays.asList(123456));

        return request;
    }

    // ── Wait Utilities ───────────────────────────────────────────────────────
    public static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static boolean waitForCondition(java.util.function.BooleanSupplier condition,
            long timeoutMillis) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutMillis) {
            if (condition.getAsBoolean()) {
                return true;
            }
            sleepQuietly(100);
        }
        return false;
    }
}
