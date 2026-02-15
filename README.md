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


### 1. Configuration
Initialize the SDK with your credentials. You can set the target **Region** (Egypt, UAE, KSA, Oman) to automatically configure the base URLs.

```java
import com.paymob.sdk.Paymob;
import com.paymob.sdk.models.Region;

// Initialize with V2 credential (Recommended for new integrations)
// API Key, Secret Key, Public Key, Region
Paymob.init("YOUR_API_KEY", "YOUR_SECRET_KEY", "YOUR_PUBLIC_KEY", Region.UAE);

// OR Initialize with V1 credentials (Legacy)
// Paymob.init("YOUR_API_KEY");
```

### 2. Unified Checkout (Intention API) - Recommended
The simplified flow to accept payments using a single step.

```java
import com.paymob.sdk.services.IntentionService;
import com.paymob.sdk.models.intention.*;

IntentionService intentionService = new IntentionService();

// 1. Create Intention
IntentionRequest request = new IntentionRequest();
request.setAmount(1000); // 10.00 (amount in cents)
request.setCurrency("AED");
request.setPaymentMethods(List.of(456789)); // Integration IDs

// Minimum required billing data
IntentionRequest.BillingData billingData = new IntentionRequest.BillingData();
billingData.setFirstName("John");
billingData.setLastName("Doe");
billingData.setEmail("john@example.com");
billingData.setPhoneNumber("+971500000000");
request.setBillingData(billingData);

IntentionResponse response = intentionService.createIntention(request);

// 2. Redirect User to Checkout Page
String checkoutUrl = intentionService.getUnifiedCheckoutUrl(response.getClientSecret());
System.out.println("Redirect User to: " + checkoutUrl);
```

### 3. Transaction Management
Control your transactions using the Secret Key.

```java
import com.paymob.sdk.services.TransactionService;

TransactionService txService = new TransactionService();

// Get Transaction Details
Object txDetails = txService.getTransaction(123456L, "bearer_token_if_needed");

// Void an Auth Transaction
txService.voidTransaction(123456L);

// Refund a Transaction
txService.refundTransaction(123456L, 1000); // Refund 10.00

// Capture an Auth Transaction
txService.captureTransaction(123456L, 1000); // Capture 10.00
```

### 4. Legacy Standard Flow (Auth -> Order -> Key)
Use this flow if you need granular control over steps or are using the classic iFrame integration.

#### Step 1: Authentication
```java
AuthService authService = new AuthService();
AuthResponse auth = authService.authenticate();
String authToken = auth.getToken();
```

#### Step 2: Create Order
```java
OrderService orderService = new OrderService();
OrderResponse order = orderService.createOrder(authToken, "10000", "EGP", "ORDER_123");
String orderId = String.valueOf(order.getId());
```

#### Step 3: Request Payment Key
```java
PaymentService paymentService = new PaymentService();
PaymentKeyRequest keyRequest = new PaymentKeyRequest();
keyRequest.setAuthToken(authToken);
keyRequest.setAmountCents("10000");
keyRequest.setOrderId(orderId);
// ... set billing data ...
PaymentKeyResponse keyResponse = paymentService.requestPaymentKey(keyRequest);
String paymentToken = keyResponse.getToken();
```


### 5. Callback Verification (HMAC)
Verify callback requests from Paymob (Webhooks) using your HMAC Secret.

```java
import com.paymob.sdk.Paymob;
import com.paymob.sdk.utils.HmacUtil;
import com.paymob.sdk.models.Region;

// 1. Initialize with HMAC Secret
Paymob.init("API_KEY", "SECRET_KEY", "PUBLIC_KEY", "HMAC_SECRET", Region.UAE);

// 2. In your Webhook Controller
public void handleWebhook(@RequestParam Map<String, String> params) {
    String hmac = params.get("hmac");
    
    // Concatenate values according to Paymob docs order
    // Example: amount_cents + created_at + currency + error_occured + ...
    // Note: You must implement the concatenation logic based on the specific callback type
    String data = params.get("amount_cents") + params.get("created_at") /* ... */;
    
    boolean isValid = HmacUtil.verifyHmac(hmac, data);
    
    if (isValid) {
        // Process valid callback
    } else {
        // Reject invalid request
    }
}
```


## License
MIT
