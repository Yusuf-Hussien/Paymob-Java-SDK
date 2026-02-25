package com.paymob.sdk.models.enums;

/**
 * Defines the amount of diagnostic information logged for HTTP requests and
 * responses.
 */
public enum LogLevel {
    /**
     * No logs.
     */
    NONE,

    /**
     * Logs request and response lines plus their respective headers and bodies (if
     * present).
     */
    BODY,

    /**
     * Logs request and response lines plus their respective headers.
     */
    HEADERS,

    /**
     * Logs request and response lines.
     */
    BASIC
}
