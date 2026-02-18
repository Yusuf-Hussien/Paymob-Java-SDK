package com.paymob.sdk.models.enums;

/**
 * Billing cycle options for subscription plans.
 */
public enum BillingCycle {
    WEEKLY("WEEKLY"),
    BIWEEKLY("BIWEEKLY"),
    MONTHLY("MONTHLY"),
    QUARTERLY("QUARTERLY"),
    ANNUAL("ANNUAL");

    private final String value;

    BillingCycle(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BillingCycle fromValue(String value) {
        for (BillingCycle cycle : values()) {
            if (cycle.value.equals(value)) {
                return cycle;
            }
        }
        throw new IllegalArgumentException("Unknown billing cycle: " + value);
    }
}
