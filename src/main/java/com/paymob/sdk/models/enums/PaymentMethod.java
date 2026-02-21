package com.paymob.sdk.models.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Enumeration of supported payment methods.
 */
public enum PaymentMethod {
    @JsonProperty("card")
    CARD("card"),
    
    @JsonProperty("wallet")
    WALLET("wallet"),
    
    @JsonProperty("kiosk")
    KIOSK("kiosk"),
    
    @JsonProperty("installment")
    INSTALLMENT("installment");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    /**
     * Gets the string value of the payment method.
     * @return The payment method value
     */
    public String getValue() {
        return value;
    }

    /**
     * Finds a PaymentMethod by its string value.
     * @param value The string value
     * @return The PaymentMethod enum
     * @throws IllegalArgumentException if the value is not found
     */
    public static PaymentMethod fromValue(String value) {
        for (PaymentMethod method : values()) {
            if (method.value.equalsIgnoreCase(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown payment method: " + value);
    }
}
