# Technical Design & Architecture

This document outlines the technical architecture, design patterns, and architectural decisions used in the Paymob Java SDK.

## 📋 Table of Contents

- [Architecture Overview](#architecture-overview)
- [Design Patterns](#design-patterns)
- [Module Structure](#module-structure)
- [Authentication Layer](#authentication-layer)
- [HTTP Client Design](#http-client-design)
- [Error Handling Strategy](#error-handling-strategy)
- [Configuration Management](#configuration-management)
- [Thread Safety](#thread-safety)
- [Performance Considerations](#performance-considerations)
- [Security Architecture](#security-architecture)

## 🏗️ Architecture Overview

The SDK follows a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                   Client Application                    │
├─────────────────────────────────────────────────────┤
│                PaymobClient (Facade)               │
├─────────────────────────────────────────────────────┤
│  Services Layer (Business Logic)                 │
│  ┌─────────────┬─────────────┬─────────────┐ │
│  │ Intention   │ Transaction  │ Subscription │ │
│  │ Service     │ Service     │ Service     │ │
│  └─────────────┴─────────────┴─────────────┘ │
├─────────────────────────────────────────────────────┤
│               HTTP Layer (Transport)                 │
│  ┌─────────────┬─────────────┬─────────────┐ │
│  │ HTTP Client │ Interceptors│ Auth Strategy│ │
│  └─────────────┴─────────────┴─────────────┘ │
├─────────────────────────────────────────────────────┤
│              Core Layer (Infrastructure)              │
│  ┌─────────────┬─────────────┬─────────────┐ │
│  │ Config      │ Models      │ Exceptions  │ │
│  └─────────────┴─────────────┴─────────────┘ │
└─────────────────────────────────────────────────────┘
```

### Key Architectural Principles

1. **Single Responsibility**: Each class has one reason to change
2. **Dependency Inversion**: Depend on abstractions, not concretions
3. **Interface Segregation**: Small, focused interfaces
4. **Open/Closed**: Open for extension, closed for modification
5. **Don't Repeat Yourself**: Shared utilities and common patterns

## 🎨 Design Patterns

### 1. Builder Pattern

Used for configuration and request objects to provide:
- **Fluent API**: Chainable method calls
- **Immutable Objects**: Thread-safe, predictable state
- **Validation**: Build-time validation of required fields

```java
PaymobConfig config = PaymobConfig.builder()
    .apiKey("api_key")
    .secretKey("secret_key")
    .region(Region.UAE)
    .build(); // Validates all required fields
```

### 2. Strategy Pattern

Used for authentication to support multiple auth methods:

```java
public interface AuthStrategy {
    void apply(Request.Builder requestBuilder);
}

public class SecretKeyAuthStrategy implements AuthStrategy {
    private final String secretKey;
    
    @Override
    public void apply(Request.Builder requestBuilder) {
        requestBuilder.header("Authorization", "Token " + secretKey);
    }
}

public class BearerTokenAuthStrategy implements AuthStrategy {
    private final String apiKey;
    private final String baseUrl;
    private final Cache<String, String> tokenCache;
    
    @Override
    public void apply(Request.Builder requestBuilder) {
        String token = getOrRefreshToken();
        requestBuilder.header("Authorization", "Bearer " + token);
    }
}
```

### 3. Factory Pattern

Used for creating region-specific configurations:

```java
public class PaymobRegionFactory {
    public static PaymobRegion fromString(String region) {
        return Arrays.stream(PaymobRegion.values())
            .filter(r -> r.name().equalsIgnoreCase(region))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid region: " + region));
    }
}
```

### 4. Template Method Pattern

Used in HTTP client for consistent request/response handling:

```java
public abstract class AbstractHttpClient {
    public <T> T post(String url, Object body, Class<T> responseType) {
        Request request = buildRequest(url, body);
        Response response = executeRequest(request);
        return handleResponse(response, responseType);
    }
    
    protected abstract Request buildRequest(String url, Object body);
    protected abstract Response executeRequest(Request request);
    protected abstract <T> T handleResponse(Response response, Class<T> responseType);
}
```

### 5. Observer Pattern

Used for metrics and logging:

```java
public interface PaymobMetricsRecorder {
    void recordApiCall(String service, String operation, String region, 
                      int statusCode, long durationMs, boolean success);
}

public class NoOpMetricsRecorder implements PaymobMetricsRecorder {
    public static final PaymobMetricsRecorder INSTANCE = new NoOpMetricsRecorder();
    
    @Override
    public void recordApiCall(String service, String operation, String region, 
                              int statusCode, long durationMs, boolean success) {
        // No-op implementation
    }
}
```

## 📁 Module Structure

### Core Package (`com.paymob.sdk.core`)

```java
// Main entry point and configuration
public class PaymobClient {
    private final PaymobConfig config;
    private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();
    
    // Lazy initialization of services
    public IntentionService intentions() {
        return (IntentionService) services.computeIfAbsent(
            IntentionService.class, 
            k -> new IntentionService(httpClient, config)
        );
    }
}

// Configuration with builder pattern
public class PaymobConfig {
    private final String apiKey;
    private final String secretKey;
    private final PaymobRegion region;
    // ... other fields
    
    private PaymobConfig(Builder builder) {
        this.apiKey = builder.apiKey;
        this.secretKey = builder.secretKey;
        this.region = builder.region;
        // Validate required fields
        validate();
    }
    
    public static class Builder {
        private String apiKey;
        private String secretKey;
        private PaymobRegion region;
        
        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }
        
        public PaymobConfig build() {
            return new PaymobConfig(this);
        }
    }
}
}
```

### Services Package (`com.paymob.sdk.services`)

Each service follows the same pattern:

```java
public class IntentionService {
    private final HttpClient httpClient;
    private final PaymobConfig config;
    
    public IntentionService(HttpClient httpClient, PaymobConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }
    
    public IntentionResponse create(IntentionRequest request) {
        // Validate request
        validateRequest(request);
        
        // Build URL
        String url = config.getRegion().getBaseUrl() + "/v1/intention/";
        
        // Execute request
        return httpClient.post(url, request, IntentionResponse.class);
    }
    
    private void validateRequest(IntentionRequest request) {
        if (request.getAmount() <= 0) {
            throw new ValidationException("Amount must be positive");
        }
        // ... other validations
    }
}
```

## 🔐 Authentication Layer

### Design Goals

1. **Pluggable**: Support different authentication methods
2. **Automatic**: Handle token refresh transparently
3. **Secure**: Never expose credentials in logs
4. **Efficient**: Cache tokens to avoid unnecessary requests

### Implementation Details

```java
// Bearer token strategy with caching
public class BearerTokenAuthStrategy implements AuthStrategy {
    private final String apiKey;
    private final String baseUrl;
    private final HttpClient httpClient;
    private final Cache<String, String> tokenCache;
    
    public BearerTokenAuthStrategy(String apiKey, String baseUrl, HttpClient httpClient) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.httpClient = httpClient;
        this.tokenCache = Caffeine.newBuilder()
            .maximumSize(1)
            .expireAfterWrite(55, TimeUnit.MINUTES) // 55 min TTL
            .build();
    }
    
    @Override
    public void apply(Request.Builder requestBuilder) {
        String token = tokenCache.get("bearer", k -> fetchNewToken());
        requestBuilder.header("Authorization", "Bearer " + token);
    }
    
    private String fetchNewToken() {
        TokenRequest tokenRequest = new TokenRequest(apiKey);
        TokenResponse response = httpClient.post(baseUrl + "/api/auth/tokens", 
                                         tokenRequest, TokenResponse.class);
        return response.getToken();
    }
}
```

## 🌐 HTTP Client Design

### Interceptor Chain

```java
public class OkHttpClientAdapter implements HttpClient {
    private final OkHttpClient client;
    private final List<Interceptor> interceptors;
    
    public OkHttpClientAdapter(PaymobConfig config, AuthStrategy authStrategy) {
        this.interceptors = Arrays.asList(
            new AuthInterceptor(authStrategy),
            new RetryInterceptor(config.getMaxRetries()),
            new LoggingInterceptor(config.getLogLevel())
        );
        
        this.client = new OkHttpClient.Builder()
            .connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
            .readTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
            .addInterceptor(new ChainInterceptor(interceptors))
            .build();
    }
}

// Interceptor chain implementation
private static class ChainInterceptor implements Interceptor {
    private final List<Interceptor> interceptors;
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        
        for (Interceptor interceptor : interceptors) {
            Response response = interceptor.intercept(chain);
            if (response != null) {
                return response;
            }
        }
        
        return chain.proceed(request);
    }
}
```

### Retry Interceptor

```java
public class RetryInterceptor implements Interceptor {
    private final int maxRetries;
    private final long baseDelayMs;
    
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        IOException lastException = null;
        
        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                response = chain.proceed(request);
                if (response.isSuccessful() || !isRetryable(response.code())) {
                    return response;
                }
            } catch (IOException e) {
                lastException = e;
                if (attempt == maxRetries) {
                    throw e;
                }
            }
            
            // Exponential backoff
            try {
                Thread.sleep(baseDelayMs * (1L << attempt));
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new IOException("Interrupted during retry", ie);
            }
        }
        
        return response;
    }
    
    private boolean isRetryable(int code) {
        return code == 500 || code == 502 || code == 503 || code == 504;
    }
}
```

## ⚠️ Error Handling Strategy

### Exception Hierarchy

```java
// Base exception for all Paymob errors
public abstract class PaymobException extends Exception {
    private final int httpStatus;
    private final String paymobErrorBody;
    
    protected PaymobException(int httpStatus, String paymobErrorBody, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.paymobErrorBody = paymobErrorBody;
    }
    
    // Getters...
}

// Specific exception types
public class AuthenticationException extends PaymobException {
    public AuthenticationException(int httpStatus, String errorBody, String message) {
        super(httpStatus, errorBody, message);
    }
}

public class ValidationException extends PaymobException {
    public ValidationException(String message) {
        super(406, null, message);
    }
}

public class PaymobServerException extends PaymobException {
    public PaymobServerException(int httpStatus, String errorBody, String message) {
        super(httpStatus, errorBody, message);
    }
}
```

### Error Response Handling

```java
public class ResponseHandler {
    public <T> T handleResponse(Response response, Class<T> responseType) throws PaymobException {
        int code = response.code();
        
        if (code == 200) {
            return parseSuccessResponse(response, responseType);
        } else if (code == 401) {
            throw new AuthenticationException(code, response.body(), "Authentication failed");
        } else if (code == 406) {
            throw new ValidationException(parseValidationError(response.body()));
        } else if (code >= 500) {
            throw new PaymobServerException(code, response.body(), "Server error");
        } else {
            throw new PaymobException(code, response.body(), "HTTP " + code);
        }
    }
}
```

## ⚙️ Configuration Management

### Type-Safe Configuration

```java
public class PaymobConfig {
    // Required fields
    private final String secretKey;
    private final PaymobRegion region;
    
    // Optional fields with defaults
    private final String apiKey;
    private final String publicKey;
    private final String hmacSecret;
    private final int connectTimeoutSeconds;
    private final int readTimeoutSeconds;
    private final LogLevel logLevel;
    private final PaymobMetricsRecorder metricsRecorder;
    
    // Builder with validation
    public static class Builder {
        // Field initialization with defaults
        private String secretKey;
        private PaymobRegion region;
        private String apiKey;
        private String publicKey;
        private String hmacSecret;
        private int connectTimeoutSeconds = 10;
        private int readTimeoutSeconds = 30;
        private LogLevel logLevel = LogLevel.BASIC;
        private PaymobMetricsRecorder metricsRecorder = NoOpMetricsRecorder.INSTANCE;
        
        public PaymobConfig build() {
            // Validate required fields
            if (secretKey == null) {
                throw new IllegalArgumentException("secretKey is required");
            }
            if (region == null) {
                throw new IllegalArgumentException("region is required");
            }
            
            return new PaymobConfig(this);
        }
    }
}
```

### Environment Variable Support

```java
public class ConfigurationLoader {
    public static PaymobConfig fromEnvironment() {
        return PaymobConfig.builder()
            .apiKey(System.getenv("PAYMOB_API_KEY"))
            .secretKey(System.getenv("PAYMOB_SECRET_KEY"))
            .publicKey(System.getenv("PAYMOB_PUBLIC_KEY"))
            .region(PaymobRegion.fromString(System.getenv("PAYMOB_REGION", "EGYPT")))
            .hmacSecret(System.getenv("PAYMOB_HMAC_SECRET"))
            .connectTimeoutSeconds(parseInt(System.getenv("PAYMOB_CONNECT_TIMEOUT", "10")))
            .readTimeoutSeconds(parseInt(System.getenv("PAYMOB_READ_TIMEOUT", "30")))
            .build();
    }
}
```

## 🧵 Thread Safety

### Immutable Objects

All request/response objects are immutable:

```java
public final class IntentionResponse {
    private final String id;
    private final String clientSecret;
    private final String paymentUrl;
    
    public IntentionResponse(String id, String clientSecret, String paymentUrl) {
        this.id = id;
        this.clientSecret = clientSecret;
        this.paymentUrl = paymentUrl;
    }
    
    // Only getters, no setters
    public String getId() { return id; }
    public String getClientSecret() { return clientSecret; }
    public String getPaymentUrl() { return paymentUrl; }
}
```

### Concurrent Service Access

```java
public class PaymobClient {
    private final ConcurrentMap<Class<?>, Object> services = new ConcurrentHashMap<>();
    
    @SuppressWarnings("unchecked")
    public <T> T getService(Class<T> serviceClass, Supplier<T> factory) {
        return (T) services.computeIfAbsent(serviceClass, k -> factory.get());
    }
}
```

### Thread-Safe Caching

```java
public class BearerTokenAuthStrategy {
    private final Cache<String, String> tokenCache;
    
    public BearerTokenAuthStrategy() {
        this.tokenCache = Caffeine.newBuilder()
            .maximumSize(1)
            .expireAfterWrite(55, TimeUnit.MINUTES)
            .build(); // Thread-safe by default
    }
    
    public String getToken() {
        return tokenCache.get("bearer", k -> fetchNewToken());
    }
}
```

## ⚡ Performance Considerations

### Connection Pooling

```java
public class OkHttpClientAdapter {
    private OkHttpClient createClient(PaymobConfig config) {
        return new OkHttpClient.Builder()
            .connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES))
            .connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS)
            .readTimeout(config.getReadTimeout(), TimeUnit.SECONDS)
            .build();
    }
}
```

### Lazy Initialization

```java
public class PaymobClient {
    private volatile IntentionService intentionService;
    
