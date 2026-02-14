package com.paymob.sdk;

import com.paymob.sdk.models.Region;
import com.paymob.sdk.exceptions.PaymobException;

/**
 * Main entry point for the Paymob Java SDK.
 * Stores global configuration such as the API Key and Base URL.
 */
public class Paymob {
    private static volatile String apiKey;
    private static volatile String secretKey;
    private static volatile String publicKey;
    private static volatile Region region = Region.EGYPT;
    private static volatile int timeoutSeconds = 60;

    private Paymob() {
        // Prevent instantiation
    }

    /**
     * Initialize the SDK with the API Key (Auth).
     * Defaults to Egypt Region.
     *
     * @param apiKey The Paymob API Key.
     */
    public static void init(String apiKey) {
        init(apiKey, null, null, Region.EGYPT);
    }

    /**
     * Initialize the SDK with full credentials (Auth).
     *
     * @param apiKey
     * @param secretKey
     * @param publicKey
     * @param region
     */
    public static void init(String apiKey, String secretKey, String publicKey, Region region) {
        Paymob.apiKey = apiKey;
        Paymob.secretKey = secretKey;
        Paymob.publicKey = publicKey;
        Paymob.region = region != null ? region : Region.EGYPT;
    }

    public static String getApiKey() {
        if (apiKey == null) {
            throw new PaymobException("Paymob SDK not initialized. Call Paymob.init() first.");
        }
        return apiKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static String getPublicKey() {
        return publicKey;
    }

    public static String getBaseUrl() {
        return region.getBaseUrl();
    }

    public static String getCheckoutUrl() {
        return region.getCheckoutUrl();
    }

    public static int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public static void setTimeoutSeconds(int timeoutSeconds) {
        Paymob.timeoutSeconds = timeoutSeconds;
    }
}
