# Saved Cards API Reference

**Class:** `com.paymob.sdk.services.savedcard.SavedCardService`
**Accessed via:** `client.savedCards()`
**Auth:** Secret Key

Process payments using previously tokenized cards. Cards are tokenized during the first payment when the customer opts to save their card.

---

## Methods

### `processCitPayment(CitPaymentRequest request) → TokenizedPaymentResponse`

Customer Initiated Transaction — the customer is present and enters their CVV.

```java
TokenizedPaymentResponse response = client.savedCards().processCitPayment(
    CitPaymentRequest.builder()
        .cardToken("e98aceb96f5a370d...")
        .cvv("123")
        .amount(1000)
        .currency("EGP")
        .build());
```

### `processMitPayment(MitPaymentRequest request) → TokenizedPaymentResponse`

Merchant Initiated Transaction — automated charge with no customer present (recurring billing, top-ups). No CVV required.

```java
TokenizedPaymentResponse response = client.savedCards().processMitPayment(
    MitPaymentRequest.builder()
        .cardToken("e98aceb96f5a370d...")
        .amount(1000)
        .currency("EGP")
        .merchantOrderId("MST-" + System.currentTimeMillis())
        .build());
```

---

## Request Fields

### CitPaymentRequest

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `cardToken` | `String` | ✅ | Saved card token from a previous `CARD_TOKEN` webhook |
| `cvv` | `String` | ✅ | Card CVV (collected from the customer at time of payment) |
| `amount` | `int` | ✅ | Amount in cents |
| `currency` | `String` | ✅ | 3-letter currency code |

### MitPaymentRequest

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `cardToken` | `String` | ✅ | Saved card token |
| `amount` | `int` | ✅ | Amount in cents |
| `currency` | `String` | ✅ | 3-letter currency code |
| `merchantOrderId` | `String` | ✅ | Your unique reference for this charge |

---

## CIT vs MIT

| | CIT | MIT |
|--|-----|-----|
| Customer present | Yes | No |
| CVV required | Yes | No |
| Use case | Manual card selection at checkout | Recurring charges, top-ups |
