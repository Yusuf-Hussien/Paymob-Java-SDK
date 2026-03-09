# API Reference Index

Use this section for exact SDK service methods, request/response models, and field requirements.

If you need walkthroughs and architecture decisions, start at the [Guides Index](../guides/README.md).

## Services

| Reference | Covers |
|-----------|--------|
| [Intention](intention.md) | Create, update, retrieve intentions; build checkout URL |
| [Transaction](transaction.md) | Refund, void, capture |
| [Transaction Inquiry](transaction-inquiry.md) | Query transactions by IDs/references |
| [Saved Cards](saved-cards.md) | CIT, MIT, and tokenized card flows |
| [Subscription Plans](subscription-plans.md) | Plan create/update/suspend/resume |
| [Subscriptions](subscriptions.md) | Customer enrollment and lifecycle actions |
| [Quick Links](quick-links.md) | Create and cancel payment links |
| [Webhooks](webhooks.md) | Validator APIs and event model |

## Shared Models

- [Schemas and Mandatory Fields](schemas-and-mandatory-fields.md)

## Practical Usage Pattern

1. Read the relevant guide for flow and context.
2. Use this API page for exact field-level implementation.
3. Validate with webhook events and idempotent handling.
