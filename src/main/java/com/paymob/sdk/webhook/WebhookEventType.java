package com.paymob.sdk.webhook;

/**
 * Types of Paymob webhook events.
 * <p>
 * Transaction types use internal constant values.
 * Subscription types use the exact {@code trigger_type} strings from the Paymob
 * API.
 */
public enum WebhookEventType {
    // ── Transaction types ──
    TRANSACTION_SUCCESSFUL("TRANSACTION_SUCCESSFUL"),
    TRANSACTION_FAILED("TRANSACTION_FAILED"),
    TRANSACTION_VOIDED("TRANSACTION_VOIDED"),
    TRANSACTION_REFUNDED("TRANSACTION_REFUNDED"),

    // ── Subscription types (values match Paymob's trigger_type exactly) ──
    SUBSCRIPTION_CREATED("Subscription Created"),
    SUBSCRIPTION_SUSPENDED("suspended"),
    SUBSCRIPTION_CANCELED("canceled"),
    SUBSCRIPTION_RESUMED("resumed"),
    SUBSCRIPTION_UPDATED("updated"),
    SUBSCRIPTION_ADD_SECONDARY_CARD("add_secondry_card"),
    SUBSCRIPTION_CHANGE_PRIMARY_CARD("change_primary_card"),
    SUBSCRIPTION_DELETE_CARD("delete_card"),
    SUBSCRIPTION_REGISTER_WEBHOOK("register_webhook"),
    SUBSCRIPTION_SUCCESSFUL_TRANSACTION("Successful Transaction"),
    SUBSCRIPTION_FAILED_TRANSACTION("Failed Transaction"),
    SUBSCRIPTION_FAILED_OVERDUE_TRANSACTION("Failed Overdue Transaction");

    private final String value;

    WebhookEventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Looks up an event type by its exact internal value string.
     */
    public static WebhookEventType fromValue(String value) {
        for (WebhookEventType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown webhook event type: " + value);
    }

    /**
     * Looks up a subscription event type by the trigger_type string from the API.
     * Returns null if no match (instead of throwing), for safe parsing.
     */
    public static WebhookEventType fromTriggerType(String triggerType) {
        if (triggerType == null)
            return null;
        for (WebhookEventType type : values()) {
            if (type.value.equals(triggerType)) {
                return type;
            }
        }
        return null;
    }
}
