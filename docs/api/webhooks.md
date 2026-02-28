# Webhooks API Reference

**Class:** `com.paymob.sdk.webhook.WebhookValidator`
**Instantiated directly:** `new WebhookValidator(hmacSecret)`

Validates Paymob webhook signatures using HMAC-SHA512 and parses events into typed objects. Auto-detects the event type (Transaction, Subscription, or Card Token) from the payload structure.

---

## Methods

### `validateSignature(String payload, String receivedHmac) → boolean`

Validates the HMAC signature without parsing the event. Use when you only need to verify authenticity.

```java
boolean valid = validator.validateSignature(rawBody, hmacFromRequest);
```

### `validateAndParse(String payload, String receivedHmac) → WebhookEvent`

Validates the signature and returns a parsed `WebhookEvent`. Returns `null` if validation fails.

```java
WebhookEvent event = validator.validateAndParse(rawBody, hmacFromRequest);
if (event == null) {
    // reject — invalid signature
}
```

### `validateCallbackSignature(Map<String, String> queryParams, String receivedHmac) → boolean`

Validates the HMAC for GET user-redirect callbacks using query parameters.

```java
boolean valid = validator.validateCallbackSignature(queryParams, hmacFromQuery);
```

---

## WebhookEvent

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getType()` | `WebhookEventType` | The parsed event type enum |
| `getData(Class<T> clazz)` | `T` | Deserializes the event payload into the given class |
| `getData()` | `Object` | Returns raw parsed data |

---

## All Event Types

### Transaction Events

| `WebhookEventType` | When it fires | `getData()` type |
|--------------------|---------------|-----------------|
| `TRANSACTION_SUCCESSFUL` | Successful payment | `TransactionResponse` |
| `TRANSACTION_FAILED` | Failed payment | `TransactionResponse` |
| `TRANSACTION_VOIDED` | Transaction voided | `TransactionResponse` |
| `TRANSACTION_REFUNDED` | Transaction refunded | `TransactionResponse` |

### Subscription Events

| `WebhookEventType` | When it fires | `getData()` type |
|--------------------|---------------|-----------------|
| `SUBSCRIPTION_CREATED` | Customer enrolled and first payment made | `SubscriptionResponse` |
| `SUBSCRIPTION_SUSPENDED` | Subscription suspended (manually or due to failed retries) | `SubscriptionResponse` |
| `SUBSCRIPTION_RESUMED` | Suspended subscription reactivated | `SubscriptionResponse` |
| `SUBSCRIPTION_CANCELED` | Subscription permanently canceled | `SubscriptionResponse` |
| `SUBSCRIPTION_UPDATED` | Subscription details updated | `SubscriptionResponse` |
| `SUBSCRIPTION_SUCCESSFUL_TRANSACTION` | Recurring billing payment succeeded | `SubscriptionResponse` |
| `SUBSCRIPTION_FAILED_TRANSACTION` | Recurring billing payment failed | `SubscriptionResponse` |
| `SUBSCRIPTION_FAILED_OVERDUE_TRANSACTION` | Overdue payment retry failed | `SubscriptionResponse` |
| `SUBSCRIPTION_ADD_SECONDARY_CARD` | Customer added a secondary card | `SubscriptionResponse` |
| `SUBSCRIPTION_CHANGE_PRIMARY_CARD` | Customer changed the primary billing card | `SubscriptionResponse` |
| `SUBSCRIPTION_DELETE_CARD` | A card was removed from the subscription | `SubscriptionResponse` |

### Card Token Events

| `WebhookEventType` | When it fires | `getData()` type |
|--------------------|---------------|-----------------|
| `CARD_TOKEN` | Customer saved their card during checkout | `String` (token) |
