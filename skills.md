# Skills for Agent-Driven Integration

This file helps developers and coding agents integrate `Paymob-Java-SDK` quickly and consistently.

## Scope

Use these skills when an agent is asked to:

- add or update Paymob payment flows
- implement webhook validation
- add subscription or saved-card features
- debug API/auth/configuration issues
- add integration tests and examples

Use repository docs as the source of truth:

- `README.md`
- `docs/README.md`
- `docs/api/`
- `docs/guides/`
- `example/`

## Skill 1: Bootstrap SDK Client

Goal: create a correct `PaymobConfig` and `PaymobClient` for the requested region and services.

Checklist:

- confirm which flow is required (intention, subscription, inquiry, quick link, webhook)
- ensure required keys are present (`secretKey`, `apiKey`, `publicKey`, `hmacSecret` as needed)
- set region explicitly (`EGYPT`, `KSA`, `UAE`, `OMAN`)
- avoid hardcoding secrets in source files
- keep timeout/log level explicit when debugging

Agent prompt template:

```text
Set up Paymob-Java-SDK client config for [REGION] in a Java [FRAMEWORK] project.
Implement a production-safe configuration class that reads credentials from env vars.
Include validation for missing keys and show a minimal usage example.
```

## Skill 2: Payment Intention Flow

Goal: create a valid intention request and return checkout URL safely.

Checklist:

- ensure `amount` equals sum of all item amounts
- use correct currency enum
- provide `paymentMethods` integration IDs
- include required billing data
- return or redirect using unified checkout URL
- handle `ValidationException`, `AuthenticationException`, and fallback `PaymobException`

Agent prompt template:

```text
Implement a payment intention endpoint using Paymob-Java-SDK.
Input: cart items, customer billing data, and payment method integration IDs.
Output: checkout URL from unified checkout.
Add robust exception mapping to HTTP responses.
```

## Skill 3: Webhook Verification and Parsing

Goal: accept Paymob callbacks securely and map event payloads.

Checklist:

- validate HMAC signature before processing
- reject invalid signatures with non-2xx response
- parse webhook event type and payload defensively
- make handlers idempotent (avoid double-processing)
- log correlation IDs and transaction references

Agent prompt template:

```text
Create a webhook controller for Paymob using WebhookValidator.
Validate HMAC first, parse event payload, and route to handlers by event type.
Implement idempotency guard and return safe responses.
```

## Skill 4: Subscription and Saved Card Features

Goal: implement recurring billing and tokenized card operations with clear boundaries.

Checklist:

- separate plan management from subscription lifecycle logic
- store only token references, never raw card details
- support CIT/MIT paths as needed
- handle retries and failed charge events
- document required credentials and integration IDs per environment

Agent prompt template:

```text
Add subscription support with Paymob-Java-SDK:
1) create/manage plans
2) create subscriptions
3) handle pause/cancel/update actions
4) add webhook-driven status synchronization
Include service and controller layers with tests.
```

## Skill 5: Integration Test Workflow

Goal: validate real API behavior and keep reproducible test setup.

Checklist:

- run unit tests first, then integration tests
- configure test env via `setup_test_env.ps1` pattern
- isolate test data by reference IDs
- assert both success and expected failure cases
- save reports for CI review

Agent prompt template:

```text
Add integration tests for [FEATURE] in Paymob-Java-SDK.
Use environment-based credentials and unique references per test.
Cover success path plus at least 2 failure scenarios.
Ensure tests are CI-friendly and deterministic.
```

## Skill 6: Troubleshooting and Recovery

Goal: speed up diagnosis for common integration failures.

Checklist:

- verify region and credential pairing first
- inspect SDK exception class and raw error body
- check integration ID and payment method compatibility
- retry only transient server/timeouts with backoff
- avoid retrying validation/authentication failures blindly

Agent prompt template:

```text
Diagnose this Paymob SDK failure from stack trace and error body.
Classify as auth, validation, not-found, timeout, or server error.
Provide root cause, fix steps, and code patch.
```

## Definition of Done for Agent Changes

Every agent-generated integration change should include:

- compile-ready Java code
- error handling aligned with `PaymobException` hierarchy
- minimal tests (or clear test additions when requested)
- updates to docs/examples for public behavior changes
- no committed secrets or merchant credentials

## Recommended Developer Handoff Notes

When an agent finishes, it should report:

- files changed
- assumptions made (region, currency, integrations)
- required environment variables
- how to run or test the feature
- known risks or follow-up improvements
