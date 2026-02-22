package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionTransactionResponse {

    @JsonProperty("id")
    private long id;

    @JsonProperty("amount_cents")
    @JsonAlias({"amountCents"})
    private int amountCents;

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("pending")
    private boolean pending;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("created_at")
    @JsonAlias({"createdAt"})
    private String createdAt;

    @JsonProperty("paid_at")
    @JsonAlias({"paidAt"})
    private String paidAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(int amountCents) {
        this.amountCents = amountCents;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }
}
