package com.paymob.sdk.utils;

import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import java.time.Duration;

public class TestConfigUtils {
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
            throw new IllegalStateException("PAYMOB_SECRET_KEY environment variable is required for integration tests");
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
                .timeout(Duration.ofSeconds(30))
                .enableLogging(true)
                .build();
    }

    public static int getIntegrationId() {
        String id = System.getenv("PAYMOB_INTEGRATION_ID");
        return id != null ? Integer.parseInt(id) : 0;
    }
}
