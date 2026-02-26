package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * Query filters for listing subscriptions.
 */
public class SubscriptionListRequest {
    private Long transaction;

    @JsonAlias({ "plan_id", "planId" })
    private Long planId;

    private String state;

    @JsonAlias({ "starts_at", "startsAt" })
    private String startsAt;

    @JsonAlias({ "next_billing", "nextBilling" })
    private String nextBilling;

    @JsonAlias({ "reminder_date", "reminderDate" })
    private String reminderDate;

    @JsonAlias({ "ends_at", "endsAt" })
    private String endsAt;

    public SubscriptionListRequest() {
    }

    private SubscriptionListRequest(Builder builder) {
        this.transaction = builder.transaction;
        this.planId = builder.planId;
        this.state = builder.state;
        this.startsAt = builder.startsAt;
        this.nextBilling = builder.nextBilling;
        this.reminderDate = builder.reminderDate;
        this.endsAt = builder.endsAt;
    }

    public static Builder builder() {
        return new Builder();
    }

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

    public static class Builder {
        private Long transaction;
        private Long planId;
        private String state;
        private String startsAt;
        private String nextBilling;
        private String reminderDate;
        private String endsAt;

        public Builder transaction(Long transaction) {
            this.transaction = transaction;
            return this;
        }

        public Builder planId(Long planId) {
            this.planId = planId;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder startsAt(String startsAt) {
            this.startsAt = startsAt;
            return this;
        }

        public Builder nextBilling(String nextBilling) {
            this.nextBilling = nextBilling;
            return this;
        }

        public Builder reminderDate(String reminderDate) {
            this.reminderDate = reminderDate;
            return this;
        }

        public Builder endsAt(String endsAt) {
            this.endsAt = endsAt;
            return this;
        }

        public SubscriptionListRequest build() {
            return new SubscriptionListRequest(this);
        }
    }
}
