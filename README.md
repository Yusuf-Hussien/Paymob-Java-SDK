# Paymob Java SDK

A Java SDK for integrating with the Paymob Payment Gateway.

## Features
- **Authentication**: Easy API Key authentication.
- **Orders**: Create and manage orders.
- **Payments**: Generate payment keys for various payment methods.
- **No External Dependencies**: Uses Java 11 `HttpClient`.

## Requirements
- Java 11 or later
- Maven 3.6+

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
