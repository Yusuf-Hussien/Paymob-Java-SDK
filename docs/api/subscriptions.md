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

```java
List<SubscriptionCardResponse> cards = client.subscriptions().listCards(subscriptionId);
client.subscriptions().changePrimaryCard(subscriptionId, cards.get(1).getId());
client.subscriptions().deleteCard(subscriptionId, cardId);
```

---

## SubscriptionListRequest Fields

| Field | Type | Description |
|-------|------|-------------|
| `state` | `String` | Filter by state: `"active"`, `"suspended"`, `"canceled"` |
| `planId` | `Long` | Filter by plan ID |
| `startsAt` | `String` | Filter by start date |
| `nextBilling` | `String` | Filter by next billing date |
| `endsAt` | `String` | Filter by end date |
