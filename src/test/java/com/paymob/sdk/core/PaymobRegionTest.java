package com.paymob.sdk.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymobRegionTest {

    @Test
    void testEgyptRegion() {
        PaymobRegion egypt = PaymobRegion.EGYPT;
        assertEquals("https://accept.paymob.com", egypt.getBaseUrl());
        assertEquals("https://accept.paymob.com", egypt.getCheckoutUrl());
    }

    @Test
    void testKsaRegion() {
        PaymobRegion ksa = PaymobRegion.KSA;
        assertEquals("https://ksa.paymob.com", ksa.getBaseUrl());
        assertEquals("https://ksa.paymob.com", ksa.getCheckoutUrl());
    }

    @Test
    void testUaeRegion() {
        PaymobRegion uae = PaymobRegion.UAE;
        assertEquals("https://uae.paymob.com", uae.getBaseUrl());
        assertEquals("https://uae.paymob.com", uae.getCheckoutUrl());
    }

    @Test
    void testOmanRegion() {
        PaymobRegion oman = PaymobRegion.OMAN;
        assertEquals("https://oman.paymob.com", oman.getBaseUrl());
        assertEquals("https://oman.paymob.com", oman.getCheckoutUrl());
    }

    @Test
    void testAllRegionsHaveUrls() {
        for (PaymobRegion region : PaymobRegion.values()) {
            assertNotNull(region.getBaseUrl());
            assertNotNull(region.getCheckoutUrl());
            assertTrue(region.getBaseUrl().startsWith("https://"));
            assertTrue(region.getCheckoutUrl().startsWith("https://"));
        }
    }

    @Test
    void testRegionUrlsMatch() {
        for (PaymobRegion region : PaymobRegion.values()) {
            assertEquals(region.getBaseUrl(), region.getCheckoutUrl());
        }
    }
}
