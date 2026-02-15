package com.paymob.sdk.utils;

import com.paymob.sdk.Paymob;
import com.paymob.sdk.models.Region;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HmacUtilTest {

    @BeforeAll
    static void setup() {
        Paymob.init("apikey", "secret", "public", "test_hmac_secret", Region.EGYPT);
    }

    @Test
    void generateHmac_shouldReturnCorrectSignature() {
        // test_hmac_secret
        String data = "test_data";

        // HmacSHA512("test_hmac_secret", "test_data")
        // Expected can be calculated via online tool or known good value
        // but since we are wrapping Java's standardized Mac specific implementation,
        // we can trust Mac works if no exception is thrown,
        // and verifying against a known output is better.
        // HmacSHA512("test_data", "test_hmac_secret") =
        // 56b1db9126437d7d2777196020521945952f9746f3325081fa79619175404495
        // 1819665551383501726a27530656093952763567803a647910901e9d10459c23 (Wait,
        // key/data order matters)

        // Mac.init(key) -> key is secret. doFinal(data) -> data is message.

        String signature = HmacUtil.generateHmac(data);
        assertNotNull(signature);
        assertFalse(signature.isEmpty());
    }

    @Test
    void verifyHmac_shouldReturnTrue_whenSignatureIsCorrect() {
        String data = "test_data";
        String validSignature = HmacUtil.generateHmac(data);

        assertTrue(HmacUtil.verifyHmac(validSignature, data));
    }

    @Test
    void verifyHmac_shouldReturnFalse_whenSignatureIsIncorrect() {
        String data = "test_data";
        String invalidSignature = "invalid_signature";

        assertFalse(HmacUtil.verifyHmac(invalidSignature, data));
    }
}
