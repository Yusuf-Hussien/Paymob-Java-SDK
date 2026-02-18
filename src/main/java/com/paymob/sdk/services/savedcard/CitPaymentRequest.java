package com.paymob.sdk.services.savedcard;

/**
 * Request for Customer Initiated Transaction (CIT).
 * Customer is present at checkout, requires CVV re-entry.
 */
public class CitPaymentRequest {
    private String cardToken;
    private String cvv;
    private int amount;
    private String currency;

    public CitPaymentRequest(String cardToken, String cvv, int amount, String currency) {
        this.cardToken = cardToken;
        this.cvv = cvv;
        this.amount = amount;
        this.currency = currency;
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
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
}
