# Documentation

**Community-maintained Java SDK for the [Paymob](https://paymob.com) Payment Gateway.**

---

## Guides

Step-by-step integration walkthroughs.

| Guide | Description |
|-------|-------------|
| [Payment Intention](guides/payment-intention.md) | Core checkout flow — create an intention, redirect, handle webhook |
| [Saved Cards](guides/saved-cards.md) | CIT and MIT payments using tokenized cards |
| [Quick Links](guides/quick-links.md) | Create shareable payment links |
| [Subscriptions](guides/subscriptions.md) | Plan management and recurring billing lifecycle |
| [Webhooks](guides/webhooks.md) | HMAC verification, all event types, code examples |

---

## API Reference

Per-service method and field reference.

| Reference | Description |
|-----------|-------------|
| [Intention](api/intention.md) | Create, update, retrieve intentions; generate checkout URLs |
| [Transaction](api/transaction.md) | Refund, void, capture |
| [Transaction Inquiry](api/transaction-inquiry.md) | Look up transactions by merchant order ID, order ID, or transaction ID |
| [Saved Cards](api/saved-cards.md) | CIT and MIT payment requests |
| [Subscription Plans](api/subscription-plans.md) | Plan CRUD, suspend, resume |
| [Subscriptions](api/subscriptions.md) | Enrollment, lifecycle management, card management |
| [Quick Links](api/quick-links.md) | Create and cancel payment links |
| [Webhooks](api/webhooks.md) | WebhookValidator, WebhookEvent, all event types |

---

## Internals

| Document | Description |
|----------|-------------|
| [Architecture](internals/architecture.md) | Layers, components, auth system, HTTP pipeline, design decisions |

---

## External Resources

- [Paymob Official Docs](https://docs.paymob.com)
- [Paymob API Postman Collections](https://github.com/PaymobAccept/API-Postman-Collections) _(official)_
- [Changelog](../CHANGELOG.md)
