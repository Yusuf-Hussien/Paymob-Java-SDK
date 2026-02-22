package com.paymob.sdk.webhook;

/**
 * Strategy interface for parsing webhook payloads into {@link WebhookEvent}.
 * <p>
 * Each webhook type implements its own parser to detect payload shape
 * and resolve the correct {@link WebhookEventType}.
 * <p>
 * To support a new webhook type, implement this interface and register
 * it in {@link WebhookValidator}.
 */
public interface WebhookEventParser {

    /**
     * Returns true if this parser can handle the given payload shape.
     */
    boolean canParse(String payload);

    /**
     * Parses the payload into a {@link WebhookEvent} with the correct event type.
     */
    WebhookEvent parse(String payload);
}
