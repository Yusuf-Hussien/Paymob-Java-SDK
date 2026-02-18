package com.paymob.sdk.services.subscription;

/**
 * Request for subscription lifecycle operations (suspend, resume, cancel).
 */
public class SubscriptionLifecycleRequest {
    private String action; // SUSPEND, RESUME, CANCEL
    private String reason;

    public SubscriptionLifecycleRequest() {
    }

    public SubscriptionLifecycleRequest(String action, String reason) {
        this.action = action;
        this.reason = reason;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
