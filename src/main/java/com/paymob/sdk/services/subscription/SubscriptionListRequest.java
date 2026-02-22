package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * Query filters for listing subscriptions.
 */
public class SubscriptionListRequest {
    private Long transaction;

    @JsonAlias({"plan_id", "planId"})
    private Long planId;

    private String state;

    @JsonAlias({"starts_at", "startsAt"})
    private String startsAt;

    @JsonAlias({"next_billing", "nextBilling"})
    private String nextBilling;

    @JsonAlias({"reminder_date", "reminderDate"})
    private String reminderDate;

    @JsonAlias({"ends_at", "endsAt"})
    private String endsAt;

    public Long getTransaction() {
        return transaction;
    }

    public void setTransaction(Long transaction) {
        this.transaction = transaction;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public String getNextBilling() {
        return nextBilling;
    }

    public void setNextBilling(String nextBilling) {
        this.nextBilling = nextBilling;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(String endsAt) {
        this.endsAt = endsAt;
    }
}
