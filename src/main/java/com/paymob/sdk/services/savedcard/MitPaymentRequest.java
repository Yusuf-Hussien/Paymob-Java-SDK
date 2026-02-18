package com.paymob.sdk.services.savedcard;

/**
 * Request for Merchant Initiated Transaction (MIT).
 * Automated charges for subscriptions/top-ups, no customer present.
 */
public class MitPaymentRequest {
    private String cardToken;
    private int amount;
    private String currency;
    private String merchantOrderId;

    public MitPaymentRequest(String cardToken, int amount, String currency, String merchantOrderId) {
        this.cardToken = cardToken;
        this.amount = amount;
        this.currency = currency;
        this.merchantOrderId = merchantOrderId;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMerchantOrderId() {
        return merchantOrderId;
    }

    public void setMerchantOrderId(String merchantOrderId) {
        this.merchantOrderId = merchantOrderId;
    }
}
