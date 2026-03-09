# Paymob Java SDK Documentation

This is the canonical documentation home for the SDK.

The repository root `README.md` is intentionally brief and focused on quick start. Use this section for deeper implementation guidance, field-level reference, and architecture notes.

## Start Here

Choose the path that matches your task:

| I want to... | Read this |
|--------------|-----------|
| Set up credentials and region correctly | [Getting Paymob Credentials](guides/getting-paymob-credentials.md) |
| Build my first checkout flow | [Payment Intention Guide](guides/payment-intention.md) |
| Validate callbacks securely | [Webhooks Guide](guides/webhooks.md) |
| Integrate tokenized card flows | [Saved Cards Guide](guides/saved-cards.md) |
| Build recurring billing flows | [Subscriptions Guide](guides/subscriptions.md) |
| Use quick payment links | [Quick Links Guide](guides/quick-links.md) |

## Documentation Map

### Guides (Task-Oriented)

Use guides when you need implementation flow, best practices, and end-to-end examples.

- [Guides Index](guides/README.md)
- [Getting Paymob Credentials](guides/getting-paymob-credentials.md)
- [Payment Intention](guides/payment-intention.md)
- [Saved Cards](guides/saved-cards.md)
- [Quick Links](guides/quick-links.md)
- [Subscriptions](guides/subscriptions.md)
- [Webhooks](guides/webhooks.md)

### API Reference (Field and Method-Level)

Use API reference for signatures, request/response fields, and operation-specific notes.

- [API Index](api/README.md)
- [Intention](api/intention.md)
- [Transaction](api/transaction.md)
- [Transaction Inquiry](api/transaction-inquiry.md)
- [Saved Cards](api/saved-cards.md)
- [Subscription Plans](api/subscription-plans.md)
- [Subscriptions](api/subscriptions.md)
- [Quick Links](api/quick-links.md)
- [Webhooks](api/webhooks.md)
- [Schemas and Mandatory Fields](api/schemas-and-mandatory-fields.md)

### Architecture and Internals

- [System Design](internals/system-design.md)

### Assets

- [Assets Index](assets/README.md)

## Reading Order (Recommended)

1. [Getting Paymob Credentials](guides/getting-paymob-credentials.md)
2. [Payment Intention](guides/payment-intention.md)
3. [Webhooks](guides/webhooks.md)
4. Service-specific guides based on your use case
5. API reference pages for the exact fields and methods you need

## Documentation Conventions

- Guides explain "how" and "why".
- API pages list exact models, methods, and required fields.
- Amounts are in minor units (for example, cents).
- Webhook POST callbacks are the source of truth for payment state.

## External Resources

- [Paymob Official Docs](https://docs.paymob.com)
- [Paymob API Postman Collections](https://github.com/PaymobAccept/API-Postman-Collections) (official)
- [Changelog](../CHANGELOG.md)
