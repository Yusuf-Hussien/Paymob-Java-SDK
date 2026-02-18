package com.paymob.sdk.webhook;

/**
 * Types of Paymob webhook events.
 */
public enum WebhookEventType {
    TRANSACTION_SUCCESSFUL("TRANSACTION_SUCCESSFUL"),
    TRANSACTION_FAILED("TRANSACTION_FAILED"),
    TRANSACTION_VOIDED("TRANSACTION_VOIDED"),
    TRANSACTION_REFUNDED("TRANSACTION_REFUNDED"),
    SUBSCRIPTION_CREATED("SUBSCRIPTION_CREATED"),
    SUBSCRIPTION_SUSPENDED("SUBSCRIPTION_SUSPENDED"),
    SUBSCRIPTION_RESUMED("SUBSCRIPTION_RESUMED"),
    SUBSCRIPTION_CANCELLED("SUBSCRIPTION_CANCELLED");

    private final String value;

    WebhookEventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static WebhookEventType fromValue(String value) {
        for (WebhookEventType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown webhook event type: " + value);
    }
}
