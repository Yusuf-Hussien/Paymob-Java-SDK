package com.paymob.sdk.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymobRegionTest {

    @Test
    void testEgyptRegion() {
        PaymobRegion egypt = PaymobRegion.EGYPT;
        assertEquals("https://accept.paymob.com", egypt.getBaseUrl());
    }

    @Test
    void testKsaRegion() {
        PaymobRegion ksa = PaymobRegion.KSA;
        assertEquals("https://ksa.paymob.com", ksa.getBaseUrl());
    }

    @Test
    void testUaeRegion() {
        PaymobRegion uae = PaymobRegion.UAE;
        assertEquals("https://uae.paymob.com", uae.getBaseUrl());
    }

    @Test
    void testOmanRegion() {
        PaymobRegion oman = PaymobRegion.OMAN;
        assertEquals("https://oman.paymob.com", oman.getBaseUrl());
    }

    @Test
    void testAllRegionsHaveUrls() {
        for (PaymobRegion region : PaymobRegion.values()) {
            assertNotNull(region.getBaseUrl());
            assertTrue(region.getBaseUrl().startsWith("https://"));
            assertTrue(region.getBaseUrl().concat("/unifiedcheckout/").startsWith("https://"));
        }
    }
}
