package com.paymob.sdk.models.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents an error response from Paymob API.
 * Used for deserializing error JSON bodies.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymobError {
    @JsonProperty("detail")
    private String detail;

    @JsonProperty("message")
    private String message;

    @JsonProperty("code")
    private String code;

    public PaymobError() {
    }

    public PaymobError(String detail, String message, String code) {
        this.detail = detail;
        this.message = message;
        this.code = code;
    }

    /**
     * Gets the detailed error description.
     * 
     * @return The error detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Gets the error message.
     * 
     * @return The error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the error code.
     * 
     * @return The error code
     */
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "PaymobError{" +
                "detail='" + detail + '\'' +
                ", message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
