# Schemas and Mandatory Fields

This page provides a quick SDK-first view of common request/response models and required fields.

## 1. Intention

### Request: `IntentionRequest`

| Field | Type | Mandatory | Notes |
|-------|------|-----------|-------|
| `amount` | `int` | Yes | In cents; should match sum of item totals |
| `currency` | `Currency` | Yes | `EGP`, `SAR`, `AED`, `OMR` |
| `paymentMethods` | `List<Integer>` | Yes (create) | Integration IDs |
| `billingData` | `BillingData` | Yes | Required customer billing fields |
| `items` | `List<Item>` | Recommended | Used for amount consistency |
| `specialReference` | `String` | No | Merchant reference |
| `notificationUrl` | `String` | No | Per-request webhook URL |
| `redirectionUrl` | `String` | No | Post-payment redirect |
| `cardTokens` | `List<String>` | No | Saved-card checkout scenarios |
| `subscriptionPlanId` | `Long` | No | Subscription enrollment |
| `subscriptionv2Id` | `Long` | No | Add secondary card to subscription |
| `acceptOrderId` | `Long` | No | Existing Paymob order linkage |
| `extras` | `Map<String, Object>` | No | Metadata |

### Response: `IntentionResponse`

| Field | Type | Notes |
|-------|------|-------|
| `id` | `String` | Intention ID |
| `clientSecret` | `String` | Used to build checkout URL and retrieve intention |
| `status` | `String` | Intention status |
| `confirmed` | `boolean` | Confirmation status |
| `intentionOrderId` | `Long` | Paymob order ID |
| `created` | `String` | Timestamp |

## 2. Saved Cards

### Request: `CitPaymentRequest`

| Field | Type | Mandatory | Notes |
|-------|------|-----------|-------|
| `cardToken` | `String` | Yes | Token from `CARD_TOKEN` webhook |
| `cvv` | `String` | Yes | Customer-provided CVV |
| `amount` | `int` | Yes | In cents |
| `currency` | `String` | Yes | 3-letter code |

### Request: `MitPaymentRequest`

| Field | Type | Mandatory | Notes |
|-------|------|-----------|-------|
| `cardToken` | `String` | Yes | Saved card token |
| `amount` | `int` | Yes | In cents |
| `currency` | `String` | Yes | 3-letter code |
| `merchantOrderId` (`special_reference`) | `String` | No | Strongly recommended unique reference |

### Request: `MotoCardPayRequest`

| Field | Type | Mandatory | Notes |
|-------|------|-----------|-------|
| `source.identifier` | `String` | Yes | Saved card token |
| `source.subtype` | `String` | Yes | Must be `TOKEN` |
| `paymentToken` | `String` | Yes | Payment token from prior intention flow |

### Response: `TokenizedPaymentResponse`

| Field | Type | Notes |
|-------|------|-------|
| `success` | `boolean` | Operation status |
| `message` | `String` | Optional status message |
| `transactionId` | `long` | Related transaction ID |
| `clientSecret` | `String` | Inherited from `IntentionResponse` |

## 3. Transactions (Refund, Void, Capture)

### Request: `RefundRequest`

| Field | Type | Mandatory | Notes |
|-------|------|-----------|-------|
| `transactionId` | `long` | Yes | Target transaction |
| `amountCents` | `int` | Recommended | Set explicit value for partial/full refund clarity |

### Request: `VoidRequest`

| Field | Type | Mandatory | Notes |
|-------|------|-----------|-------|
| `transactionId` | `long` | Yes | Target transaction |

### Request: `CaptureRequest`

| Field | Type | Mandatory | Notes |
|-------|------|-----------|-------|
| `transactionId` | `long` | Yes | Authorized transaction |
| `amountCents` | `int` | Yes | Capture amount in cents |

### Response: `TransactionResponse`

| Field | Type | Notes |
|-------|------|-------|
| `id` | `Long` | Transaction ID |
| `success` | `Boolean` | Success flag |
| `pending` | `Boolean` | Pending state |
| `amountCents` | `Integer` | Amount in cents |
| `currency` | `String` | Currency code |
| `isVoided` | `Boolean` | Void status |
| `isRefunded` | `Boolean` | Refund status |
| `order` | `Transaction.OrderInfo` | Linked order object |
| `sourceData` | `Map<String, Object>` | Card/payment source metadata |

## 4. Mandatory BillingData Fields for Intention Flows

| Field | Mandatory |
|-------|-----------|
| `firstName` | Yes |
| `lastName` | Yes |
| `email` | Yes |
| `phoneNumber` | Yes |
| `country` | Yes |
| `city` | Yes |
| `street` | Yes |

## 5. Subscription Plans

### Request: `SubscriptionPlanRequest`

