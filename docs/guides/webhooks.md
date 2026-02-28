# Webhooks Guide

This guide covers everything you need to handle Paymob webhooks securely using the SDK.

## Overview

Paymob sends two kinds of callbacks:

| Type | Method | When | Use for |
|------|--------|------|---------|
| **Server Callback** | `POST` | When any payment event occurs | Updating your database — this is your source of truth |
| **User Callback** | `GET` | When the customer finishes (or abandons) the payment page | Showing a result page to the customer |

Always use the server callback to update your order status. The user redirect can be bypassed if the customer closes their browser.

---

## HMAC Signature Verification

Every webhook Paymob sends includes an HMAC-SHA512 signature. The SDK verifies it automatically.

```java
WebhookValidator validator = new WebhookValidator("your_hmac_secret");
```

The validator auto-detects the webhook type (Transaction, Subscription, or Card Token) and applies the correct field concatenation logic for each.

---

## Server Callback (POST)

Paymob sends a POST request with a JSON body to your configured notification URL. The HMAC arrives as a **query parameter** named `hmac`:

```
POST https://yoursite.com/webhook/paymob?hmac=abc123...
Content-Type: application/json

{ "type": "TRANSACTION", "obj": { ... } }
```

### Handling with Spring Boot

```java
import com.paymob.sdk.webhook.WebhookValidator;
import com.paymob.sdk.webhook.WebhookEvent;
import com.paymob.sdk.webhook.WebhookEventType;

@RestController
public class WebhookController {

    private final WebhookValidator validator = new WebhookValidator("your_hmac_secret");

    @PostMapping("/webhook/paymob")
    public ResponseEntity<String> handle(
            @RequestBody String rawBody,
            @RequestParam("hmac") String hmac) {

        WebhookEvent event = validator.validateAndParse(rawBody, hmac);

        if (event == null) {
            // Signature mismatch — reject the request
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid HMAC");
        }

        switch (event.getType()) {
            case TRANSACTION_SUCCESSFUL -> handleSuccess(event);
            case TRANSACTION_FAILED     -> handleFailure(event);
            case TRANSACTION_REFUNDED   -> handleRefund(event);
            case TRANSACTION_VOIDED     -> handleVoid(event);
            case CARD_TOKEN             -> handleCardToken(event);

            // Subscription events
            case SUBSCRIPTION_CREATED              -> handleSubscriptionCreated(event);
            case SUBSCRIPTION_SUSPENDED            -> handleSubscriptionSuspended(event);
            case SUBSCRIPTION_RESUMED              -> handleSubscriptionResumed(event);
            case SUBSCRIPTION_CANCELED             -> handleSubscriptionCanceled(event);
            case SUBSCRIPTION_SUCCESSFUL_TRANSACTION -> handleRecurringSuccess(event);
            case SUBSCRIPTION_FAILED_TRANSACTION   -> handleRecurringFailure(event);
        }

        // Always return 200 OK quickly to prevent Paymob from retrying
        return ResponseEntity.ok("ok");
    }
}
```

### Handling with Plain Servlet / Jakarta EE

```java
@WebServlet("/webhook/paymob")
public class PaymobWebhookServlet extends HttpServlet {

    private final WebhookValidator validator = new WebhookValidator("your_hmac_secret");

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String rawBody = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String hmac = req.getParameter("hmac");

        WebhookEvent event = validator.validateAndParse(rawBody, hmac);

        if (event == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Process event...

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("ok");
    }
}
```

---

## User Callback (GET)

After the customer completes or abandons the payment, Paymob redirects them to your `redirectionUrl` with the result in query parameters. The HMAC is also a query parameter here.

```
GET https://yoursite.com/payment/result
    ?success=true
    &id=192036465
    &order=217503754
    &merchant_order_id=ORDER-123
    &amount_cents=10000
    &currency=EGP
    &hmac=abc123...
```

