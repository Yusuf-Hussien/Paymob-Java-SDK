package com.paymob.sdk.core;

/**
 * Represents Paymob regions with their respective base URLs.
 * Each region has its own dedicated Paymob infrastructure.
 */
public enum PaymobRegion {
    EGYPT("https://accept.paymob.com"),
    KSA("https://ksa.paymob.com"),
    UAE("https://uae.paymob.com"),
    OMAN("https://oman.paymob.com");

    private final String baseUrl;

    PaymobRegion(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
