package com.paymob.sdk.models.enums;

/**
 * Supported currency codes for Paymob transactions.
 */
public enum Currency {
    EGP("EGP"),
    SAR("SAR"),
    AED("AED"),
    OMR("OMR");

    private final String code;

    Currency(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Currency fromCode(String code) {
        for (Currency currency : values()) {
            if (currency.code.equals(code)) {
                return currency;
            }
        }
        throw new IllegalArgumentException("Unknown currency code: " + code);
    }
}
