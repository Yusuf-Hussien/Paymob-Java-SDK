# Paymob Java SDK

A Java SDK for integrating with the Paymob Payment Gateway.

## Features
- **Authentication**: Easy API Key authentication.
- **Orders**: Create and manage orders.
- **Payments**: Generate payment keys for various payment methods.
- **No External Dependencies**: Uses Java 11 `HttpClient`.

## Documentation
- [Integration Flow & Diagrams](docs/flow_diagrams.md)
- [Paymob API Reference](https://docs.paymob.com)

## Requirements
- Java 11 or later
- Maven 3.6+
- A Paymob Merchant Account

## Getting Started: Credentials Manual

Before using the SDK, you need to obtain the following credentials from your Paymob Dashboard:

### 1. API Key
Used to authenticate your requests.
1.  Log in to your [Paymob Dashboard](https://accept.paymob.com/portal2/en/login).
2.  Navigate to **Settings** (gear icon) -> **Account Info**.
3.  Scroll down to the **API Key** section.
4.  Click **View** to reveal and copy your API Key.
    *   *Security Note: Never share this key publicly or commit it to version control.*

### 2. Integration ID
Identifies the payment method (e.g., Online Card, Mobile Wallet).
1.  Navigate to **Developers** -> **Payment Integrations**.
2.  Find the integration you want to use (e.g., "Online Card").
3.  Copy the **Integration ID** (a number like `123456`).
    *   *Note: You may need multiple IDs for different payment methods.*

### 3. iFrame ID (Optional)
Required if you are using the Paymob iFrame for card entry.
1.  Navigate to **Developers** -> **iFrames**.
2.  Create or select an existing iFrame.
3.  Copy the **iFrame ID**.

## Installation

### Maven
Add the following to your `pom.xml`:
```xml
<dependency>
    <groupId>com.paymob</groupId>
    <artifactId>paymob-java-sdk</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Usage

### 1. Initialization
Initialize the SDK with your API Key (from Paymob Dashboard).
```java
import com.paymob.sdk.Paymob;

Paymob.init("YOUR_API_KEY");
// Optional: Set timeout
Paymob.setTimeoutSeconds(60);
```

### 2. Authenticate
Get an Authentication Token to use for subsequent requests.
```java
import com.paymob.sdk.services.AuthService;
import com.paymob.sdk.models.auth.AuthResponse;

AuthService authService = new AuthService();
AuthResponse auth = authService.authenticate();
String authToken = auth.getToken();
```

### 3. Create an Order
```java
import com.paymob.sdk.services.OrderService;
import com.paymob.sdk.models.orders.OrderResponse;

OrderService orderService = new OrderService();
OrderResponse order = orderService.createOrder(
    authToken, 
    "10000", // amount in cents (100.00 EGP)
    "EGP", 
    "ORDER_12345" // Merchant Order ID
);
String orderId = String.valueOf(order.getId());
```

### 4. Generate Payment Key
```java
import com.paymob.sdk.services.PaymentService;
import com.paymob.sdk.models.payments.PaymentKeyRequest;
import com.paymob.sdk.models.payments.PaymentKeyResponse;

PaymentService paymentService = new PaymentService();
PaymentKeyRequest keyRequest = new PaymentKeyRequest();
keyRequest.setAuthToken(authToken);
keyRequest.setAmountCents("10000");
keyRequest.setExpiration(3600);
keyRequest.setOrderId(orderId);
keyRequest.setIntegrationId(123456); // From Paymob Dashboard
keyRequest.setCurrency("EGP");

// Billing Data is mandatory
PaymentKeyRequest.BillingData billingData = new PaymentKeyRequest.BillingData();
billingData.firstName = "John";
billingData.lastName = "Doe";
billingData.email = "john@example.com";
billingData.phoneNumber = "+201234567890";
keyRequest.setBillingData(billingData);

PaymentKeyResponse keyResponse = paymentService.requestPaymentKey(keyRequest);
String paymentToken = keyResponse.getToken();

// Use paymentToken in iFrame or mobile SDKs
```

## License
MIT
