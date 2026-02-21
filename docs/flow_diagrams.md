# Paymob Integration Flows

This document provides comprehensive visualizations of all Paymob API flows implemented in the Java SDK.

## 📋 Table of Contents

- [Payment Intention Flow](#payment-intention-flow-recommended)
- [Legacy Standard Flow](#legacy-standard-flow-auth--order--key)
- [Saved Card Flows](#saved-card-flows-cit--mit)
- [Subscription Lifecycle](#subscription-lifecycle)
- [Transaction Management](#transaction-management)
- [Transaction Inquiry](#transaction-inquiry)
- [Quick Link Generation](#quick-link-generation)
- [Webhook Processing](#webhook-processing)

## 💳 Payment Intention Flow (Recommended)

The modern unified checkout that supports all payment methods through a single API call.

```mermaid
flowchart TD
    A[Customer Initiates Payment] --> B[Create Intention Request]
    B --> C[POST /v1/intention/]
    C --> D{Paymob Validates}
    D -->|Success| E[Return Intention Response]
    D -->|Error| F[Return Error Details]
    
    E --> G[Extract payment_url]
    G --> H[Redirect Customer to Paymob]
    H --> I[Customer Selects Payment Method]
    I --> J{Payment Methods}
    J -->|Card| K[Enter Card Details]
    J -->|Wallet| L[Enter Wallet Number]
    J -->|Kiosk| M[Show Kiosk Instructions]
    
    K --> N[3DS Verification if Required]
    L --> O[Wallet Confirmation]
    M --> P[Kiosk Payment]
    
    N --> Q[Payment Processing]
    O --> Q
    P --> Q
    
    Q --> R{Payment Result}
    R -->|Success| S[Send Success Webhook]
    R -->|Failure| T[Send Failure Webhook]
    
    S --> U[Redirect to Success Page]
    T --> V[Redirect to Failure Page]
    
    style A fill:#e1f5fe
    style E fill:#e8f5e8
    style S fill:#e8f5e8
    style F fill:#ffebee
    style T fill:#ffebee
```

### Key Components

**Request Parameters:**
- `amount`: Payment amount in cents
- `currency`: 3-letter currency code (EGP, SAR, AED, OMR)
- `integration_ids`: List of payment method IDs
- `billing_data`: Customer information (name, email, phone)
- `items`: Optional line items for detailed invoices
- `expiration`: Optional payment link expiration

**Response Fields:**
- `id`: Unique intention identifier
- `client_secret`: Secret for hosted checkout
- `payment_url`: Hosted payment page URL

### Error Handling

| HTTP Code | Cause | Resolution |
|-----------|-------|------------|
| 401 | Invalid secret key | Verify credentials and region |
| 406 | Invalid request data | Check amount, currency, billing data |
| 404 | Integration ID not found | Verify integration exists in region |
| 500 | Paymob server error | Implement retry logic |

## 🔧 Legacy Standard Flow (Auth → Order → Key)

> **Note:** This flow is maintained for backward compatibility. New integrations should use the Payment Intention flow.

```mermaid
sequenceDiagram
    participant Merchant as Merchant Server
    participant Paymob as Paymob API
    participant User as Customer Browser

    Note over Merchant, Paymob: 1. Authentication
    Merchant->>Paymob: POST /api/auth/tokens<br/>{api_key: "..."}
    Paymob-->>Merchant: 200 OK<br/>{token: "bearer_token"}

    Note over Merchant, Paymob: 2. Create Order
    Merchant->>Paymob: POST /api/ecommerce/orders<br/>Auth: Bearer token<br/>{amount_cents, currency, merchant_order_id}
    Paymob-->>Merchant: 201 Created<br/>{id: order_id}

    Note over Merchant, Paymob: 3. Request Payment Key
    Merchant->>Paymob: POST /api/acceptance/payment_keys<br/>Auth: Bearer token<br/>{order_id, amount_cents, integration_id, billing_data}
    Paymob-->>Merchant: 201 Created<br/>{token: payment_key}

    Note over Merchant, User: 4. Process Payment
    Merchant->>User: Render iFrame with payment_key
    User->>Paymob: Submit payment details
    Paymob-->>User: 3DS challenge if required
    User->>Paymob: Complete 3DS
    Paymob-->>User: Payment result
    Paymob-->>Merchant: Webhook with transaction data
```

### When to Use Legacy Flow

- **Existing integrations** using the classic iFrame approach
- **Custom payment UI** requiring direct iframe embedding
- **Specific compliance requirements** needing the 3-step process

### Migration Path

To migrate from Legacy to Intention flow:

1. Replace 3-step process with single `IntentionService.create()` call
2. Update webhook handler to use new event structure
3. Remove iframe embedding code - use hosted checkout URL
4. Update error handling for new response format

## 💳 Saved Card Flows (CIT vs MIT)

Two distinct flows for tokenized payments based on customer presence.

```mermaid
flowchart TD
    A[Saved Card Payment Initiated] --> B{Customer Present?}
    
    B -->|Yes| C[CIT - Customer Initiated]
    B -->|No| D[MIT - Merchant Initiated]
    
    C --> E[Collect CVV from Customer]
    E --> F[POST /api/acceptance/payments/pay]
    F --> G{CVV Valid?}
    G -->|Yes| H[Process Payment]
    G -->|No| I[Return CVV Error]
    
    D --> J[POST /api/acceptance/payments/pay]
    J --> K[Verify Token Validity]
    K --> L[Process Payment]
    
    H --> M{Payment Result}
    L --> M
    
    M -->|Success| N[Send Success Webhook]
    M -->|Failure| O[Send Failure Webhook]
    
    I --> P[Request New CVV]
    P --> E
    
    style C fill:#e3f2fd
    style D fill:#f3e5f5
    style N fill:#e8f5e8
    style O fill:#ffebee
```

### CIT (Customer Initiated Transaction)

**Use Cases:**
- Customer saves card during checkout
- Customer manually selects saved card
- Subscription payments with customer interaction

**Required Fields:**
- `token`: Saved card token
- `cvv`: 3-4 digit CVV (collected from customer)
- `order_id`: Paymob order ID
- `amount_cents`: Payment amount
- `currency`: Currency code

**Security Note:** CVV is never stored - only used for the current transaction.

### MIT (Merchant Initiated Transaction)

**Use Cases:**
- Recurring subscription charges
- Automatic top-ups
- Late payment processing
- Installment payments

**Required Fields:**
- `token`: Saved card token
- `order_id`: Paymob order ID
- `amount_cents`: Payment amount
- `currency`: Currency code

**Security Note:** No CVV required - relies on previously authenticated token.

### Token Lifecycle

```mermaid
stateDiagram-v2
    [*] --> First Payment
    First Payment --> 3DS Verification
    3DS Verification --> Token Generated
    Token Generated --> CIT Available
    Token Generated --> MIT Available
    
    CIT Available --> CIT Payment
    MIT Available --> MIT Payment
    
    CIT Payment --> Token Refresh
    MIT Payment --> Token Refresh
    
    Token Refresh --> CIT Available
    Token Refresh --> MIT Available
    
    Token Generated --> Token Expired
    Token Expired --> [*]
    
    note right of Token Generated
        Token valid for
        recurring payments
    end note
```

## 🔄 Subscription Lifecycle

Complete subscription management from plan creation to cancellation.

```mermaid
stateDiagram-v2
    [*] --> Plan Created
    Plan Created --> Active Subscription
    
    Active Subscription --> Billing Cycle
    Billing Cycle --> Payment Attempt
    
    Payment Attempt --> Payment Success
    Payment Attempt --> Payment Failed
    
    Payment Success --> Next Billing
    Payment Failed --> Retrial Period
    
    Retrial Period --> Payment Attempt
    Retrial Period --> Subscription Suspended
    
    Next Billing --> Billing Cycle
    
    Active Subscription --> Manual Suspend
    Subscription Suspended --> Manual Resume
    Subscription Suspended --> Auto Cancel
    
    Manual Resume --> Active Subscription
    Manual Suspend --> Subscription Suspended
    
    Active Subscription --> Manual Cancel
    Subscription Suspended --> Manual Cancel
    Manual Cancel --> [*]
    Auto Cancel --> [*]
```

### Plan Creation Flow

```mermaid
sequenceDiagram
    participant Merchant as Merchant Server
    participant Paymob as Paymob API

    Note over Merchant, Paymob: Create Subscription Plan
    Merchant->>Paymob: POST /api/plan/<br/>Auth: Bearer token<br/>{name, amount_cents, currency, billing_cycle}
    Paymob-->>Merchant: 201 Created<br/>{id: plan_id}

    Note over Merchant, Paymob: Subscribe Customer
    Merchant->>Paymob: POST /api/subscription/<br/>Auth: Bearer token<br/>{plan_id, customer_id, integration_id}
    Paymob-->>Merchant: 201 Created<br/>{id: subscription_id, status: active}
```

### Billing Cycle Management

| Billing Cycle | Frequency | Common Use Cases |
|---------------|-----------|------------------|
| WEEKLY | Every 7 days | Digital content, services |
| BIWEEKLY | Every 14 days | Payroll, mid-tier services |
| MONTHLY | Every 30 days | SaaS, subscriptions |
| QUARTERLY | Every 90 days | Premium services |
| ANNUAL | Every 365 days | Enterprise licenses |

### Retrial Logic Configuration

- **Retrial Days**: Number of days to retry failed payments
- **Reminder Days**: Days before billing to send reminders
- **Max Retries**: Maximum retry attempts before suspension

### Webhook Events for Subscriptions

- `SUBSCRIPTION_CREATED`: New subscription activated
- `SUBSCRIPTION_RENEWED`: Successful recurring payment
- `SUBSCRIPTION_FAILED`: Payment retry attempt failed
- `SUBSCRIPTION_SUSPENDED**: Subscription suspended due to failures
- `SUBSCRIPTION_RESUMED`: Subscription reactivated
- `SUBSCRIPTION_CANCELLED`: Subscription terminated

## 🔀 Transaction Management

Post-payment operations for refunds, voids, and captures.

```mermaid
flowchart TD
    A[Transaction Operation Request] --> B{Operation Type}
    
    B -->|Refund| C[Refund Request]
    B -->|Void| D[Void Request]
    B -->|Capture| E[Capture Request]
    
    C --> F[POST /api/acceptance/void_refund/refund]
    D --> G[POST /api/acceptance/void_refund/void]
    E --> H[POST /api/acceptance/capture]
    
    F --> I{Validate Refund}
    G --> J{Validate Void}
    E --> K{Validate Capture}
    
    I -->|Valid| L[Process Refund]
    I -->|Invalid| M[Refund Error]
    
    J -->|Valid| N[Process Void]
    J -->|Invalid| O[Void Error]
    
    K -->|Valid| P[Process Capture]
    K -->|Invalid| Q[Capture Error]
    
    L --> R[Refund Successful]
    N --> S[Void Successful]
    P --> T[Capture Successful]
    
    M --> U[Return Error Details]
    O --> U
    Q --> U
    
    style L fill:#e8f5e8
    style N fill:#e8f5e8
    style P fill:#e8f5e8
    style M fill:#ffebee
    style O fill:#ffebee
    style Q fill:#ffebee
```

### Operation Details

#### Refund
- **Purpose**: Reverse a completed transaction (full or partial)
- **Timing**: Any time after settlement
- **Required**: `transaction_id`, optional `amount_cents`
- **Note**: If no amount specified, full refund is processed

#### Void
- **Purpose**: Cancel a same-day transaction before settlement
- **Timing**: Same day only (before batch processing)
- **Required**: `transaction_id`
- **Benefit**: No processing fees compared to refund

#### Capture
- **Purpose**: Finalize an authorized-only transaction
- **Timing**: Within 14 days of authorization
- **Required**: `transaction_id`, `amount_cents`, `authorisation_date`
- **Validation**: Authorization date must be within 14-day window

## 🔍 Transaction Inquiry

Flexible transaction lookup by multiple identifiers.

```mermaid
flowchart TD
    A[Transaction Inquiry Request] --> B{Search By}
    
    B -->|Merchant Order ID| C[GET /api/ecommerce/orders/transaction_inquiry<br/>?merchant_order_id=...]
    B -->|Paymob Order ID| D[GET /api/ecommerce/orders/transaction_inquiry<br/>?order_id=...]
    B -->|Transaction ID| E[GET /api/ecommerce/orders/transaction_inquiry<br/>?transaction_id=...]
    
    C --> F[Search by Merchant Reference]
    D --> G[Search by Paymob Order]
    E --> H[Search by Transaction]
    
    F --> I[Return Transaction Details]
    G --> I
    H --> I
    
    I --> J{Transaction Found?}
    J -->|Yes| K[Return Transaction Data]
    J -->|No| L[Return Not Found]
    
    style K fill:#e8f5e8
    style L fill:#ffebee
```

### Query Parameters

| Parameter | Type | Description | Example |
|-----------|------|-------------|---------|
| `merchant_order_id` | String | Your internal order reference | `ORDER-12345` |
| `order_id` | Integer | Paymob's internal order ID | `123456789` |
| `transaction_id` | Integer | Specific transaction ID | `987654321` |

### Response Data

```json
{
  "id": 987654321,
  "order_id": 123456789,
  "merchant_order_id": "ORDER-12345",
  "amount_cents": 10000,
  "currency": "EGP",
  "status": "SUCCESS",
  "gateway_response_code": "APPROVED",
  "gateway_response_message": "Transaction successful",
  "created_at": "2025-01-15T10:30:00Z"
}
```

## 🔗 Quick Link Generation

Create shareable payment links without full checkout sessions.

```mermaid
sequenceDiagram
    participant Merchant as Merchant Server
    participant Paymob as Paymob API
    participant Customer as End Customer

    Note over Merchant, Paymob: Create Quick Link
    Merchant->>Paymob: POST /v1/ecommerce/quick-link<br/>Auth: Token secret_key<br/>{amount, currency, title, integration_id}
    Paymob-->>Merchant: 201 Created<br/>{id, url, expiration_date}

    Note over Merchant, Customer: Share Payment Link
    Merchant->>Customer: Send link via email/SMS/QR
    Customer->>Paymob: Click link / Scan QR
    Paymob-->>Customer: Show payment page
    Customer->>Paymob: Complete payment
    Paymob-->>Merchant: Webhook with transaction result
```

### Use Cases

- **Invoice Payments**: Send payment links with invoices
- **Pay-by-Link**: Social media and messaging apps
- **QR Codes**: Physical locations and print materials
- **Email Collections**: Customer service and billing

### Quick Link Features

| Feature | Description |
|---------|-------------|
| **Custom Title** | Descriptive payment description |
| **Expiration** | Optional link expiry date |
| **Multi-Payment** | Link can be used multiple times |
| **Tracking** | Monitor link usage and conversions |
| **Branding** | Custom merchant branding on payment page |

## 🛡️ Webhook Processing

Secure webhook handling with HMAC signature verification.

```mermaid
flowchart TD
    A[Paymob Sends Webhook] --> B[Receive HTTP POST]
    B --> C[Extract Paymob-Hmac Header]
    C --> D[Compute Expected HMAC]
    D --> E{Signatures Match?}
    
    E -->|No| F[Reject Request - 401]
    E -->|Yes| G[Parse Webhook Body]
    
    G --> H[Identify Event Type]
    H --> I{Event Type}
    
    I -->|TRANSACTION_PROCESSED| J[Handle Successful Payment]
    I -->|TRANSACTION_FAILED| K[Handle Failed Payment]
    I -->|SUBSCRIPTION_RENEWED| L[Handle Subscription Renewal]
    I -->|REFUND_PROCESSED| M[Handle Refund]
    I -->|Other| N[Handle Generic Event]
    
    J --> O[Update Order Status]
    K --> P[Notify Customer]
    L --> Q[Extend Subscription]
    M --> R[Process Refund]
    N --> S[Log and Process]
    
    O --> T[Send 200 OK Response]
    P --> T
    Q --> T
    R --> T
    S --> T
    
    F --> U[Log Security Violation]
    
    style T fill:#e8f5e8
    style F fill:#ffebee
    style U fill:#fff3cd
```

### HMAC Verification Process

```java
// 1. Extract received HMAC
String receivedHmac = request.getHeader("Paymob-Hmac");

// 2. Concatenate request body values in Paymob order
StringBuilder data = new StringBuilder();
data.append(params.get("amount_cents"));
data.append(params.get("created_at"));
data.append(params.get("currency"));
data.append(params.get("error_occured"));
data.append(params.get("has_parent_transaction"));
data.append(params.get("id"));
data.append(params.get("integration_id"));
data.append(params.get("is_3d_secure"));
data.append(params.get("is_auth"));
data.append(params.get("is_capture"));
data.append(params.get("is_refunded"));
data.append(params.get("is_standalone_payment"));
data.append(params.get("is_voided"));
data.append(params.get("order_id"));
data.append(params.get("owner"));
data.append(params.get("pending"));
data.append(params.get("source_data_pan"));
data.append(params.get("source_data_sub_type"));
data.append(params.get("source_data_type"));
data.append(params.get("success"));

// 3. Compute HMAC-SHA512
String expectedHmac = HmacUtil.computeHmacSha512(data.toString(), hmacSecret);

// 4. Constant-time comparison
boolean isValid = MessageDigest.isEqual(
    expectedHmac.getBytes(StandardCharsets.UTF_8),
    receivedHmac.getBytes(StandardCharsets.UTF_8)
);
```

### Security Best Practices

- **Always verify HMAC** before processing any webhook
- **Use constant-time comparison** to prevent timing attacks
- **Log verification failures** for security monitoring
- **Respond quickly** with 200 OK to prevent retries
- **Idempotency**: Handle duplicate webhook deliveries gracefully

---

## 📚 Additional Resources

- **[Java SDK Documentation](../README.md)**: Complete API reference
- **[Paymob API Docs](https://docs.paymob.com)**: Official API documentation
- **[Production Guide](../Paymob-Java-SDK-Production-Guide.md)**: Deployment best practices
- **[Examples](../examples/)**: Ready-to-use code samples

For technical support or questions about these flows, please refer to the [Paymob Support Portal](https://support.paymob.com).
