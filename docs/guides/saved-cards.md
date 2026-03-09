# Saved Cards Guide

This guide covers tokenizing cards during checkout and using them for future payments, both when the customer is present (CIT) and for merchant-initiated charging flows (MIT + MOTO execution).

## How Card Tokenization Works

When a customer completes a payment through the Unified Checkout, they can opt to save their card. Paymob tokenizes the card and fires a `CARD_TOKEN` webhook with the reusable token. Store this token linked to the customer in your database.

```java
case CARD_TOKEN -> {
    String cardToken = (String) event.getData();
    db.saveCardToken(customerId, cardToken);
}
```

---

## CIT — Customer Initiated Transaction

Use when the customer is present and actively initiating the payment. CVV is required.

```java
import com.paymob.sdk.services.savedcard.CitPaymentRequest;
import com.paymob.sdk.services.savedcard.TokenizedPaymentResponse;

TokenizedPaymentResponse response = client.savedCards().processCitPayment(
    CitPaymentRequest.builder()
        .cardToken(db.getCardToken(customerId))
        .cvv("123")               // Collected from the customer at checkout
        .amount(1000)
        .currency("EGP")
        .build());
```

**When to use:** Checkout with a pre-selected saved card where the customer is interacting with your UI.

---

## MIT — Merchant Initiated Transaction

Use this to create an intention for merchant-initiated charging with no customer present. No CVV is required.

```java
import com.paymob.sdk.services.savedcard.MitPaymentRequest;

TokenizedPaymentResponse response = client.savedCards().processMitPayment(
    MitPaymentRequest.builder()
        .cardToken(db.getCardToken(customerId))
        .amount(1000)
        .currency("EGP")
        .merchantOrderId("RENEWAL-" + System.currentTimeMillis())  // Must be unique
        .build());
```

Then execute the MOTO payment using the saved card token and payment token:

```java
import com.paymob.sdk.services.savedcard.MotoCardPayRequest;
import com.paymob.sdk.services.transaction.TransactionResponse;

TransactionResponse charge = client.savedCards().executeMotoPayment(
    new MotoCardPayRequest(
        db.getCardToken(customerId),
        paymentTokenFromMitFlow));
```

**When to use:** Scheduled billing, auto top-ups, or any charge triggered by your system without customer interaction.

---

## Error Handling

```java
try {
    TokenizedPaymentResponse response = client.savedCards().processCitPayment(citRequest);
} catch (ValidationException e) {
    // Invalid CVV, expired token, insufficient funds, etc.
    log.error("Payment failed: {}", e.getErrorBody());
} catch (AuthenticationException e) {
    log.error("Auth failed — check secret key and region");
}
```

---

## CIT vs MIT Summary

| | CIT | MIT |
|--|-----|-----|
| Customer present | Yes | No |
| CVV required | Yes | No |
| Use case | Checkout with saved card | Recurring charges, top-ups |

See the [Saved Cards API Reference](../api/saved-cards.md) for full request field details.
