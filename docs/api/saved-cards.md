# Saved Cards API Reference

**Class:** `com.paymob.sdk.services.savedcard.SavedCardService`
**Accessed via:** `client.savedCards()`
**Auth:** Secret Key for CIT/MIT intention creation, no auth header for `executeMotoPayment`

Process payments using previously tokenized cards. Cards are tokenized during the first payment when the customer opts to save their card.

MIT flow note: `processMitPayment` creates an intention context for merchant-initiated charging. `executeMotoPayment` is the charge execution call that uses the saved card token and payment token.

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

### `executeMotoPayment(MotoCardPayRequest request) → TransactionResponse`

Executes the charge using a saved card token and a payment token (from the created intention/payment key flow).

```java
TransactionResponse charge = client.savedCards().executeMotoPayment(
    new MotoCardPayRequest("saved_card_token", "payment_token"));
```

Recommended sequence for no-customer-present charging:

1. Ensure the card is already tokenized (from prior customer-authorized flow).
2. Call `processMitPayment(...)` with the saved card token.
3. Use the returned payment context token where required by your flow.
4. Call `executeMotoPayment(...)` to perform the actual charge.

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
| `merchantOrderId` | `String` | No | Your unique reference for this charge |

### MotoCardPayRequest

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `source.identifier` | `String` | ✅ | Saved card token |
| `source.subtype` | `String` | ✅ | Must be `TOKEN` |
| `paymentToken` | `String` | ✅ | Payment token from the intention/payment key flow |

## Response Fields

### TokenizedPaymentResponse

`TokenizedPaymentResponse` extends `IntentionResponse`.

| Field | Type | Description |
|-------|------|-------------|
| `success` | `boolean` | Whether request was accepted successfully |
| `message` | `String` | Additional status/error message |
| `transactionId` | `long` | Related transaction identifier |
| `clientSecret` | `String` | Inherited from `IntentionResponse`; used for checkout URL/retrieval |

### TransactionResponse (from `executeMotoPayment`)

| Field | Type | Description |
|-------|------|-------------|
| `id` | `Long` | Transaction ID |
| `success` | `Boolean` | Charge success flag |
| `pending` | `Boolean` | Pending status |
| `amountCents` | `Integer` | Charged amount |
| `currency` | `String` | Currency code |
| `isVoided` | `Boolean` | Void status |
| `isRefunded` | `Boolean` | Refund status |
| `order` | `Transaction.OrderInfo` | Linked order details |
| `sourceData` | `Map<String, Object>` | Source metadata |

---

## CIT vs MIT

| | CIT | MIT |
|--|-----|-----|
| Customer present | Yes | No |
| CVV required | Yes | No |
| Use case | Manual card selection at checkout | Recurring charges, top-ups |
