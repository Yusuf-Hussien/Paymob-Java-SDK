# System Design & Architecture

This document describes the complete system design of the Paymob Java SDK — its layers, components, data flow, design decisions, and how everything connects.

## Table of Contents

- [High-Level Architecture](#high-level-architecture)
- [Component Map](#component-map)
- [Layer Breakdown](#layer-breakdown)
- [Authentication System](#authentication-system)
- [HTTP Pipeline](#http-pipeline)
- [Service Layer](#service-layer)
- [Webhook System](#webhook-system)
- [Exception Hierarchy](#exception-hierarchy)
- [Data Flow — Payment Intention](#data-flow--payment-intention)
- [Data Flow — Webhook Processing](#data-flow--webhook-processing)
- [Design Decisions](#design-decisions)

---

## High-Level Architecture

```mermaid
graph TB
    subgraph Application["Client Application"]
        APP[Your Java App]
    end

    subgraph SDK["Paymob Java SDK"]
        direction TB

        subgraph Entry["Entry Point"]
            PC[PaymobClient]
            CFG[PaymobConfig]
            RGN[PaymobRegion]
        end

        subgraph Auth["Authentication Layer"]
            AS[AuthStrategy interface]
            SK[SecretKeyAuthStrategy]
            BT[BearerTokenAuthStrategy]
            CC[Caffeine Token Cache]
        end

        subgraph Services["Service Layer"]
            IS[IntentionService]
            TS[TransactionService]
            IQS[TransactionInquiryService]
            SCS[SavedCardService]
            SS[SubscriptionService]
            SPS[SubscriptionPlanService]
            QLS[QuickLinkService]
        end

        subgraph HTTP["HTTP Layer"]
            HC[HttpClient interface]
            OKH[OkHttpClientAdapter]
            subgraph Interceptors["Interceptor Chain"]
                AI[AuthInterceptor]
                RI[RetryInterceptor]
                LI[LoggingInterceptor]
            end
            JM[Jackson ObjectMapper]
        end

        subgraph Webhook["Webhook System"]
            WV[WebhookValidator]
            subgraph Calculators["Signature Calculators"]
                TSC[TransactionSignatureCalculator]
                SSC[SubscriptionSignatureCalculator]
                CSC[CardTokenSignatureCalculator]
            end
            subgraph Parsers["Event Parsers"]
                TEP[TransactionWebhookEventParser]
                SEP[SubscriptionWebhookEventParser]
                CEP[CardTokenWebhookEventParser]
            end
            WE[WebhookEvent]
            WET[WebhookEventType enum]
        end

        subgraph Exceptions["Exception Layer"]
            PE[PaymobException]
            AE[AuthenticationException]
            VE[ValidationException]
            NF[ResourceNotFoundException]
            SE[PaymobServerException]
            TE[PaymobTimeoutException]
        end
    end

    subgraph Paymob["Paymob API"]
        direction LR
        AUTH_EP[POST /api/auth/tokens]
        INT_EP[POST /v1/intention/]
        TXN_EP[POST /api/acceptance/void_refund]
        INQ_EP[POST /api/ecommerce/orders/transaction_inquiry]
        SUB_EP[POST/GET /api/acceptance/subscriptions]
        PLAN_EP[POST/GET /api/acceptance/subscription-plans]
        QL_EP[POST /api/ecommerce/payment-links]
        SC_EP[POST /v1/intention/ with token]
    end

    APP --> PC
    PC --> CFG
    PC --> Auth
    PC --> Services

    SK --> AS
    BT --> AS
    BT --> CC

    Services --> HC
    HC --> OKH
    OKH --> Interceptors
    OKH --> JM
    AI --> AS

    OKH -->|HTTPS| Paymob
    BT -->|Token fetch| AUTH_EP
    IS --> INT_EP
    TS --> TXN_EP
    IQS --> INQ_EP
    SS --> SUB_EP
    SPS --> PLAN_EP
    QLS --> QL_EP
    SCS --> SC_EP

    OKH -->|error| Exceptions

    APP --> WV
    WV --> Calculators
    WV --> Parsers
    Parsers --> WE
    WE --> WET
```

---

## Component Map

```mermaid
graph LR
    subgraph core["core/"]
        PaymobClient
        PaymobConfig
        PaymobRegion

        subgraph auth["auth/"]
            AuthStrategy
            SecretKeyAuthStrategy
            BearerTokenAuthStrategy
        end
    end

    subgraph http["http/"]
        HttpClient
        OkHttpClientAdapter
        subgraph interceptors["interceptors/"]
            AuthInterceptor
            RetryInterceptor
            LoggingInterceptor
        end
    end

    subgraph services["services/"]
        subgraph intention["intention/"]
            IntentionService
            IntentionRequest
            IntentionResponse
        end
        subgraph transaction["transaction/"]
            TransactionService
            RefundRequest
            VoidRequest
            CaptureRequest
            TransactionResponse
        end
        subgraph inquiry["inquiry/"]
            TransactionInquiryService
            InquiryRequest
            InquiryResponse
        end
        subgraph savedcard["savedcard/"]
            SavedCardService
            CitPaymentRequest
            MitPaymentRequest
            TokenizedPaymentResponse
        end
        subgraph subscription["subscription/"]
            SubscriptionService
            subgraph plan["plan/"]
                SubscriptionPlanService
                SubscriptionPlanRequest
                SubscriptionPlanResponse
            end
        end
        subgraph quicklink["quicklink/"]
            QuickLinkService
            QuickLinkRequest
            QuickLinkResponse
        end
    end

    subgraph models["models/"]
        subgraph common["common/"]
            BillingData
            Item
            Amount
        end
        subgraph enums["enums/"]
            Currency
            LogLevel
        end
    end

    subgraph exceptions["exceptions/"]
        PaymobException
        AuthenticationException
        ValidationException
        ResourceNotFoundException
        PaymobServerException
        PaymobTimeoutException
    end

    subgraph webhook["webhook/"]
        WebhookValidator
        WebhookEvent
        WebhookEventType

        subgraph signature["signature/"]
            TransactionSignatureCalculator
            SubscriptionSignatureCalculator
            CardTokenSignatureCalculator
        end
        subgraph parser["parser/"]
            TransactionWebhookEventParser
            SubscriptionWebhookEventParser
            CardTokenWebhookEventParser
        end
    end
```

---

## Layer Breakdown

```mermaid
graph TB
    L1["Layer 1 — Entry Point
    PaymobClient · PaymobConfig · PaymobRegion
    Single facade, immutable config, thread-safe"]

    L2["Layer 2 — Authentication
    AuthStrategy interface
    SecretKeyAuthStrategy · BearerTokenAuthStrategy
    Caffeine 55-min token cache"]

    L3["Layer 3 — Services
    IntentionService · TransactionService
    InquiryService · SavedCardService
    SubscriptionService · SubscriptionPlanService · QuickLinkService
    Each service holds: HttpClient + AuthStrategy + PaymobConfig"]

    L4["Layer 4 — HTTP
    HttpClient interface → OkHttpClientAdapter
    OkHttp3 connection pool
    AuthInterceptor → RetryInterceptor → LoggingInterceptor
    Jackson ObjectMapper snake_case + NON_NULL"]

    L5["Layer 5 — Paymob API
    HTTPS endpoints per region
    accept.paymob.com · ksa.paymob.com · uae.paymob.com · oman.paymob.com"]

    L1 --> L2
    L2 --> L3
    L3 --> L4
    L4 --> L5
```

---

## Authentication System

```mermaid
flowchart TD
    START([Service makes API call]) --> Q{Which auth?}

    Q -->|Intention, Transaction,\nSavedCard| SK[SecretKeyAuthStrategy]
    Q -->|Inquiry, SubscriptionPlans,\nQuickLink, Subscription mgmt| BT[BearerTokenAuthStrategy]

    SK --> SKH["Header: Authorization: Token sk_..."]
    SKH --> REQ[Outgoing HTTP Request]

    BT --> CACHE{Caffeine cache\nhit?}
    CACHE -->|Yes — token valid| BTH["Header: Authorization: Bearer token"]
    CACHE -->|No — expired or missing| FETCH[POST /api/auth/tokens\nwith api_key]
    FETCH --> STORE[Store in cache\n55-min TTL]
    STORE --> BTH
    BTH --> REQ

    REQ --> PAYMOB[Paymob API]

    style SK fill:#dbeafe
    style BT fill:#f3e8ff
    style CACHE fill:#fef9c3
```

**Why two strategies?**

Paymob exposes two auth schemes depending on the API group. Most modern endpoints accept a `Token <secret_key>` header directly. Older subscription and inquiry endpoints require a short-lived Bearer token obtained by exchanging the API key. The SDK handles both transparently — callers never deal with auth headers directly.

The Bearer token is cached in Caffeine with a 55-minute TTL (Paymob tokens expire after 60 minutes, leaving a 5-minute safety buffer). Token refresh is synchronized under a lock to prevent concurrent fetches when the cache expires.

---

## HTTP Pipeline

```mermaid
sequenceDiagram
    participant SVC as Service
    participant OKH as OkHttpClientAdapter
    participant AI as AuthInterceptor
    participant RI as RetryInterceptor
    participant LI as LoggingInterceptor
    participant NET as Paymob API

    SVC->>OKH: post(endpoint, body, ResponseClass, authStrategy)
    OKH->>OKH: Jackson serialize body to JSON
    OKH->>OKH: Build OkHttp Request
    OKH->>AI: intercept(chain)
    AI->>AI: Apply Authorization header
    AI->>RI: chain.proceed(request)
    RI->>LI: chain.proceed(request)
    LI->>NET: HTTPS request
    NET-->>LI: HTTP response
    LI-->>RI: response
    RI->>RI: 5xx? → backoff & retry\n4xx or 2xx? → pass through
    RI-->>AI: response
    AI-->>OKH: response
    OKH->>OKH: Check status code
    OKH->>OKH: 2xx → Jackson deserialize to ResponseClass
    OKH->>OKH: 4xx/5xx → mapToException()
    OKH-->>SVC: ResponseClass or throws PaymobException
```

**Retry logic:** `RetryInterceptor` retries up to 3 times on 5xx responses with exponential backoff (1s → 2s → 4s, capped at 10s). 4xx responses are never retried — they represent a problem with the request itself, not a transient server issue.

**Serialization:** Jackson uses `SNAKE_CASE` naming and omits null fields. This means Java `billingData` serializes as `billing_data` matching the Paymob API contract, and optional fields left null are cleanly absent from the request body.

---

## Service Layer

```mermaid
graph TD
    PC[PaymobClient] --> IS[IntentionService\nAuth: SecretKey]
    PC --> TS[TransactionService\nAuth: SecretKey]
    PC --> IQS[TransactionInquiryService\nAuth: Bearer]
    PC --> SCS[SavedCardService\nAuth: SecretKey]
    PC --> SS[SubscriptionService\nAuth: SecretKey + Bearer]
    PC --> SPS[SubscriptionPlanService\nAuth: Bearer]
    PC --> QLS[QuickLinkService\nAuth: Bearer]

    IS -->|POST /v1/intention/| PA[Paymob API]
    TS -->|POST /api/acceptance/void_refund/*\nPOST /api/acceptance/capture| PA
    IQS -->|POST /api/ecommerce/orders/transaction_inquiry\nGET /api/acceptance/transactions/id| PA
    SCS -->|POST /v1/intention/ with cardToken| PA
    SS -->|POST /v1/intention/ enrollment\nGET/POST /api/acceptance/subscriptions/*| PA
    SPS -->|GET/POST /api/acceptance/subscription-plans/*| PA
    QLS -->|POST /api/ecommerce/payment-links\nPOST /api/ecommerce/payment-links/cancel| PA

    style IS fill:#dcfce7
    style TS fill:#dcfce7
    style SCS fill:#dcfce7
    style SS fill:#f3e8ff
    style IQS fill:#f3e8ff
    style SPS fill:#f3e8ff
    style QLS fill:#f3e8ff
```

**Green = SecretKey auth | Purple = Bearer auth**

`SubscriptionService` is unique: enrollment calls (`subscribe`, `addSecondaryCard`) use SecretKey auth because they go through the Intention API. Management calls (`suspend`, `resume`, `cancel`, `list`, etc.) use Bearer auth because they hit the older subscription endpoints.

---

## Webhook System

```mermaid
flowchart TD
    RAW[Raw POST body + hmac query param] --> WV[WebhookValidator]

    WV --> DETECT{Auto-detect\nwebhook type}

    DETECT -->|has type=TRANSACTION\nor obj.success| TSC[TransactionSignatureCalculator\nConcatenates 20 transaction fields\nin exact Paymob-defined order]

    DETECT -->|has trigger_type\nor subscription_data| SSC[SubscriptionSignatureCalculator\ntrigger_type + for + subscription_data.id]

    DETECT -->|has type=TOKEN\nor obj.token + masked_pan| CSC[CardTokenSignatureCalculator\nConcatenates 8 card token fields]

    TSC --> HMAC[HMAC-SHA512\nover concatenated string\nusing hmacSecret]
    SSC --> HMAC
    CSC --> HMAC

    HMAC --> CMP{MessageDigest.isEqual\nconstant-time compare}

    CMP -->|Mismatch| REJECT[Return null\nReject — 401]
    CMP -->|Match| PARSE[Route to matching\nWebhookEventParser]

    PARSE -->|Transaction payload| TEP[TransactionWebhookEventParser\n→ WebhookEventType.TRANSACTION_*]
    PARSE -->|Subscription payload| SEP[SubscriptionWebhookEventParser\n→ WebhookEventType.SUBSCRIPTION_*]
    PARSE -->|Token payload| CEP[CardTokenWebhookEventParser\n→ WebhookEventType.CARD_TOKEN]

    TEP --> WE[WebhookEvent\ntype + raw obj node]
    SEP --> WE
    CEP --> WE

    WE --> APP[event.getData Class\nJackson deserialize obj node\ninto TransactionResponse\nSubscriptionResponse\nor token String]

    style REJECT fill:#ffebee
    style CMP fill:#fef9c3
    style WE fill:#dcfce7
```

**Why separate Calculators and Parsers?**

Paymob uses different field concatenation rules for each webhook type when computing the HMAC. The `WebhookSignatureCalculator` hierarchy isolates this per-type concatenation logic. The `WebhookEventParser` hierarchy isolates the payload shape parsing. Keeping them separate means adding a new webhook type (a new calculator + a new parser) requires zero changes to the existing classes or to `WebhookValidator` beyond registering the new pair.

---

## Exception Hierarchy

```mermaid
graph TD
    RE[RuntimeException]
    PE[PaymobException\nhttpStatus · errorBody\nbase for all SDK errors]
    AE[AuthenticationException\nHTTP 401\nWrong key or region]
    VE[ValidationException\nHTTP 406\nBad request fields]
    NF[ResourceNotFoundException\nHTTP 404\nNot found]
    SE[PaymobServerException\nHTTP 5xx\nServer-side error]
    TE[PaymobTimeoutException\nNetwork timeout]

    RE --> PE
    PE --> AE
    PE --> VE
    PE --> NF
    PE --> SE
    PE --> TE
```

All exceptions are unchecked (`RuntimeException`). `PaymobException` carries `httpStatus` and `errorBody` so callers can inspect the raw Paymob error response alongside the SDK message.

The `mapToException()` method in `OkHttpClientAdapter` is the single place where HTTP status codes are mapped to typed exceptions.

---

## Data Flow — Payment Intention

```mermaid
sequenceDiagram
    participant APP as Your Application
    participant PC as PaymobClient
    participant IS as IntentionService
    participant OKH as OkHttpClientAdapter
    participant SK as SecretKeyAuthStrategy
    participant PAY as Paymob API

    APP->>PC: new PaymobClient(config)
    PC->>SK: new SecretKeyAuthStrategy(secretKey)
    PC->>IS: new IntentionService(httpClient, secretKeyAuth, config)

    APP->>PC: client.intentions().createIntention(request)
    PC->>IS: createIntention(request)
    IS->>OKH: setBaseUrl(region.getBaseUrl())
    IS->>OKH: post("/v1/intention/", request, IntentionResponse.class, secretKeyAuth)
    OKH->>OKH: Jackson → serialize request as JSON snake_case
    OKH->>SK: getAuthorizationHeader()
    SK-->>OKH: "Token sk_live_..."
    OKH->>PAY: POST /v1/intention/\nAuthorization: Token sk_live_...\n{amount, currency, payment_methods, ...}
    PAY-->>OKH: 200 OK\n{id, client_secret, intention_order_id, ...}
    OKH->>OKH: Jackson → deserialize → IntentionResponse
    OKH-->>IS: IntentionResponse
    IS-->>APP: IntentionResponse

    APP->>IS: getUnifiedCheckoutUrl(response)
    IS-->>APP: "https://accept.paymob.com/unifiedcheckout/?publicKey=pk_...&clientSecret=pi_..."
    APP->>APP: redirect customer to URL
```

---

## Data Flow — Webhook Processing

```mermaid
sequenceDiagram
    participant PAY as Paymob API
    participant SRV as Your HTTP Server
    participant WV as WebhookValidator
    participant CALC as SignatureCalculator
    participant PARS as EventParser
    participant APP as Your Handler

    PAY->>SRV: POST /webhook?hmac=abc123\n{type: TRANSACTION, obj: {...}}

    SRV->>WV: validateAndParse(rawBody, "abc123")
    WV->>WV: validateSignature(rawBody, hmac)
    WV->>CALC: canHandle(rawBody)?
    CALC-->>WV: true (TransactionSignatureCalculator)
    WV->>CALC: calculateExpectedHmac(rawBody, hmacSecret)
    CALC->>CALC: parse JSON, concatenate 20 fields in order
    CALC->>CALC: HMAC-SHA512(concatenated, hmacSecret)
    CALC-->>WV: expectedHmac
    WV->>WV: MessageDigest.isEqual(expected, received)

    alt Mismatch
        WV-->>SRV: return null
        SRV-->>PAY: 401 Unauthorized
    else Match
        WV->>PARS: canParse(rawBody)?
        PARS-->>WV: true (TransactionWebhookEventParser)
        WV->>PARS: parse(rawBody)
        PARS->>PARS: read type field → TRANSACTION_SUCCESSFUL
        PARS->>PARS: read obj node → store in WebhookEvent
        PARS-->>WV: WebhookEvent{type=TRANSACTION_SUCCESSFUL, obj=JsonNode}
        WV-->>SRV: WebhookEvent

        SRV->>APP: event.getType() → TRANSACTION_SUCCESSFUL
        APP->>APP: event.getData(TransactionResponse.class)
        APP->>APP: update order in database
        SRV-->>PAY: 200 OK
    end
```

---

## Design Decisions

### Why is `PaymobClient` a simple class and not a Builder-only construct?

`PaymobClient` supports both `new PaymobClient(config)` for simple cases and `PaymobClient.builder().config(...).httpClient(...).build()` for advanced use (e.g. injecting a custom `HttpClient` for testing). The simple constructor is the 95% case.

### Why are services eagerly initialized instead of lazy?

All services are created at `PaymobClient` construction time. This means startup is slightly heavier but there are no hidden costs or synchronization issues on first use. Services are stateless objects — creating them upfront is safe and predictable.

### Why does `SubscriptionService` receive both auth strategies?

Enrollment goes through the Intention API (SecretKey) while management operations (suspend, resume, list) go through older endpoints (Bearer). Rather than creating two separate service objects or switching auth inside the HTTP client, each call inside `SubscriptionService` explicitly passes the correct strategy. This makes the auth decision visible at the call site.

### Why is `HttpClient` an interface?

`HttpClient` is an interface so that tests can inject a `MockWebServer`-backed adapter or a pure mock without touching production code. The only production implementation is `OkHttpClientAdapter`.

### Why are all request/response objects mutable (have setters)?

Jackson requires either a no-arg constructor with setters, or `@JsonCreator` annotations. Setters were chosen for simplicity. Builders are provided for all request objects to give callers a clean construction API while keeping Jackson deserialization straightforward on responses.
