# Transaction Inquiry API Reference

**Class:** `com.paymob.sdk.services.inquiry.TransactionInquiryService`
**Accessed via:** `client.inquiry()`
**Auth:** Bearer Token (API Key)

Look up transaction status by multiple identifiers. Useful for reconciliation and status polling.

---

## Methods

### `byMerchantOrderId(String merchantOrderId) → InquiryResponse`

Look up by your internal order reference (`specialReference` in the intention).

```java
InquiryResponse result = client.inquiry().byMerchantOrderId("ORDER-123");
```

### `byOrderId(long orderId) → InquiryResponse`

Look up by Paymob's internal order ID (`intentionOrderId` from `IntentionResponse`).

```java
InquiryResponse result = client.inquiry().byOrderId(217503754L);
```

### `byTransactionId(long transactionId) → InquiryResponse`

Look up a specific transaction directly by its ID.

```java
InquiryResponse result = client.inquiry().byTransactionId(192036465L);
```

---

## Identifier Reference

| Method | Identifier Source | Use When |
|--------|-------------------|----------|
| `byMerchantOrderId` | Your `specialReference` | You have your own order reference |
| `byOrderId` | `intentionOrderId` from `IntentionResponse` | You stored Paymob's order ID |
| `byTransactionId` | Transaction ID from webhook | You have the specific transaction ID |

---

## InquiryResponse Fields

`InquiryResponse` extends `Transaction` and exposes the complete transaction object.

| Field | Type | Description |
|-------|------|-------------|
| `id` | `long` | Transaction ID |
| `success` | `boolean` | Whether the transaction was successful |
| `pending` | `boolean` | Whether the transaction is still pending |
| `amountCents` | `int` | Transaction amount in cents |
| `currency` | `String` | 3-letter currency code |
| `isVoided` | `boolean` | Whether the transaction has been voided |
| `isRefunded` | `boolean` | Whether the transaction has been refunded |
| `isCaptured` | `boolean` | Whether the transaction has been captured |
| `isAuth` | `boolean` | Whether this is an authorization-only transaction |
| `is3dSecure` | `boolean` | Whether 3D Secure was used |
| `isLive` | `boolean` | `true` for live transactions, `false` for sandbox |
| `errorOccured` | `boolean` | Whether an error occurred during processing |
| `integrationId` | `int` | Integration ID used for this transaction |
| `merchantOrderId` | `String` | Your `specialReference` merchant order ID |
| `createdAt` | `String` | ISO 8601 creation timestamp |
| `updatedAt` | `String` | ISO 8601 last-updated timestamp |
| `sourceData` | `Map<String, Object>` | Payment source metadata (card type, sub-type, etc.) |
| `data` | `Map<String, Object>` | Additional transaction data from the gateway |
| `order` | `Transaction.OrderInfo` | Nested order details (id, amountCents, currency, merchantOrderId, items) |
