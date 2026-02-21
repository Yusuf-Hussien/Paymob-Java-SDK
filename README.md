# Paymob Java SDK

**Community-maintained Java SDK for Paymob Payment Gateway.**
**This is NOT the official SDK.**

Supports Egypt, KSA, UAE, and Oman.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.yusuf-hussien/paymob-java-sdk)](https://search.maven.org/artifact/io.github.yusuf-hussien/paymob-java-sdk)
[![CI](https://github.com/Yusuf-Hussien/Paymob-Java-SDK/actions/workflows/maven-publish.yml/badge.svg)](https://github.com/Yusuf-Hussien/Paymob-Java-SDK/actions)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17%2B-orange)](https://openjdk.org/)

## 📋 Table of Contents

- [Quick Start (5 minutes)](#quick-start-5-minutes)
- [Services](#services)
- [Error Handling](#error-handling)
- [Configuration](#configuration)
- [Multi-Region](#multi-region)
- [Logging](#logging)
- [Metrics](#metrics)
- [Requirements](#requirements)
- [Contributing](#contributing)
- [License](#license)

## 📚 Documentation

- **[API Reference](#services)** - Complete service documentation
- **[Integration Flows](docs/flow_diagrams.md)** - Visual diagrams and step-by-step guides
- **[Examples](examples/)** - Ready-to-use code examples
- **[Production Guide](Paymob-Java-SDK-Production-Guide.md)** - Deployment and best practices
- **[Official Paymob API Docs](https://docs.paymob.com)** - Official Paymob documentation

## ⚠️ Important Notice

This is a **community-maintained SDK** and is **NOT the official Paymob SDK**. 

For the official SDK and support:
- **Official Paymob Documentation**: https://docs.paymob.com
- **Paymob Developer Support**: Contact through your Paymob Dashboard
- **Official SDKs**: Check Paymob's official repositories for supported languages

This community SDK provides:
- ✅ Full API coverage for all Paymob services
- ✅ Active maintenance and community support
- ✅ Comprehensive examples and documentation
- ✅ Production-ready with proper error handling
- ✅ Open source with MIT license

## Quick Start (5 minutes)

### 1. Add Dependency

**Maven:**
```xml
<dependency>
    <groupId>io.github.yusuf-hussien</groupId>
    <artifactId>paymob-java-sdk</artifactId>
    <version>0.1.1</version>
</dependency>
```

**Gradle:**
```gradle
implementation 'io.github.yusuf-hussien:paymob-java-sdk:0.1.1'
```

### 2. Create Your First Payment

```java
import com.paymob.sdk.PaymobClient;
import com.paymob.sdk.PaymobConfig;
import com.paymob.sdk.models.Region;
import com.paymob.sdk.models.intention.*;

// Initialize SDK
PaymobConfig config = PaymobConfig.builder()
    .apiKey("your_api_key")
    .secretKey("your_secret_key")
    .publicKey("your_public_key")
    .region(Region.UAE)
    .build();

PaymobClient client = new PaymobClient(config);

// Create payment intention
IntentionRequest request = IntentionRequest.builder()
    .amount(1000) // 10.00 in cents
    .currency("AED")
    .integrationIds(List.of(456789))
    .billingData(BillingData.builder()
        .firstName("John")
        .lastName("Doe")
        .email("john@example.com")
        .phoneNumber("+971500000000")
        .build())
    .build();

IntentionResponse response = client.intentions().create(request);
String paymentUrl = response.getPaymentUrl();
```

### 3. Handle Webhook

Paymob supports two types of webhooks:

#### 🔄 Server Callback (POST) - Recommended
Server-to-server notifications for transaction updates:

```java
import com.paymob.sdk.webhook.WebhookValidator;
import com.paymob.sdk.webhook.WebhookEvent;

// Initialize webhook validator with your HMAC secret
WebhookValidator validator = new WebhookValidator("your_hmac_secret");

@PostMapping("/webhook/paymob")
public ResponseEntity<String> handleWebhook(@RequestBody String rawBody, 
                                          @RequestParam("hmac") String hmac) {
    try {
        WebhookEvent event = validator.validateAndParse(rawBody, hmac);
        
        if (event == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid HMAC");
        }
        
        // Process event based on type
        switch (event.getEventType()) {
            case TRANSACTION_PROCESSED:
                // Handle successful payment
                break;
            case TRANSACTION_FAILED:
                // Handle failed payment
                break;
            // ... handle other event types
        }
        
        return ResponseEntity.ok("success");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
    }
}
```

#### 👤 User Callback (GET) - Optional
User redirect after payment completion:

```java
@GetMapping("/callback")
public String handleCallback(@RequestParam Map<String, String> queryParams,
                           @RequestParam("hmac") String hmac) {
    try {
        if (!validator.validateCallbackSignature(queryParams, hmac)) {
            return "Security validation failed";
        }
        
        boolean isSuccess = Boolean.parseBoolean(queryParams.getOrDefault("success", "false"));
        String merchantOrderId = queryParams.get("merchant_order_id");
        
        if (isSuccess) {
            // Show success page
            return generateSuccessHtml(merchantOrderId);
        } else {
            // Show failure page
            return generateFailureHtml(merchantOrderId);
        }
    } catch (Exception e) {
        return "Error processing callback";
    }
}
```

**Important Notes:**
- Paymob sends HMAC as **query parameter** named `hmac` (not header)
- HMAC is **hex-encoded** (not Base64)
- Fields are concatenated in **specific order** for validation
- Use `MessageDigest.isEqual()` for secure comparison
- **Server callbacks** are recommended for reliable updates
- **User callbacks** are for user-facing pages

## Services

### 💳 Payment Intention

Create payment links for cards, wallets, and more:

```java
IntentionResponse response = client.intentions().create(request);
String paymentUrl = response.getPaymentUrl();
```

### 💳 Saved Cards

Charge customers using saved tokens:

```java
// Customer Initiated (with CVV)
TokenizedPaymentResponse cit = client.savedCards().payWithCit(citRequest);

// Merchant Initiated (no CVV)
TokenizedPaymentResponse mit = client.savedCards().payWithMit(mitRequest);
```

### 🔄 Subscriptions

Manage recurring billing:

```java
// Create subscription plan
SubscriptionResponse plan = client.subscriptions().createPlan(planRequest);

// Subscribe customer
SubscriptionResponse subscription = client.subscriptions().subscribe(subRequest);

// Manage lifecycle
client.subscriptions().suspend(subscriptionId);
client.subscriptions().resume(subscriptionId);
client.subscriptions().cancel(subscriptionId);
```

### 🔀 Transaction Management

Control payments after they're made:

```java
// Refund (full or partial)
TransactionResponse refund = client.transactions().refund(
    RefundRequest.builder().transactionId(txId).amountCents(1000).build());

// Void same-day transaction
TransactionResponse void = client.transactions().void_(
    VoidRequest.builder().transactionId(txId).build());

// Capture authorized funds
TransactionResponse capture = client.transactions().capture(
    CaptureRequest.builder().transactionId(txId).amountCents(1000).build());
```

### 🔍 Transaction Inquiry

Look up transactions by different criteria:

```java
// By your order ID
InquiryResponse byMerchantOrderId = client.inquiry().byMerchantOrderId("ORDER-123");

// By Paymob order ID
InquiryResponse byOrderId = client.inquiry().byOrderId(456789);

// By transaction ID
InquiryResponse byTransactionId = client.inquiry().byTransactionId(987654);
```

### 🔗 Quick Links

Create shareable payment links:

```java
QuickLinkResponse link = client.quickLinks().create(
    QuickLinkRequest.builder()
        .amount(1000)
        .currency("AED")
        .title("Invoice #123")
        .integrationId(456789)
        .build());

String shareableUrl = link.getUrl();
```

## Error Handling

The SDK provides comprehensive error handling with specific exception types:

```java
try {
    IntentionResponse response = client.intentions().create(request);
} catch (AuthenticationException e) {
    // Invalid credentials - check API keys and region
    System.err.println("Authentication failed: " + e.getMessage());
} catch (ValidationException e) {
    // Invalid request data - check amount, currency, billing data
    System.err.println("Validation failed: " + e.getMessage());
} catch (PaymobServerException e) {
    // Paymob server error - implement retry logic
    System.err.println("Server error: " + e.getMessage());
} catch (PaymobTimeoutException e) {
    // Network timeout - check connection and retry
    System.err.println("Timeout: " + e.getMessage());
} catch (PaymobException e) {
    // Generic Paymob error
    System.err.println("Paymob error: " + e.getMessage());
}
```

## Configuration

All builder options with types and defaults:

```java
PaymobClient client = PaymobClient.builder()
    .apiKey("your_api_key")                    // Required for subscriptions
    .secretKey("your_secret_key")              // Required for most operations
    .publicKey("your_public_key")              // Required for webhooks
    .region(Region.UAE)                        // Required: EGYPT, KSA, UAE, OMAN
    .connectTimeoutSeconds(10)                 // Optional: connection timeout (default: 10)
    .readTimeoutSeconds(30)                     // Optional: read timeout (default: 30)
    .hmacSecret("your_hmac_secret")            // Optional: webhook verification
    .metricsRecorder(customMetricsRecorder)     // Optional: metrics collection
    .build();
```

## Multi-Region

Switch between Paymob regions:

```java
// Egypt
PaymobClient client = PaymobClient.builder()
    .region(Region.EGYPT)
    .build();

// Saudi Arabia
PaymobClient client = PaymobClient.builder()
    .region(Region.KSA)
    .build();

// UAE
PaymobClient client = PaymobClient.builder()
    .region(Region.UAE)
    .build();

// Oman
PaymobClient client = PaymobClient.builder()
    .region(Region.OMAN)
    .build();
```

## Logging

Enable/disable logging and control log levels:

```java
// Enable debug logging
PaymobConfig config = PaymobConfig.builder()
    .secretKey("sk_...")
    .region(Region.UAE)
    .debug(true)  // Enable debug logging
    .build();
```

Or via properties:
```properties
paymob.debug=true
logging.level.com.paymob=DEBUG
```

## Metrics

Plug in Micrometer with example recorder:

```java
public class MicrometerPaymobRecorder implements PaymobMetricsRecorder {
    private final MeterRegistry registry;

    public MicrometerPaymobRecorder(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void recordApiCall(String service, String operation, String region,
                              int statusCode, long durationMs, boolean success) {
        Timer.builder("paymob.api.call")
            .tag("service", service)
            .tag("operation", operation)
            .tag("region", region)
            .tag("status", String.valueOf(statusCode))
            .tag("success", String.valueOf(success))
            .register(registry)
            .record(durationMs, TimeUnit.MILLISECONDS);
    }

    // Implement other methods...
}

// Configure with custom metrics
PaymobClient client = PaymobClient.builder()
    .metricsRecorder(new MicrometerPaymobRecorder(meterRegistry))
    .build();
```

## Requirements

- Java 17+
- No Spring required — plain Java
- Maven 3.6+
- A Paymob Merchant Account

## Contributing

See CONTRIBUTING.md

## License

MIT
