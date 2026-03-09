# Quick Links API Reference

**Class:** `com.paymob.sdk.services.quicklink.QuickLinkService`
**Accessed via:** `client.quickLinks()`
**Auth:** Bearer Token (API Key)

Create shareable payment links for invoices, social media, QR codes, and customer service.

---

## Methods

### `createPaymentLink(QuickLinkRequest request) → QuickLinkResponse`

```java
QuickLinkResponse link = client.quickLinks().createPaymentLink(
    QuickLinkRequest.builder()
        .amountCents(10000)
        .paymentMethods(integrationId)
        .currency(Currency.EGP)
        .isLive(false)
        .fullName("John Doe")
        .customerEmail("john@example.com")
        .customerPhone("+201234567890")
        .description("Invoice #456")
        .build());

String shareableUrl = link.getClientUrl();
```

### `cancel(QuickLinkResponse response) → QuickLinkResponse`
### `cancel(int paymentLinkId) → QuickLinkResponse`

```java
client.quickLinks().cancel(link);
// or
client.quickLinks().cancel(link.getId());
```

---

## QuickLinkRequest Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `amountCents` | `int` | ✅ | Payment amount in cents |
| `paymentMethods` | `int` | ✅ | Integration ID |
| `currency` | `Currency` | No | Payment currency |
| `isLive` | `boolean` | ✅ | `true` for live, `false` for sandbox |
| `fullName` | `String` | No | Customer full name |
| `customerEmail` | `String` | No | Customer email |
| `customerPhone` | `String` | No | Customer phone number |
| `description` | `String` | No | Payment description shown on the link page |
| `referenceId` | `String` | No | Your internal reference |
| `notificationUrl` | `String` | No | Webhook URL for this link |
| `expiresAt` | `String` | No | Link expiry datetime (ISO 8601) |
| `paymentLinkImage` | `File` | No | Custom image displayed on the link page |

---

## QuickLinkResponse Fields

| Field | Type | Description |
|-------|------|-------------|
| `id` | `long` | Quick link ID (use with `cancel(int)`) |
| `clientUrl` | `String` | The shareable payment URL to send to customers |
| `shortenUrl` | `String` | Shortened version of `clientUrl` (if available) |
| `state` | `String` | Link state: `"active"`, `"canceled"`, `"paid"` |
| `amountCents` | `int` | Payment amount in cents |
| `currency` | `String` | 3-letter currency code |
| `description` | `String` | Payment description (if set) |
| `referenceId` | `String` | Your internal reference (if set) |
| `clientInfo` | `ClientInfo` | Nested customer details (fullName, email, phoneNumber) |
| `success` | `boolean` | Whether the link was created successfully |
| `message` | `String` | Additional status message |
| `createdAt` | `String` | ISO 8601 creation timestamp |
| `expiresAt` | `String` | ISO 8601 expiry timestamp (if set) |
| `paidAt` | `String` | ISO 8601 timestamp when the link was paid (if paid) |
| `notificationUrl` | `String` | Webhook URL for this link (if set) |
| `redirectionUrl` | `String` | Post-payment redirect URL (if set) |
| `order` | `Long` | Paymob order ID linked to the payment (after payment) |
| `paymentLinkImage` | `String` | URL of the custom image (if set) |
