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
