# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2025-02-28

### Added
- Integration test workflow (`integration-test.yml`) against live Paymob sandbox API.
- Dedicated `integration-test` Maven profile to isolate Failsafe execution from the release build.
- Reorganized CI/CD into three focused workflows: `ci.yml`, `release.yml`, `pages.yml`.
- Full documentation overhaul: corrected method names, added webhooks guide, subscriptions guide, and complete API reference.

### Fixed
- CI badge in README now points to `ci.yml` instead of the removed `maven-publish.yml`.
- Version number inconsistency between Maven and Gradle snippets in README.

## [0.1.0] - 2025-01-15

### Added
- Initial release of the community-maintained Paymob Java SDK.
- Core services: Payment Intention, Saved Cards, Subscriptions, Subscription Plans, Transactions, Transaction Inquiry, Quick Links.
- Webhook HMAC-SHA512 verification with auto-detection of event type (Transaction, Subscription, Card Token).
- Multi-region support: Egypt, KSA, UAE, Oman.
- Typed exception hierarchy: `AuthenticationException`, `ValidationException`, `ResourceNotFoundException`, `PaymobServerException`, `PaymobTimeoutException`.
- Configurable HTTP logging via `LogLevel` (NONE, BASIC, HEADERS, BODY).
- Bearer token caching with Caffeine (55-minute TTL) for subscription-related calls.
- Unit tests with JUnit 5, Mockito, and MockWebServer.
- Integration tests against real Paymob sandbox API.
- Published to Maven Central under `io.github.yusuf-hussien:paymob-java-sdk`.