    public IntentionService intentions() {
        if (intentionService == null) {
            synchronized (this) {
                if (intentionService == null) {
                    intentionService = new IntentionService(httpClient, config);
                }
            }
        }
        return intentionService;
    }
}
```

### Efficient JSON Processing

```java
public class JsonMapper {
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    
    public static String toJson(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }
    
    public static <T> T fromJson(String json, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(json, clazz);
    }
}
```

## 🛡️ Security Architecture

### Credential Protection

```java
public class SensitiveDataFilter {
    private static final Set<String> SENSITIVE_FIELDS = Set.of(
        "secret_key", "api_key", "cvv", "card_number", "token"
    );
    
    public static String maskSensitiveData(String json) {
        String masked = json;
        for (String field : SENSITIVE_FIELDS) {
            masked = masked.replaceAll(
                "\"" + field + "\"\\s*:\\s*\"[^\"]*\"",
                "\"" + field + "\": \"[REDACTED]\""
            );
        }
        return masked;
    }
}
```

### HMAC Security

```java
public class HmacUtil {
    public static String computeHmacSha512(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec keySpec = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8), "HmacSHA512"
            );
            mac.init(keySpec);
            byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            return Hex.encodeHexString(hmacBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to compute HMAC", e);
        }
    }
    
    public static boolean verifyHmac(String data, String secret, String expectedHmac) {
        String computedHmac = computeHmacSha512(data, secret);
        return MessageDigest.isEqual(
            computedHmac.getBytes(StandardCharsets.UTF_8),
            expectedHmac.getBytes(StandardCharsets.UTF_8)
        );
    }
}
```

### Input Validation

```java
public class Validator {
    public static void validateAmount(int amount) {
        if (amount <= 0) {
            throw new ValidationException("Amount must be positive");
        }
        if (amount > 1_000_000_00) { // 1 million in cents
            throw new ValidationException("Amount exceeds maximum limit");
        }
    }
    
