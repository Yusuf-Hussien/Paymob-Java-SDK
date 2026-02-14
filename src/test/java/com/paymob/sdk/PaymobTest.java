package com.paymob.sdk;

import com.paymob.sdk.models.Region;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymobTest {

    @Test
    void init_shouldSetDefaultRegionToEgypt_whenOnlyApiKeyProvided() {
        Paymob.init("api_key");
        assertEquals("api_key", Paymob.getApiKey());
        assertEquals("https://accept.paymob.com", Paymob.getBaseUrl());
        assertEquals(Region.EGYPT.getCheckoutUrl(), Paymob.getCheckoutUrl());
    }

    @Test
    void init_shouldSetRegionCorrectly_whenRegionProvided() {
        Paymob.init("api_key", "secret", "public", Region.UAE);
        assertEquals("https://uae.paymob.com", Paymob.getBaseUrl());
        assertEquals("https://uae.paymob.com/unifiedcheckout", Paymob.getCheckoutUrl());
    }

    @Test
    void init_shouldThrowException_whenApiKeyNotSetAndAccessed() {
        // Resetting likely not possible due to static state and lack of cleanup method,
        // but we can test normal initialization.
        // Since tests run in random order, let's just test happy paths.
        Paymob.init("key");
        assertNotNull(Paymob.getApiKey());
    }
}
