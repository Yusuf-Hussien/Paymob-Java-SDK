package com.paymob.sdk.models.common;

import com.paymob.sdk.models.enums.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a monetary amount with currency.
 * All amounts are stored in the smallest currency unit (cents for EGP, fils for KSA, etc.).
 */
public class Amount {
    private final int value;
    private final Currency currency;

    public Amount(int value, Currency currency) {
        if (value <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        this.value = value;
        this.currency = currency;
    }

    /**
     * Gets the amount value in smallest currency unit.
     * @return The numeric amount value
     */
    public int getValue() {
        return value;
    }

    /**
     * Gets the currency.
     * @return The currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Gets the amount as a decimal string (e.g., "100.50" for 1050 cents).
     * @return The formatted amount string
     */
    public String toDecimalString() {
        return String.format("%.2f", value / 100.0);
    }
}
