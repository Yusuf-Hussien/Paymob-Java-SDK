# Payment Intention Guide

This guide walks through integrating Paymob's Unified Checkout into a Java application — from SDK setup to handling the payment result.

## Prerequisites

- A Paymob merchant account
- Your **Secret Key**, **Public Key**, and **HMAC Secret** from the Paymob dashboard
- At least one **Integration ID** (card, wallet, kiosk, etc.) configured in your region

---

## 1. Add the SDK

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

---

## 2. Initialize the Client

Create a single `PaymobClient` instance and reuse it across your application (it is thread-safe).

```java
import com.paymob.sdk.core.PaymobClient;
import com.paymob.sdk.core.PaymobConfig;
import com.paymob.sdk.core.PaymobRegion;
import com.paymob.sdk.models.enums.LogLevel;

PaymobConfig config = PaymobConfig.builder()
    .secretKey("sk_test_...")
    .publicKey("pk_test_...")
    .hmacSecret("your_hmac_secret")
    .region(PaymobRegion.EGYPT)
    .timeout(30)
    .logLevel(LogLevel.NONE)
    .build();

PaymobClient client = new PaymobClient(config);
```

---

## 3. Build the Payment Intention

When a customer checks out, create a payment intention with the order details. The `amount` must equal the sum of all item amounts.

```java
import com.paymob.sdk.models.common.BillingData;
import com.paymob.sdk.models.common.Item;
import com.paymob.sdk.models.enums.Currency;
import com.paymob.sdk.services.intention.IntentionRequest;
import com.paymob.sdk.services.intention.IntentionResponse;
import java.util.List;

Item item = Item.builder()
    .name("Blue T-Shirt (M)")
    .amount(25000)   // 250.00 EGP in cents
    .quantity(1)
    .build();

BillingData billing = BillingData.builder()
    .firstName("John")
    .lastName("Doe")
    .email("john.doe@example.com")
    .phoneNumber("+201234567890")
    .country("EGY")
    .city("Cairo")
    .street("15 Tahrir Square")
    .build();

IntentionRequest request = IntentionRequest.builder()
    .amount(25000)                          // Must equal sum of item amounts
    .currency(Currency.EGP)
    .paymentMethods(List.of(123456))        // Your Integration ID(s)
    .items(List.of(item))
    .billingData(billing)
    .specialReference("ORDER-" + orderId)  // Your internal reference (ID)
    // --- Optional Fields ---
    .notificationUrl("https://yoursite.com/webhook/paymob")   // Optional override
    .redirectionUrl("https://yoursite.com/payment/result")    // Optional
    .acceptOrderId(987654321L)              // Link to an existing order
    .subscriptionPlanId(12345L)             // Enrolling in a subscription plan
    .subscriptionv2Id(67890L)               // Adding a secondary card to a subscription
    .cardTokens(List.of("token"))           // For tokenized CIT/MIT checkouts
    .extras(Map.of("key", "value"))         // Any extra metadata
    .build();

IntentionResponse response = client.intentions().createIntention(request);
```

---

## 4. Redirect the Customer

Generate the checkout URL and redirect your customer to it.

```java
String checkoutUrl = client.intentions().getUnifiedCheckoutUrl(response);

// In a Spring controller:
return "redirect:" + checkoutUrl;

// In a servlet:
response.sendRedirect(checkoutUrl);
```

The customer will land on a Paymob-hosted page where they can choose their payment method and complete the payment.

---

## 5. Handle the Webhook (Server Callback)

Paymob sends a POST request to your notification URL when the payment result is available. Always validate the HMAC before processing.

```java
import com.paymob.sdk.webhook.WebhookValidator;
import com.paymob.sdk.webhook.WebhookEvent;
import com.paymob.sdk.webhook.WebhookEventType;
import com.paymob.sdk.services.transaction.TransactionResponse;

WebhookValidator validator = new WebhookValidator(config.getHmacSecret());

// Spring Boot example:
@PostMapping("/webhook/paymob")
public ResponseEntity<String> handleWebhook(
        @RequestBody String rawBody,
        @RequestParam("hmac") String hmac) {

    // 1. Validate signature
    WebhookEvent event = validator.validateAndParse(rawBody, hmac);
    if (event == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid HMAC");
    }

    // 2. Handle event
    if (event.getType() == WebhookEventType.TRANSACTION_SUCCESSFUL) {
        TransactionResponse tx = event.getData(TransactionResponse.class);
        // Update your order status in the database
        orderService.markAsPaid(tx.getId(), tx.getOrder().getId());
    } else if (event.getType() == WebhookEventType.TRANSACTION_FAILED) {
        TransactionResponse tx = event.getData(TransactionResponse.class);
        orderService.markAsFailed(tx.getOrder().getId());
    }

    // 3. Always respond 200 OK quickly to prevent retries
    return ResponseEntity.ok("ok");
}
```

> ⚠️ **Important:** Paymob passes the HMAC as a **query parameter** (`?hmac=...`), not a header.

---

## 6. Handle the User Redirect (Optional)

After payment, Paymob redirects the customer back to your `redirectionUrl` with the result as query parameters. Validate the HMAC here too before trusting the result.

```java
@GetMapping("/payment/result")
public String handleRedirect(
        @RequestParam Map<String, String> queryParams,
        @RequestParam("hmac") String hmac) {

    if (!validator.validateCallbackSignature(queryParams, hmac)) {
        return "redirect:/error?reason=invalid_signature";
    }

    boolean success = Boolean.parseBoolean(queryParams.getOrDefault("success", "false"));
    String orderId = queryParams.get("merchant_order_id");

    return success
        ? "redirect:/orders/" + orderId + "?status=paid"
        : "redirect:/checkout?error=payment_failed";
}
```

> **Note:** Use the webhook (Step 5) as your source of truth — not this redirect. The redirect can be tampered with or skipped if the customer closes their browser.

---

## 7. Error Handling

Wrap API calls in try-catch blocks to handle failures gracefully:

```java
import com.paymob.sdk.exceptions.*;

try {
    IntentionResponse response = client.intentions().createIntention(request);
} catch (AuthenticationException e) {
    // Wrong secret key or wrong region configured
    log.error("Paymob auth failed: {}", e.getMessage());
} catch (ValidationException e) {
    // Amount mismatch, missing billing fields, invalid currency, etc.
    log.error("Validation error: {}", e.getErrorBody());
} catch (PaymobServerException e) {
    // Paymob server error — safe to retry with backoff
    log.error("Paymob server error [{}]: {}", e.getHttpStatus(), e.getErrorBody());
} catch (PaymobTimeoutException e) {
    // Network timeout
    log.error("Timeout reaching Paymob");
}
```

---

## Complete Flow Summary

```
Customer clicks "Pay"
        │
        ▼
  createIntention(request)
        │
        ▼
  getUnifiedCheckoutUrl(response)
        │
        ▼
  Redirect customer → Paymob hosted page
        │
        ▼
  Customer selects method + completes payment
        │
        ├──→ POST webhook to your server → validate HMAC → update order
        │
        └──→ GET redirect back to your site → validate HMAC → show result page
```
