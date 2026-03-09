# Guides Index

Use guides for end-to-end workflows and implementation decisions.

If you need exact method signatures or request/response model fields, jump to the [API Index](../api/README.md).

## Recommended Reading Order

1. [Getting Paymob Credentials](getting-paymob-credentials.md)
2. [Payment Intention](payment-intention.md)
3. [Webhooks](webhooks.md)
4. Service-specific guides as needed

## Available Guides

| Guide | Use When |
|-------|----------|
| [Getting Paymob Credentials](getting-paymob-credentials.md) | You are setting up keys, integration IDs, and region mapping |
| [Payment Intention](payment-intention.md) | You want the standard hosted checkout flow |
| [Saved Cards](saved-cards.md) | You need CIT/MIT flows with tokenized cards |
| [Quick Links](quick-links.md) | You want to generate and share payment links |
| [Subscriptions](subscriptions.md) | You are implementing recurring billing |
| [Webhooks](webhooks.md) | You need secure callback validation and event handling |

## Conventions

- Treat webhook POST callbacks as the source of truth.
- Keep amounts in minor units (for example, cents).
- Keep test and live credentials fully separated.
