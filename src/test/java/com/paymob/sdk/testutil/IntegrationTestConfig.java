package com.paymob.sdk.testutil;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import com.paymob.sdk.models.enums.LogLevel;

/**
 * Environment-variable–based configuration for integration tests.
 * <p>
 * Required env vars:
 * <ul>
 * <li>{@code PAYMOB_SECRET_KEY}</li>
 * </ul>
 * Optional env vars: {@code PAYMOB_API_KEY}, {@code PAYMOB_PUBLIC_KEY},
 * {@code PAYMOB_HMAC_SECRET}, {@code PAYMOB_REGION},
 * {@code PAYMOB_INTEGRATION_ID}.
 */
public final class IntegrationTestConfig {

    private IntegrationTestConfig() {
    }

    public static PaymobClient createClientFromEnv() {
        return new PaymobClient(loadFromEnv());
    }

    public static PaymobConfig loadFromEnv() {
        String secretKey = System.getenv("PAYMOB_SECRET_KEY");
        String apiKey = System.getenv("PAYMOB_API_KEY");
        String publicKey = System.getenv("PAYMOB_PUBLIC_KEY");
        String hmacSecret = System.getenv("PAYMOB_HMAC_SECRET");
        String regionStr = System.getenv("PAYMOB_REGION");

        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException(
                    "PAYMOB_SECRET_KEY environment variable is required for integration tests");
        }

        PaymobRegion region = PaymobRegion.EGYPT;
        if (regionStr != null && !regionStr.isEmpty()) {
            try {
                region = PaymobRegion.valueOf(regionStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid region: " + regionStr + ". Defaulting to EGYPT.");
            }
        }

        return PaymobConfig.builder()
                .secretKey(secretKey)
                .apiKey(apiKey)
                .publicKey(publicKey)
                .hmacSecret(hmacSecret)
                .region(region)
                .timeout(30)
                .logLevel(LogLevel.NONE)
                .build();
    }

    public static int getIntegrationId() {
        String id = System.getenv("PAYMOB_INTEGRATION_ID");
        return id != null ? Integer.parseInt(id) : 0;
    }
}
