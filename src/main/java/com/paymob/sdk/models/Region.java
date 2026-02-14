package com.paymob.sdk.models;

public enum Region {
    EGYPT("https://accept.paymob.com", "https://accept.paymob.com/unifiedcheckout"),
    SAUDI_ARABIA("https://ksa.paymob.com", "https://ksa.paymob.com/unifiedcheckout"),
    UAE("https://uae.paymob.com", "https://uae.paymob.com/unifiedcheckout"),
    OMAN("https://oman.paymob.com", "https://oman.paymob.com/unifiedcheckout");

    private final String baseUrl;
    private final String checkoutUrl;

    Region(String baseUrl, String checkoutUrl) {
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
