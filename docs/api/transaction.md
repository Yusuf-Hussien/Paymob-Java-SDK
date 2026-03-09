# Transaction API Reference

**Class:** `com.paymob.sdk.services.transaction.TransactionService`
**Accessed via:** `client.transactions()`
**Auth:** Secret Key

Post-payment operations: refund, void, and capture.

---

## Methods

### `refundTransaction(RefundRequest request) → TransactionResponse`

Refunds a completed transaction. Partial refunds are supported by specifying `amountCents`.

Note: in the SDK model, `amountCents` is an `int`. To avoid ambiguity, set it explicitly to the intended refund amount.

```java
TransactionResponse refund = client.transactions().refundTransaction(
    RefundRequest.builder()
        .transactionId(192036465L)
        .amountCents(500)  // Omit for full refund
        .build());
```

### `voidTransaction(VoidRequest request) → TransactionResponse`

Voids a same-day transaction before settlement. Avoids processing fees compared to a refund.

```java
TransactionResponse voided = client.transactions().voidTransaction(
    VoidRequest.builder()
        .transactionId(192036465L)
        .build());
```

### `captureTransaction(CaptureRequest request) → TransactionResponse`

Captures an authorize-only transaction. Must be done within 14 days of authorization.

```java
TransactionResponse captured = client.transactions().captureTransaction(
    CaptureRequest.builder()
        .transactionId(192036465L)
        .amountCents(1000)
        .build());
```

---

## Request Fields

### RefundRequest

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `transactionId` | `long` | ✅ | ID of the transaction to refund |
| `amountCents` | `int` | Recommended | Amount to refund in cents. Set explicitly for clear behavior |

### VoidRequest

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `transactionId` | `long` | ✅ | ID of the transaction to void |

### CaptureRequest

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `transactionId` | `long` | ✅ | ID of the authorized transaction to capture |
| `amountCents` | `int` | ✅ | Amount to capture in cents |

---

## Response Fields (`TransactionResponse`)

The same response model is used for refund, void, and capture operations.

| Field | Type | Description |
|-------|------|-------------|
| `id` | `Long` | Transaction ID |
| `success` | `Boolean` | Operation success flag |
| `pending` | `Boolean` | Pending status |
| `amountCents` | `Integer` | Transaction amount in cents |
| `currency` | `String` | 3-letter currency code |
| `isVoided` | `Boolean` | Whether transaction is voided |
| `isRefunded` | `Boolean` | Whether transaction is refunded |
| `isCaptured` | `Boolean` | Whether transaction is captured |
| `order` | `Transaction.OrderInfo` | Nested order details |
| `sourceData` | `Map<String, Object>` | Payment source metadata |

---

## Operation Comparison

| Operation | Purpose | Timing | Fees |
|-----------|---------|--------|------|
| **Refund** | Reverse a completed transaction (full or partial) | Any time after settlement | Processing fees apply |
| **Void** | Cancel before settlement | Same day only | No fees |
| **Capture** | Finalize an authorized-only payment | Within 14 days of authorization | Normal fees |