| Field | Type | Mandatory | Notes |
|-------|------|-----------|-------|
| `name` | `String` | Yes | Display name |
| `amountCents` | `int` | Yes | Recurring charge in cents |
| `frequency` | `int` | Yes | Billing interval in days |
| `planType` | `String` | Yes | e.g. `"recurring"`, `"rent"` |
| `integration` | `int` | Yes | Integration ID to charge with |
| `isActive` | `boolean` | No | Accepts new subscriptions. Default: `true` |
| `reminderDays` | `int` | No | Days before billing to send reminder |
| `retrialDays` | `int` | No | Days to retry after failed deduction |
| `numberOfDeductions` | `Integer` | No | Max billing cycles (unlimited if omitted) |
| `useTransactionAmount` | `boolean` | No | Use initial transaction amount for recurrences. Default: `false` |
| `webhookUrl` | `String` | No | Per-plan webhook override |

### Request: `SubscriptionPlanUpdateRequest`

| Field | Type | Mandatory | Notes |
|-------|------|-----------|-------|
| `amountCents` | `Integer` | No | New recurring charge amount |
| `numberOfDeductions` | `Integer` | No | New max billing cycles |
| `integration` | `Integer` | No | New integration ID |

### Response: `SubscriptionPlanResponse`

| Field | Type | Notes |
|-------|------|-------|
| `id` | `long` | Plan ID |
| `name` | `String` | Display name |
| `amountCents` | `Integer` | Recurring charge in cents |
| `frequency` | `Integer` | Billing interval in days |
| `planType` | `String` | Plan type |
| `isActive` | `Boolean` | Accepting new subscriptions |
| `integration` | `Integer` | Integration ID |
| `reminderDays` | `Integer` | Days before billing for reminder |
| `retrialDays` | `Integer` | Days to retry after failure |
| `numberOfDeductions` | `Integer` | Max billing cycles (`null` = unlimited) |
| `useTransactionAmount` | `Boolean` | Uses initial transaction amount for recurrences |
| `webhookUrl` | `String` | Per-plan webhook URL |
| `createdAt` | `String` | ISO 8601 creation timestamp |
| `updatedAt` | `String` | ISO 8601 last-updated timestamp |

## 6. Subscriptions

### Request: `SubscriptionUpdateRequest`

| Field | Type | Mandatory | Notes |
|-------|------|-----------|-------|
| `amountCents` | `Integer` | No | Override recurring charge for this subscription |
| `endsAt` | `String` | No | ISO 8601 end date |

### Response: `SubscriptionResponse`

| Field | Type | Notes |
|-------|------|-------|
| `id` | `long` | Subscription ID |
| `state` | `String` | `"active"`, `"suspended"`, `"canceled"` |
| `amountCents` | `int` | Current recurring charge in cents |
| `frequency` | `int` | Billing interval in days |
| `planId` | `long` | Associated plan ID |
| `startsAt` | `String` | ISO 8601 start date |
| `nextBilling` | `String` | ISO 8601 next billing date |

### Response: `SubscriptionCardResponse`

| Field | Type | Notes |
|-------|------|-------|
| `id` | `long` | Card record ID |
| `token` | `String` | Saved card token |
| `maskedPan` | `String` | Masked card number |
| `isPrimary` | `boolean` | Whether this is the active billing card |
| `failedAttempts` | `int` | Consecutive failed billing attempts |
| `createdAt` | `String` | ISO 8601 timestamp |

## 7. Transaction Inquiry

### Response: `InquiryResponse` (extends `Transaction`)

| Field | Type | Notes |
|-------|------|-------|
| `id` | `long` | Transaction ID |
| `success` | `boolean` | Transaction success flag |
| `pending` | `boolean` | Pending status |
| `amountCents` | `int` | Amount in cents |
| `currency` | `String` | Currency code |
| `isVoided` | `boolean` | Voided status |
| `isRefunded` | `boolean` | Refunded status |
| `isCaptured` | `boolean` | Captured status |
| `isAuth` | `boolean` | Authorization-only flag |
| `is3dSecure` | `boolean` | 3D Secure flag |
| `isLive` | `boolean` | Live vs sandbox |
| `integrationId` | `int` | Integration ID used |
| `merchantOrderId` | `String` | Merchant order reference |
| `createdAt` | `String` | ISO 8601 creation timestamp |
| `sourceData` | `Map<String, Object>` | Payment source metadata |
| `order` | `Transaction.OrderInfo` | Nested order details |

## 8. Quick Links

### Response: `QuickLinkResponse`

| Field | Type | Notes |
|-------|------|-------|
| `id` | `long` | Quick link ID |
| `clientUrl` | `String` | Shareable payment URL |
| `shortenUrl` | `String` | Shortened URL (if available) |
| `state` | `String` | `"active"`, `"canceled"`, `"paid"` |
| `amountCents` | `int` | Payment amount in cents |
| `currency` | `String` | Currency code |
| `description` | `String` | Payment description |
| `referenceId` | `String` | Merchant reference |
| `clientInfo` | `ClientInfo` | Customer details (fullName, email, phoneNumber) |
| `createdAt` | `String` | ISO 8601 creation timestamp |
| `expiresAt` | `String` | ISO 8601 expiry timestamp |
| `paidAt` | `String` | ISO 8601 paid timestamp |
| `order` | `Long` | Paymob order ID (after payment) |

## Notes

- Amount values are always in minor currency units (for example, cents).
- For production safety, prefer explicitly setting all relevant monetary and reference fields.
- Use webhook callbacks as source of truth for final payment status.
