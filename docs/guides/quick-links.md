# Quick Links Guide

This guide covers creating shareable payment links and canceling them.

## What Are Quick Links?

Quick Links are hosted payment pages you can share via email, SMS, QR code, or social media — without requiring the customer to go through your checkout flow. Useful for invoices, support-initiated payments, and event tickets.

---

## Create a Payment Link

```java
import com.paymob.sdk.services.quicklink.QuickLinkRequest;
import com.paymob.sdk.services.quicklink.QuickLinkResponse;
import com.paymob.sdk.models.enums.Currency;

QuickLinkResponse link = client.quickLinks().createPaymentLink(
    QuickLinkRequest.builder()
        .amountCents(10000)                         // 100.00 EGP
        .paymentMethods(integrationId)
        .currency(Currency.EGP)
        .isLive(false)                              // true for production
        // --- Optional Fields ---
        .fullName("Jane Smith")
        .customerEmail("jane@example.com")
        .customerPhone("+201234567890")
        .description("Invoice #789")
        .referenceId("INV-789")
        .expiresAt("2024-12-31T23:59:59")           // Auto-expire link
        .notificationUrl("https://yoursite.com/webhook") // Per-link webhook override
        .paymentLinkImage(new java.io.File("logo.png"))  // Custom image
        .build());

String shareableUrl = link.getClientUrl();
// Share via email, SMS, QR code, etc.
```

---

## Handle Payment Result

When the customer pays, Paymob fires a `TRANSACTION_SUCCESSFUL` webhook to your configured notification URL (or the `notificationUrl` set on the link). Handle it the same way as any other transaction webhook.

See the [Webhooks Guide](webhooks.md) for details.

---

## Cancel a Link

```java
// Cancel using the response object
client.quickLinks().cancel(link);

// Or cancel by ID directly
client.quickLinks().cancel(link.getId());
```

Canceled links can no longer be paid. This is useful for expired invoices or when the order is fulfilled by other means.

---

## Optional Fields

| Field | Use Case |
|-------|----------|
| `expiresAt` | Auto-expire links after a deadline |
| `notificationUrl` | Per-link webhook URL (overrides account default) |
| `paymentLinkImage` | Custom image on the payment page |

See the [Quick Links API Reference](../api/quick-links.md) for the full field list.
