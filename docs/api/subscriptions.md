# Subscriptions API Reference

**Class:** `com.paymob.sdk.services.subscription.SubscriptionService`
**Accessed via:** `client.subscriptions()`
**Auth:** Secret Key (enrollment) / Bearer Token (management)

Manage customer subscription lifecycle. Enrollment goes through the Intention API; management operations hit the subscription endpoints directly.

---

## Enrollment

### `subscribe(IntentionRequest request) → IntentionResponse`

Enrolls a customer in a plan. The `IntentionRequest` must include `subscriptionPlanId`. Returns a checkout session — redirect the customer to complete enrollment and save their card.

```java
IntentionResponse enrollment = client.subscriptions().subscribe(
    IntentionRequest.builder()
        .amount(5000)
        .currency(Currency.EGP)
        .paymentMethods(List.of(integrationId))
        .items(List.of(item))
        .billingData(billing)
        .subscriptionPlanId(planId)
        .build());

String checkoutUrl = client.intentions().getUnifiedCheckoutUrl(enrollment);
```

### `addSecondaryCard(IntentionRequest request) → IntentionResponse`

Creates a checkout session for adding a secondary card to an existing subscription. Set `subscriptionv2Id` in the request.

```java
IntentionResponse addCard = client.subscriptions().addSecondaryCard(
    IntentionRequest.builder()
        .amount(0)
        .currency(Currency.EGP)
        .paymentMethods(List.of(integrationId))
        .items(List.of(placeholderItem))
        .billingData(billing)
        .subscriptionv2Id(subscriptionId)
        .build());
```

---

## Lifecycle Management

### `get(long subscriptionId) → SubscriptionResponse`
### `list(SubscriptionListRequest filters) → SubscriptionsPage`
### `update(long subscriptionId, SubscriptionUpdateRequest request) → SubscriptionResponse`
### `suspend(long subscriptionId) → SubscriptionResponse`
### `resume(long subscriptionId) → SubscriptionResponse`
### `cancel(long subscriptionId) → SubscriptionResponse`

```java
// Retrieve
SubscriptionResponse sub = client.subscriptions().get(subscriptionId);
System.out.println(sub.getState()); // "active", "suspended", "canceled"

// List with filters
SubscriptionsPage active = client.subscriptions().list(
    SubscriptionListRequest.builder()
        .state("active")
        .planId(planId)
        .build());

// Lifecycle
client.subscriptions().suspend(subscriptionId);
client.subscriptions().resume(subscriptionId);
client.subscriptions().cancel(subscriptionId); // permanent
```

---

## Transaction History

### `getLastTransaction(long subscriptionId) → SubscriptionTransactionResponse`
### `listTransactions(long subscriptionId) → SubscriptionTransactionsPage`

```java
SubscriptionTransactionResponse last = client.subscriptions().getLastTransaction(subscriptionId);
SubscriptionTransactionsPage history = client.subscriptions().listTransactions(subscriptionId);
```

---

## Card Management

### `listCards(long subscriptionId) → List<SubscriptionCardResponse>`
### `changePrimaryCard(long subscriptionId, long cardId) → SubscriptionResponse`
### `deleteCard(long subscriptionId, long cardId) → SubscriptionResponse`
### `registerWebhook(long subscriptionId, String webhookUrl) → SubscriptionResponse`

```java
List<SubscriptionCardResponse> cards = client.subscriptions().listCards(subscriptionId);
client.subscriptions().changePrimaryCard(subscriptionId, cards.get(1).getId());
client.subscriptions().deleteCard(subscriptionId, cardId);
client.subscriptions().registerWebhook(subscriptionId, "https://merchant.example/subscription-webhook");
```

---

## SubscriptionResponse Fields

Returned by `get`, `update`, `suspend`, `resume`, `cancel`, `changePrimaryCard`, `deleteCard`, `registerWebhook`, and subscription webhook events.

| Field | Type | Description |
|-------|------|-------------|
| `id` | `long` | Subscription ID |
| `state` | `String` | Current state: `"active"`, `"suspended"`, `"canceled"` |
| `amountCents` | `int` | Current recurring charge amount in cents |
| `frequency` | `int` | Billing interval in days |
| `planId` | `long` | ID of the associated plan |
| `startsAt` | `String` | ISO 8601 subscription start date |
| `nextBilling` | `String` | ISO 8601 date of next scheduled billing |

## SubscriptionUpdateRequest Fields

Only the fields you include are updated; omitted fields are left unchanged.

| Field | Type | Required | Description |
|-------|------|----------|--------------|
| `amountCents` | `Integer` | No | Override the recurring charge amount for this subscription in cents |
| `endsAt` | `String` | No | ISO 8601 date to end the subscription (extends or shortens the billing period) |

## SubscriptionCardResponse Fields

Returned by `listCards`.

| Field | Type | Description |
|-------|------|-------------|
| `id` | `long` | Card record ID (used for `changePrimaryCard` and `deleteCard`) |
| `token` | `String` | Saved card token |
| `maskedPan` | `String` | Masked card number (e.g. `"XXXX-XXXX-XXXX-1234"`) |
| `isPrimary` | `boolean` | Whether this is the active billing card |
| `failedAttempts` | `int` | Number of consecutive failed billing attempts |
| `createdAt` | `String` | ISO 8601 timestamp when the card was saved |

## SubscriptionListRequest Fields

| Field | Type | Description |
|-------|------|-------------|
| `transaction` | `Long` | Filter by related transaction ID |
| `state` | `String` | Filter by state: `"active"`, `"suspended"`, `"canceled"` |
| `planId` | `Long` | Filter by plan ID |
| `startsAt` | `String` | Filter by start date |
| `nextBilling` | `String` | Filter by next billing date |
| `reminderDate` | `String` | Filter by reminder date |
| `endsAt` | `String` | Filter by end date |
