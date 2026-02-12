package com.paymob.sdk;

/**
 * Main entry point for the Paymob Java SDK.
 * Stores global configuration such as the API Key and Base URL.
 */
public class Paymob {
    private static volatile String apiKey;
    private static volatile String baseUrl = "https://accept.paymob.com/api";
    private static volatile int timeoutSeconds = 30;

    private Paymob() {
        // Prevent instantiation
    }

    /**
     * Initialize the SDK with your API Key.
     *
     * @param apiKey The API Key from your Paymob Dashboard.
     */
    public static void init(String apiKey) {
        Paymob.apiKey = apiKey;
    }

    public static String getApiKey() {
        if (apiKey == null) {
            throw new IllegalStateException("Paymob SDK not initialized. Call Paymob.init(apiKey) first.");
        }
        return apiKey;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Set a custom Base URL (e.g., for testing or different environments).
     *
     * @param baseUrl The base URL to use.
     */
    public static void setBaseUrl(String baseUrl) {
        Paymob.baseUrl = baseUrl;
    }

    public static int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public static void setTimeoutSeconds(int timeoutSeconds) {
        Paymob.timeoutSeconds = timeoutSeconds;
    }
}
