# Transaction API Reference

**Class:** `com.paymob.sdk.services.transaction.TransactionService`
**Accessed via:** `client.transactions()`
**Auth:** Secret Key

Post-payment operations: refund, void, and capture.

---

## Methods

### `refundTransaction(RefundRequest request) → TransactionResponse`

Refunds a completed transaction. Partial refunds are supported by specifying `amountCents`. If `amountCents` is omitted, the full amount is refunded.

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
| `amountCents` | `int` | No | Amount to refund in cents. Defaults to full amount |

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

## Operation Comparison

| Operation | Purpose | Timing | Fees |
|-----------|---------|--------|------|
| **Refund** | Reverse a completed transaction (full or partial) | Any time after settlement | Processing fees apply |
| **Void** | Cancel before settlement | Same day only | No fees |
| **Capture** | Finalize an authorized-only payment | Within 14 days of authorization | Normal fees |