    public static void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new ValidationException("Invalid email address");
        }
    }
    
    public static void validatePhoneNumber(String phone) {
        if (phone == null || !phone.matches("^\\+?[1-9]\\d{6,15}$")) {
            throw new ValidationException("Invalid phone number");
        }
    }
}
```

## 📊 Metrics and Monitoring

### Metrics Collection Design

```java
public interface PaymobMetricsRecorder {
    void recordApiCall(String service, String operation, String region, 
                      int statusCode, long durationMs, boolean success);
    
    void recordTokenRefresh(String region);
    
    void recordRetry(String service, int attempt, int statusCode);
    
    // No-op implementation
    PaymobMetricsRecorder NOOP = new PaymobMetricsRecorder() {
        // Default no-op implementations
    };
}
```

### Built-in Metrics Implementation

```java
public class DefaultMetricsRecorder implements PaymobMetricsRecorder {
    private final Logger logger = LoggerFactory.getLogger(DefaultMetricsRecorder.class);
    
    @Override
    public void recordApiCall(String service, String operation, String region, 
                              int statusCode, long durationMs, boolean success) {
        if (logger.isDebugEnabled()) {
            logger.debug("API call: {}.{} in {} - {} ({}ms) - {}", 
                service, operation, region, statusCode, durationMs, success ? "SUCCESS" : "FAILED");
        }
    }
    
    @Override
    public void recordTokenRefresh(String region) {
        logger.info("Token refreshed for region: {}", region);
    }
    
    @Override
    public void recordRetry(String service, int attempt, int statusCode) {
        logger.warn("Retry triggered for {} - attempt {} - status {}", 
            service, attempt, statusCode);
    }
}
```

---

This technical design ensures the SDK is:
- **Maintainable**: Clear separation of concerns and consistent patterns
- **Extensible**: Plugin architecture for authentication and metrics
- **Secure**: Proper credential handling and input validation
- **Performant**: Efficient connection management and caching
- **Thread-Safe**: Immutable objects and concurrent access patterns
- **Observable**: Comprehensive metrics and logging capabilities
