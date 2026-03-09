# Paymob Java SDK

**Community-maintained Java SDK for the [Paymob](https://paymob.com) Payment Gateway.**
**This is NOT an official Paymob product.**

Supports **Egypt**, **KSA**, **UAE**, and **Oman** regions.

[![Maven Central](https://img.shields.io/maven-central/v/io.github.yusuf-hussien/paymob-java-sdk)](https://central.sonatype.com/artifact/io.github.yusuf-hussien/paymob-java-sdk)
[![CI](https://github.com/Yusuf-Hussien/Paymob-Java-SDK/actions/workflows/ci.yml/badge.svg)](https://github.com/Yusuf-Hussien/Paymob-Java-SDK/actions/workflows/ci.yml)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17%2B-orange)](https://openjdk.org/)

> **Disclaimer:** This is a community-maintained SDK and is **NOT** affiliated with or endorsed by Paymob.
> For official Paymob documentation and support, visit [docs.paymob.com](https://docs.paymob.com).

---

## 📋 Table of Contents

- [Quick Start](#-quick-start)
- [Services](#-services)
- [Error Handling](#-error-handling)
- [Configuration](#%EF%B8%8F-configuration)
- [Requirements](#-requirements)
- [Documentation](#-documentation)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🚀 Quick Start

### 1. Add the Dependency

**Maven:**

```xml
<dependency>
    <groupId>io.github.yusuf-hussien</groupId>
    <artifactId>paymob-java-sdk</artifactId>
    <version>1.1.0</version><!-- verify the latest version -->
</dependency>
```

**Gradle:**

```gradle
implementation 'io.github.yusuf-hussien:paymob-java-sdk:1.1.0'
```

### 2. Initialize the Client

```java
import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;

PaymobConfig config = PaymobConfig.builder()
    .secretKey("your_secret_key")
    .publicKey("your_public_key")
    .hmacSecret("your_hmac_secret")
    .apiKey("your_api_key")         
    .region(PaymobRegion.EGYPT)
    .build();

PaymobClient client = new PaymobClient(config);
```

See [Configuration](#%EF%B8%8F-configuration) for all available options.

### 3. Create a Payment

```java
import com.paymob.sdk.models.common.BillingData;
import com.paymob.sdk.models.common.Item;
import com.paymob.sdk.models.enums.Currency;
import com.paymob.sdk.services.intention.IntentionRequest;
import com.paymob.sdk.services.intention.IntentionResponse;
import java.util.List;

Item item = Item.builder()
    .name("Order #123")
    .amount(1000)   // 10.00 EGP in cents
    .quantity(1)
    .build();

BillingData billing = BillingData.builder()
    .firstName("Yusuf")
    .lastName("Hussien")
    .email("yusuf@example.com")
    .phoneNumber("+201000000000")
    .country("EGY")
    .city("Cairo")
    .street("123 Main St")
    .build();

IntentionRequest request = IntentionRequest.builder()
    .amount(1000)                         // Must equal sum of item amounts
    .currency(Currency.EGP)
    .paymentMethods(List.of(123456))      // Your Integration ID(s)
    .items(List.of(item))
    .billingData(billing)
    .specialReference("ORDER-12345")
    .build();

IntentionResponse response = client.intentions().createIntention(request);

// Redirect the customer to this URL
String checkoutUrl = client.intentions().getUnifiedCheckoutUrl(response);
```

---

## 📦 Services

| Service | Accessed via | Description |
|---------|-------------|-------------|
| Payment Intention | `client.intentions()` | Create checkout sessions for cards, wallets, kiosk, and more |
| Saved Cards | `client.savedCards()` | CIT and MIT intention flows, plus MOTO charge execution with saved tokens |
| Subscription Plans | `client.subscriptionPlans()` | Create and manage recurring billing plans |
| Subscriptions | `client.subscriptions()` | Enroll customers, manage lifecycle, handle card changes |
| Transaction Management | `client.transactions()` | Refund, void, and capture transactions |
| Transaction Inquiry | `client.inquiry()` | Look up transactions by order reference, order ID, or transaction ID |
| Quick Links | `client.quickLinks()` | Generate shareable payment links |
| Webhooks | `new WebhookValidator(hmacSecret)` | Validate HMAC signatures and parse all event types |

For method signatures, request/response fields, and code examples, see the [API Reference](docs/api/) or the [Guides](docs/guides/).

---

## ⚠️ Error Handling

All exceptions are unchecked and extend `PaymobException`, which carries `httpStatus` and `errorBody` for inspecting the raw Paymob error response.

| Exception | HTTP Status | Common Cause |
|-----------|-------------|--------------|
| `AuthenticationException` | 401 | Invalid credentials or region mismatch |
| `ValidationException` | 406 | Missing or invalid request fields |
| `ResourceNotFoundException` | 404 | Integration ID, transaction, or plan not found |
| `PaymobServerException` | 5xx | Paymob server-side error — safe to retry |
| `PaymobTimeoutException` | — | Network timeout |

```java
try {
    IntentionResponse response = client.intentions().createIntention(request);
} catch (AuthenticationException e) {
    // Wrong secret key or wrong region configured
} catch (ValidationException e) {
    log.error("Validation error: {}", e.getErrorBody());
} catch (PaymobServerException e) {
    // Retry with backoff
} catch (PaymobException e) {
    // Catch-all
}
```

---

## ⚙️ Configuration

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| `secretKey` | ✅ Always | — | Secret key for API authentication |
| `apiKey` | ✅ For Bearer-token services | — | API key used to generate Bearer tokens (subscriptions, inquiry, quick links) |
| `publicKey` | ✅ For checkout URL & webhooks | — | Public key |
| `hmacSecret` | ✅ For webhook validation | — | HMAC secret for signature verification |
| `region` | No | `EGYPT` | API region — `EGYPT`, `KSA`, `UAE`, `OMAN` |
| `timeout` | No | `60s` | HTTP timeout in seconds |
| `logLevel` | No | `NONE` | HTTP logging verbosity — `NONE`, `BASIC`, `HEADERS`, `BODY` |

**Regions:**

| Region | Base URL |
|--------|----------|
| `EGYPT` | `https://accept.paymob.com` |
| `KSA` | `https://ksa.paymob.com` |
| `UAE` | `https://uae.paymob.com` |
| `OMAN` | `https://oman.paymob.com` |

**Log levels** use SLF4J — bring your own backend (Logback, Log4j2, etc.). `BODY` logs full request and response bodies.

---

## 📋 Requirements

- Java 17+
- Maven 3.6+ (for building from source)
- A [Paymob merchant account](https://paymob.com)
- No Spring dependency — works with any Java application

---

## 📚 Documentation

Full documentation is organized under `docs/`.

- Start here: [Documentation Home](docs/README.md)
- Quick start flow: [Getting Paymob Credentials](docs/guides/getting-paymob-credentials.md) -> [Payment Intention](docs/guides/payment-intention.md) -> [Webhooks](docs/guides/webhooks.md)
- API details: [API Index](docs/api/README.md)
- Architecture notes: [System Design](docs/internals/system-design.md)

---

## 🤝 Contributing

Contributions are welcome! Please read the [Contributing Guide](CONTRIBUTING.md) before submitting a pull request.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
