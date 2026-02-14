# Paymob Integration Flow

This document visualizes the standard Paymob API flow implemented in this SDK.

## High Level Overview

```mermaid
sequenceDiagram
    participant Merchant as Merchant Server
    participant Paymob as Paymob API
    participant User as Customer Browser/App

    Note over Merchant, Paymob: 1. Authentication
    Merchant->>Paymob: Authenticate (API Key)
    Paymob-->>Merchant: Auth Token

    Note over Merchant, Paymob: 2. Create Order
    Merchant->>Paymob: Create Order (Token, Amount, MerchantOrderID)
    Paymob-->>Merchant: Order ID

    Note over Merchant, Paymob: 3. Request Payment Key
    Merchant->>Paymob: Request Key (Token, OrderID, BillingData, IntegrationID)
    Paymob-->>Merchant: Payment Key (Token)

    Note over Merchant, User: 4. Process Payment
    Merchant->>User: Display Payment Interface (iFrame/SDK) with Payment Key
    User->>Paymob: Enter Card Details / Pay
    Paymob-->>User: Transaction Result
    Paymob-->>Merchant: Webhook / Callback (Transaction Data)
```

## Detailed Steps

### 1. Authentication
Obtain an authentication token using your API key. This token is required for all subsequent requests.
*   **SDK Method**: `AuthService.authenticate()`
*   **Input**: API Key (configured in `Paymob.init()`)
*   **Output**: `token` (String)

### 2. Order Registration
Register an order on Paymob system.
*   **SDK Method**: `OrderService.createOrder()`
*   **Input**: Auth Token, Amount (in cents), Currency, Merchant Order ID
*   **Output**: `id` (Order ID)

### 3. Payment Key Request
Generate a payment key for a specific order. This key encapsulates the amount and order details.
*   **SDK Method**: `PaymentService.requestPaymentKey()`
*   **Input**: Auth Token, Order ID, Amount, Billing Data, Integration ID
*   **Output**: `token` (Payment Key)

### 4. Transaction Handling
Use the Payment Key to render the payment interface (iFrame or Mobile SDK) on the client side.
*   After payment, Paymob redirects the user and sends a callback (webhook) to the merchant server.
*   **SDK Method**: `TransactionService.getTransaction()` can be used to query transaction status using the Transaction ID received in the callback.

---

## Unified Checkout Flow (Intention API)

This flow simplifies the process into a single step: Creating an Intention.

```mermaid
sequenceDiagram
    participant Merchant as Merchant Server
    participant Paymob as Paymob API
    participant User as Customer Browser

    Note over Merchant, Paymob: 1. Create Intention
    Merchant->>Paymob: POST /v1/intention/ (Secret Key)
    Paymob-->>Merchant: Intention Created (Client Secret)

    Note over Merchant, User: 2. Redirect User
    Merchant->>User: Redirect to Unified Checkout URL
    User->>Paymob: Complete Payment
    Paymob-->>User: Success/Failure Page
    Paymob-->>Merchant: Webhook (Transaction Data)
```

### Steps
1.  **Create Intention**: Call `IntentionService.createIntention()` with amount, currency, and billing data.
2.  **Redirect**: Generate the redirect URL using `IntentionService.getUnifiedCheckoutUrl(clientSecret)`.

---

## Transaction Control Flow

Manage your transactions after they are created.

```mermaid
sequenceDiagram
    participant Merchant as Merchant Server
    participant Paymob as Paymob API

    Note over Merchant, Paymob: Refund
    Merchant->>Paymob: POST /api/acceptance/void_refund/refund (Secret Key)
    Paymob-->>Merchant: Refund Successful

    Note over Merchant, Paymob: Void
    Merchant->>Paymob: POST /api/acceptance/void_refund/void (Secret Key)
    Paymob-->>Merchant: Void Successful

    Note over Merchant, Paymob: Capture
    Merchant->>Paymob: POST /api/acceptance/capture (Secret Key)
    Paymob-->>Merchant: Capture Successful
```
