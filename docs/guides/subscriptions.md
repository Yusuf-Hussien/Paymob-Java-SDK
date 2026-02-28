# Subscriptions Guide

This guide covers the full subscription lifecycle — creating plans, enrolling customers, managing the subscription state, and handling recurring billing events.

## Overview

Paymob subscriptions work in two layers:

- **Plans** — define the billing terms (amount, frequency, integration). Managed via `client.subscriptionPlans()`.
- **Subscriptions** — represent a specific customer enrolled in a plan. Managed via `client.subscriptions()`.

Customer enrollment happens through the Intention API, not a direct subscription call. This gives the customer a hosted checkout page to save their card and make the first payment.

---

## 1. Create a Plan

Before enrolling customers, create at least one plan:

```java
import com.paymob.sdk.services.subscription.plan.SubscriptionPlanRequest;
import com.paymob.sdk.services.subscription.plan.SubscriptionPlanResponse;

SubscriptionPlanResponse plan = client.subscriptionPlans().create(
    SubscriptionPlanRequest.builder()
        .name("Monthly Pro")
        .amountCents(9900)       // 99.00 EGP per billing cycle
        .frequency(30)           // Bill every 30 days
        .planType("recurring")
        .isActive(true)
        .integration(integrationId)
        // --- Optional Fields ---
        .reminderDays(3)         // Days before deduction to send reminder
        .retrialDays(7)          // Days to retry after failed deduction
        .numberOfDeductions(12)  // Limit total possible automatic deductions
        .useTransactionAmount(false) // Whether to use amount from intitial transaction
        .webhookUrl("https://yoursite.com/webhook/plan") // Per-plan webhook override
        .build());

long planId = plan.getId();
```

### Plan Management

```java
// List all plans
SubscriptionPlansPage allPlans = client.subscriptionPlans().list();

// Retrieve a specific plan
SubscriptionPlanResponse plan = client.subscriptionPlans().retrieve(planId);

// Update plan details
SubscriptionPlanResponse updated = client.subscriptionPlans().update(planId,
    SubscriptionPlanUpdateRequest.builder()
        .amountCents(14900)             // Price change (Optional)
        .numberOfDeductions(24)         // Cycle limit change (Optional)
        .integration(integrationId)     // Integration change (Optional)
        .build());

// Suspend (no new enrollments allowed, existing subscriptions unaffected)
client.subscriptionPlans().suspend(planId);

// Resume a suspended plan
client.subscriptionPlans().resume(planId);
```

---

## 2. Enroll a Customer

Enrollment is done through the Intention API with `subscriptionPlanId` set. This creates a checkout session where the customer saves their card and pays the first billing cycle.

```java
import com.paymob.sdk.services.intention.IntentionRequest;
import com.paymob.sdk.services.intention.IntentionResponse;
import com.paymob.sdk.models.common.BillingData;
import com.paymob.sdk.models.common.Item;
import com.paymob.sdk.models.enums.Currency;
import java.util.List;

Item item = Item.builder()
    .name("Monthly Pro Subscription")
    .amount(9900)
    .quantity(1)
    .build();

BillingData billing = BillingData.builder()
    .firstName("Jane")
    .lastName("Smith")
    .email("jane@example.com")
    .phoneNumber("+201234567890")
    .country("EGY")
    .city("Cairo")
    .street("10 Nile St")
    .build();

IntentionResponse enrollment = client.subscriptions().subscribe(
    IntentionRequest.builder()
        .amount(9900)
        .currency(Currency.EGP)
        .paymentMethods(List.of(integrationId))
        .items(List.of(item))
        .billingData(billing)
        .subscriptionPlanId(planId)      // Links this checkout to the plan
        .specialReference("USER-456-SUB") // Optional: your internal reference
        .build());

// Redirect the customer to complete enrollment
String checkoutUrl = client.intentions().getUnifiedCheckoutUrl(enrollment);
```

Once the customer saves their card and pays, Paymob fires a `SUBSCRIPTION_CREATED` webhook with the new `subscriptionId`.

---

## 3. Listen for Subscription Webhooks

After enrollment and on every recurring billing event, Paymob sends a webhook. Handle them in your webhook endpoint:

```java
import com.paymob.sdk.services.subscription.SubscriptionResponse;

case SUBSCRIPTION_CREATED -> {
    SubscriptionResponse sub = event.getData(SubscriptionResponse.class);
    // Store sub.getId() linked to your customer
    db.saveSubscription(customerId, sub.getId(), sub.getState());
}

case SUBSCRIPTION_SUCCESSFUL_TRANSACTION -> {
    SubscriptionResponse sub = event.getData(SubscriptionResponse.class);
    // Extend the customer's access period
    db.extendAccess(sub.getId(), 30);
}

case SUBSCRIPTION_FAILED_TRANSACTION -> {
    SubscriptionResponse sub = event.getData(SubscriptionResponse.class);
    // Notify the customer their payment failed
    emailService.sendPaymentFailedEmail(sub.getId());
}

case SUBSCRIPTION_SUSPENDED -> {
    SubscriptionResponse sub = event.getData(SubscriptionResponse.class);
    // Revoke access until the subscription is resumed
    db.revokeAccess(sub.getId());
}

case SUBSCRIPTION_CANCELED -> {
    SubscriptionResponse sub = event.getData(SubscriptionResponse.class);
    db.markCanceled(sub.getId());
}
```

See the [Webhooks Guide](webhooks.md) for the complete list of subscription event types.

---

## 4. Manage Subscription Lifecycle

Once you have a `subscriptionId` (from the `SUBSCRIPTION_CREATED` webhook), you can manage it programmatically:

```java
// Retrieve subscription details
SubscriptionResponse sub = client.subscriptions().get(subscriptionId);
System.out.println(sub.getState());  // "active", "suspended", "canceled"

// Update subscription details
SubscriptionResponse updatedSub = client.subscriptions().update(subscriptionId,
    SubscriptionUpdateRequest.builder()
        .amountCents(12000)          // Custom subscription price (Optional)
        .endsAt("2025-12-31")        // Extends/shortens duration (Optional)
        .build());

// Suspend (stops recurring billing, customer loses access)
client.subscriptions().suspend(subscriptionId);

// Resume a suspended subscription
client.subscriptions().resume(subscriptionId);

// Cancel permanently (cannot be undone)
client.subscriptions().cancel(subscriptionId);

// List all subscriptions with filters
SubscriptionsPage active = client.subscriptions().list(
    SubscriptionListRequest.builder()
        .state("active")
        .planId(planId)
        // --- Optional Filters ---
        .transaction(123456L)
        .startsAt("2024-01-01")
        .nextBilling("2024-02-01")
        .reminderDate("2024-01-29")
        .endsAt("2024-12-31")
        .build());
```

---

## 5. Transaction History

```java
// Get the most recent transaction
SubscriptionTransactionResponse last = client.subscriptions().getLastTransaction(subscriptionId);

// Get full transaction history (paginated)
SubscriptionTransactionsPage history = client.subscriptions().listTransactions(subscriptionId);
```

---

## 6. Saved Card Management

Each subscription has one or more saved cards. You can allow customers to add a secondary card, change which card is primary, or remove a card.

### Add a Secondary Card

```java
// Creates a new checkout session for adding a second card
IntentionResponse addCard = client.subscriptions().addSecondaryCard(
    IntentionRequest.builder()
        .amount(0)  // Zero-amount intent just to tokenize the card
        .currency(Currency.EGP)
        .paymentMethods(List.of(integrationId))
        .items(List.of(/* placeholder item */))
        .billingData(billing)
        .subscriptionv2Id(subscriptionId)   // Links to existing subscription
        .build());

String addCardUrl = client.intentions().getUnifiedCheckoutUrl(addCard);
// Redirect customer to addCardUrl
```

### Change Primary Card

```java
// List available cards
List<SubscriptionCardResponse> cards = client.subscriptions().listCards(subscriptionId);
long newPrimaryCardId = cards.get(1).getId();

client.subscriptions().changePrimaryCard(subscriptionId, newPrimaryCardId);
```

### Delete a Card

```java
client.subscriptions().deleteCard(subscriptionId, cardId);
```

---

## Complete Lifecycle Reference

```
Plan Created (subscriptionPlans().create())
        │
        ▼
Customer Enrolls (subscriptions().subscribe())
        │   └── Checkout page → saves card → first payment
        ▼
SUBSCRIPTION_CREATED webhook fired
        │
        ▼
Recurring billing every N days
        ├── Success → SUBSCRIPTION_SUCCESSFUL_TRANSACTION
        └── Failure → SUBSCRIPTION_FAILED_TRANSACTION
                          └── (retries) → SUBSCRIPTION_SUSPENDED
                                              └── (manual) → suspend() / resume()
        │
        └── Manual management:
            ├── suspend(id)  → SUBSCRIPTION_SUSPENDED
            ├── resume(id)   → SUBSCRIPTION_RESUMED
            └── cancel(id)   → SUBSCRIPTION_CANCELED (permanent)
```
