# Subscription Plans API Reference

**Class:** `com.paymob.sdk.services.subscription.plan.SubscriptionPlanService`
**Accessed via:** `client.subscriptionPlans()`
**Auth:** Bearer Token (API Key)

Manage billing plans that customers can be enrolled into.

---

## Methods

### `create(SubscriptionPlanRequest request) → SubscriptionPlanResponse`

```java
SubscriptionPlanResponse plan = client.subscriptionPlans().create(
    SubscriptionPlanRequest.builder()
        .name("Monthly Pro")
        .amountCents(5000)
        .frequency(30)
        .planType("recurring")
        .isActive(true)
        .integration(integrationId)
        .build());
```

### `list() → SubscriptionPlansPage`

```java
SubscriptionPlansPage allPlans = client.subscriptionPlans().list();
```

### `retrieve(long planId) → SubscriptionPlanResponse`

```java
SubscriptionPlanResponse plan = client.subscriptionPlans().retrieve(planId);
```

### `update(long planId, SubscriptionPlanUpdateRequest request) → SubscriptionPlanResponse`

```java
SubscriptionPlanResponse updated = client.subscriptionPlans().update(planId,
    SubscriptionPlanUpdateRequest.builder()
        .amountCents(14900)
        .build());
```

### `suspend(long planId) → SubscriptionPlanResponse`

Suspends the plan. No new enrollments are accepted, but existing subscriptions are unaffected.

```java
client.subscriptionPlans().suspend(planId);
```

### `resume(long planId) → SubscriptionPlanResponse`

```java
client.subscriptionPlans().resume(planId);
```

---

## SubscriptionPlanRequest Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `name` | `String` | ✅ | Display name of the plan |
| `amountCents` | `int` | ✅ | Recurring charge amount in cents |
| `frequency` | `int` | ✅ | Billing interval in days (e.g. `30` for monthly) |
| `planType` | `String` | ✅ | Type of plan (e.g. `"recurring"`, `"rent"`) |
| `isActive` | `boolean` | No | Whether the plan accepts new subscriptions. Default: `true` |
| `integration` | `int` | ✅ | Integration ID to charge with |