```java
@GetMapping("/payment/result")
public String handleRedirect(
        @RequestParam Map<String, String> queryParams,
        @RequestParam("hmac") String hmac) {

    if (!validator.validateCallbackSignature(queryParams, hmac)) {
        return "redirect:/error?reason=invalid_signature";
    }

    boolean success = Boolean.parseBoolean(queryParams.getOrDefault("success", "false"));
    return success ? "redirect:/success" : "redirect:/failure";
}
```

> ⚠️ Do **not** use this redirect to update your database. Use it only to show the customer a result page. The server POST webhook is your reliable source of truth.

---

## Extracting Event Data

Use `event.getData(Class<T>)` to deserialize the payload into a typed object.

### Transaction Event

```java
import com.paymob.sdk.services.transaction.TransactionResponse;

case TRANSACTION_SUCCESSFUL -> {
    TransactionResponse tx = event.getData(TransactionResponse.class);
    long transactionId = tx.getId();
    long orderId = tx.getOrder().getId();
    int amount = tx.getAmountCents();
    boolean success = tx.isSuccess();
    // Update your order...
}
```

### Card Token Event

When a customer saves their card, you receive a `CARD_TOKEN` event containing the reusable token.

```java
case CARD_TOKEN -> {
    // getData() returns the token string directly for card token events
    String cardToken = (String) event.getData();
    // Save cardToken to the customer's profile for future CIT/MIT payments
}
```

### Subscription Event

```java
import com.paymob.sdk.services.subscription.SubscriptionResponse;

case SUBSCRIPTION_CREATED -> {
    SubscriptionResponse sub = event.getData(SubscriptionResponse.class);
    long subscriptionId = sub.getId();
    String state = sub.getState();
    // Activate the subscription in your system...
}
```

---

## All Event Types

### Transaction Events

| `WebhookEventType` | When it fires |
|--------------------|---------------|
| `TRANSACTION_SUCCESSFUL` | Payment completed successfully |
| `TRANSACTION_FAILED` | Payment attempt failed |
| `TRANSACTION_VOIDED` | Transaction voided (same-day cancellation) |
| `TRANSACTION_REFUNDED` | Refund processed |

### Subscription Events

| `WebhookEventType` | When it fires |
|--------------------|---------------|
| `SUBSCRIPTION_CREATED` | Customer enrolled and first payment made |
| `SUBSCRIPTION_SUSPENDED` | Subscription suspended (manually or due to failed retries) |
| `SUBSCRIPTION_RESUMED` | Suspended subscription reactivated |
| `SUBSCRIPTION_CANCELED` | Subscription permanently canceled |
| `SUBSCRIPTION_UPDATED` | Subscription details updated |
| `SUBSCRIPTION_SUCCESSFUL_TRANSACTION` | Recurring billing payment succeeded |
| `SUBSCRIPTION_FAILED_TRANSACTION` | Recurring billing payment failed |
| `SUBSCRIPTION_FAILED_OVERDUE_TRANSACTION` | Overdue payment retry failed |
| `SUBSCRIPTION_ADD_SECONDARY_CARD` | Customer added a secondary card |
| `SUBSCRIPTION_CHANGE_PRIMARY_CARD` | Customer changed the primary billing card |
| `SUBSCRIPTION_DELETE_CARD` | A card was removed from the subscription |

### Card Token Events

| `WebhookEventType` | When it fires |
|--------------------|---------------|
| `CARD_TOKEN` | Customer saved their card during checkout |

---

## Security Best Practices

**Always validate before processing.** Never trust the webhook payload without verifying the HMAC first.

**Use constant-time comparison.** The SDK uses `MessageDigest.isEqual()` internally — this prevents timing attacks where an attacker could guess the correct HMAC byte by byte.

**Respond quickly.** Return `200 OK` immediately after validation and process the event asynchronously if needed. Slow responses will cause Paymob to retry the webhook.

**Handle duplicates.** Paymob may deliver the same webhook more than once. Make your handlers idempotent — check if the order is already in the target state before updating it.

**Log validation failures.** Rejected webhooks (invalid HMAC) should be logged for security monitoring.
