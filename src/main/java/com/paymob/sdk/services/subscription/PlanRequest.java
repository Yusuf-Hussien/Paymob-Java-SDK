package com.paymob.sdk.services.subscription;

import com.paymob.sdk.models.enums.BillingCycle;

/**
 * Request for creating subscription plans.
 */
public class PlanRequest {
    private String name;
    private String description;
    private BillingCycle billingCycle;
    private int amountCents;
    private int retrialCount;
    private int reminderDays;
    private boolean useTransactionAmount;

    public PlanRequest(String name, BillingCycle billingCycle, int amountCents) {
        this.name = name;
        this.billingCycle = billingCycle;
        this.amountCents = amountCents;
        this.retrialCount = 3; // default
        this.reminderDays = 3; // default
        this.useTransactionAmount = true; // default
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BillingCycle getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(BillingCycle billingCycle) {
        this.billingCycle = billingCycle;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(int amountCents) {
        this.amountCents = amountCents;
    }

    public int getRetrialCount() {
        return retrialCount;
    }

    public void setRetrialCount(int retrialCount) {
        this.retrialCount = retrialCount;
    }

    public int getReminderDays() {
        return reminderDays;
    }

    public void setReminderDays(int reminderDays) {
        this.reminderDays = reminderDays;
    }

    public boolean isUseTransactionAmount() {
        return useTransactionAmount;
    }

    public void setUseTransactionAmount(boolean useTransactionAmount) {
        this.useTransactionAmount = useTransactionAmount;
    }
}
