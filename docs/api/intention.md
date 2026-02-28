# Intention API Reference

**Class:** `com.paymob.sdk.services.intention.IntentionService`
**Accessed via:** `client.intentions()`
**Auth:** Secret Key

The modern entry point for all payments. A single API call creates a checkout session supporting cards, wallets, kiosk, and more.

---

## Methods

### `createIntention(IntentionRequest request) → IntentionResponse`

Creates a new payment intention.

```java
IntentionResponse response = client.intentions().createIntention(request);
```

### `updateIntention(String clientSecret, IntentionRequest request) → IntentionResponse`

Updates an existing intention before it is paid. Only the fields included in the request are updated.

```java
IntentionResponse updated = client.intentions().updateIntention(clientSecret, updateRequest);
```

### `retrieveIntention(String clientSecret) → IntentionResponse`

Retrieves the current state of an intention.

```java
IntentionResponse details = client.intentions().retrieveIntention(clientSecret);
```

### `getUnifiedCheckoutUrl(IntentionResponse response) → String`

Generates the hosted checkout URL from a create/update response. Redirect your customer here.

```java
String url = client.intentions().getUnifiedCheckoutUrl(response);
```

### `getUnifiedCheckoutUrl(String clientSecret) → String`

Generates the hosted checkout URL directly from a client secret string.

```java
String url = client.intentions().getUnifiedCheckoutUrl(clientSecret);
```

---

## IntentionRequest Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `amount` | `int` | ✅ | Amount in cents. Must equal the sum of all item amounts |
| `currency` | `Currency` | ✅ | Payment currency (`EGP`, `SAR`, `AED`, `OMR`) |
| `paymentMethods` | `List<Integer>` | ✅ | List of integration IDs to offer at checkout |
| `items` | `List<Item>` | ✅ | Line items (name, amount, quantity) |
| `billingData` | `BillingData` | ✅ | Customer billing information |
| `specialReference` | `String` | ✅ | Your internal merchant order reference |
| `notificationUrl` | `String` | No | Webhook URL for this specific intention |
| `redirectionUrl` | `String` | ✅ | URL to redirect customer after payment |
| `cardTokens` | `List<String>` | No | Pre-selected saved card tokens |
| `subscriptionPlanId` | `Long` | No | Plan ID for subscription enrollment |
| `subscriptionv2Id` | `Long` | No | Subscription ID for adding a secondary card |
| `acceptOrderId` | `Long` | No | Paymob order ID when updating an intention |
| `extras` | `Map<String, Object>` | No | Custom metadata passed through to webhooks |

## IntentionResponse Fields

| Field | Type | Description |
|-------|------|-------------|
| `id` | `String` | Unique intention identifier |
| `clientSecret` | `String` | Secret used for checkout URL generation and retrieval |
| `intentionOrderId` | `Long` | Paymob internal order ID |
| `status` | `String` | Current status of the intention |
| `confirmed` | `boolean` | Whether the intention has been confirmed |
| `created` | `String` | ISO 8601 creation timestamp |
| `redirectionUrl` | `String` | Merchant redirection URL (if set) |
| `specialReference` | `String` | Merchant order reference (if set) |
| `extras` | `Map<String, Object>` | Custom metadata (if set) |
| `getAmount()` | `int` | Amount in cents (from nested detail) |
| `getCurrency()` | `String` | Currency code (from nested detail) |

## BillingData Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `firstName` | `String` | ✅ | Customer first name |
| `lastName` | `String` | ✅ | Customer last name |
| `email` | `String` | ✅ | Customer email address |
| `phoneNumber` | `String` | ✅ | Customer phone number (e.g. `+201234567890`) |
| `country` | `String` | ✅ | 3-letter country code (e.g. `EGY`) |
| `city` | `String` | ✅ | City name |
| `street` | `String` | ✅ | Street address |
| `apartment` | `String` | No | Apartment number |
| `floor` | `String` | No | Floor number |
| `building` | `String` | No | Building name/number |
| `postalCode` | `String` | No | Postal/ZIP code |
| `state` | `String` | No | State or province |

## Item Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `name` | `String` | ✅ | Item name |
| `amount` | `int` | ✅ | Unit price in cents |
| `quantity` | `int` | ✅ | Quantity (total = amount × quantity) |
| `description` | `String` | No | Optional item description |
