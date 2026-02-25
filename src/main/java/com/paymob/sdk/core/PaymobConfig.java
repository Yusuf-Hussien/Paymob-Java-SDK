package com.paymob.sdk.core;

import com.paymob.sdk.models.enums.LogLevel;
import java.time.Duration;

/**
 * Configuration class for Paymob SDK.
 * Contains all necessary settings for API communication.
 */
public class PaymobConfig {
    private final String secretKey;
    private final String apiKey;
    private final String publicKey;
    private final String hmacSecret;
    private final PaymobRegion region;
    private final Duration timeout;
    private final LogLevel logLevel;

    private PaymobConfig(Builder builder) {
        this.secretKey = builder.secretKey;
        this.apiKey = builder.apiKey;
        this.publicKey = builder.publicKey;
        this.hmacSecret = builder.hmacSecret;
        this.region = builder.region;
        this.timeout = builder.timeout;
        this.logLevel = builder.logLevel;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getHmacSecret() {
        return hmacSecret;
    }

    public PaymobRegion getRegion() {
        return region;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String secretKey;
        private String apiKey;
        private String publicKey;
        private String hmacSecret;
        private PaymobRegion region = PaymobRegion.EGYPT;
        private Duration timeout = Duration.ofSeconds(60);
        private LogLevel logLevel = LogLevel.NONE;

        public Builder secretKey(String secretKey) {
            this.secretKey = secretKey;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public Builder publicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public Builder hmacSecret(String hmacSecret) {
            this.hmacSecret = hmacSecret;
            return this;
        }

        public Builder region(PaymobRegion region) {
            this.region = region;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder timeout(int seconds) {
            this.timeout = Duration.ofSeconds(seconds);
            return this;
        }

        public Builder logLevel(LogLevel logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public PaymobConfig build() {
            if (secretKey == null) {
                throw new IllegalArgumentException("Secret key is required");
            }
            return new PaymobConfig(this);
        }
    }
}
