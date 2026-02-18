package com.paymob.sdk.core;

/**
 * Represents Paymob regions with their respective base URLs.
 * Each region has its own dedicated Paymob infrastructure.
 */
public enum PaymobRegion {
    EGYPT("https://accept.paymob.com", "https://accept.paymob.com"),
    KSA("https://ksa.paymob.com", "https://ksa.paymob.com"),
    UAE("https://uae.paymob.com", "https://uae.paymob.com"),
    OMAN("https://oman.paymob.com", "https://oman.paymob.com");

    private final String baseUrl;
    private final String checkoutUrl;

    PaymobRegion(String baseUrl, String checkoutUrl) {
        this.baseUrl = baseUrl;
        this.checkoutUrl = checkoutUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }
}
