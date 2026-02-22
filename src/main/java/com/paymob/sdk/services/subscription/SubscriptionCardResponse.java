package com.paymob.sdk.services.subscription;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionCardResponse {

    @JsonProperty("id")
    private long id;

    @JsonProperty("token")
    private String token;

    @JsonProperty("created_at")
    @JsonAlias({"createdAt"})
    private String createdAt;

    @JsonProperty("is_primary")
    @JsonAlias({"isPrimary"})
    private boolean isPrimary;

    @JsonProperty("masked_pan")
    @JsonAlias({"maskedPan"})
    private String maskedPan;

    @JsonProperty("failed_attempts")
    @JsonAlias({"failedAttempts"})
    private int failedAttempts;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public String getMaskedPan() {
        return maskedPan;
    }

    public void setMaskedPan(String maskedPan) {
        this.maskedPan = maskedPan;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
}
