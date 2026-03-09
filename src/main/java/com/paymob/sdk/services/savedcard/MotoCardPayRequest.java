package com.paymob.sdk.services.savedcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request payload for MOTO saved card payment execution.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MotoCardPayRequest {
    @JsonProperty("source")
    private Source source;

    @JsonProperty("payment_token")
    private String paymentToken;

    public MotoCardPayRequest() {
    }

    public MotoCardPayRequest(String cardToken, String paymentToken) {
        this.source = new Source(cardToken, "TOKEN");
        this.paymentToken = paymentToken;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Source {
        @JsonProperty("identifier")
        private String identifier;

        @JsonProperty("subtype")
        private String subtype;

        public Source() {
        }

        public Source(String identifier, String subtype) {
            this.identifier = identifier;
            this.subtype = subtype;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getSubtype() {
            return subtype;
        }

        public void setSubtype(String subtype) {
            this.subtype = subtype;
        }
    }
}