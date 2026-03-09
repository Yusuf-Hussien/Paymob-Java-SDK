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
| `integration` | `int` | ✅ | Integration ID to charge with |
| `isActive` | `boolean` | No | Whether the plan accepts new subscriptions. Default: `true` |
| `reminderDays` | `int` | No | Days before next billing to send the customer a reminder |
| `retrialDays` | `int` | No | Days to retry a failed deduction before suspending the subscription |
| `numberOfDeductions` | `Integer` | No | Maximum number of automatic billing cycles. Unlimited if omitted |
| `useTransactionAmount` | `boolean` | No | If `true`, uses the initial transaction amount instead of `amountCents` for recurring charges. Default: `false` |
| `webhookUrl` | `String` | No | Per-plan webhook URL override for billing events on this plan |

## SubscriptionPlanUpdateRequest Fields

Only the fields you include are updated; omitted fields are left unchanged.

| Field | Type | Required | Description |
|-------|------|----------|--------------|
| `amountCents` | `Integer` | No | New recurring charge amount in cents |
| `numberOfDeductions` | `Integer` | No | New maximum billing cycle limit |
| `integration` | `Integer` | No | New integration ID to charge with |

## SubscriptionPlanResponse Fields

| Field | Type | Description |
|-------|------|-------------|
| `id` | `long` | Plan ID |
| `name` | `String` | Display name |
| `amountCents` | `Integer` | Recurring charge amount in cents |
| `frequency` | `Integer` | Billing interval in days |
| `planType` | `String` | Plan type (`"recurring"`, `"rent"`) |
| `isActive` | `Boolean` | Whether the plan currently accepts new subscriptions |
| `integration` | `Integer` | Integration ID |
| `reminderDays` | `Integer` | Days before billing to send reminder |
| `retrialDays` | `Integer` | Days to retry after a failed deduction |
| `numberOfDeductions` | `Integer` | Maximum billing cycles (`null` = unlimited) |
| `useTransactionAmount` | `Boolean` | Whether initial transaction amount drives recurring charges |
| `webhookUrl` | `String` | Per-plan webhook URL (if configured) |
| `createdAt` | `String` | ISO 8601 creation timestamp |
| `updatedAt` | `String` | ISO 8601 last-updated timestamp |
