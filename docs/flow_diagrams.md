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
