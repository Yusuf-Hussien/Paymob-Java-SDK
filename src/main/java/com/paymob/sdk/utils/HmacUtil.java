package com.paymob.sdk.utils;

import com.paymob.sdk.Paymob;
import com.paymob.sdk.exceptions.PaymobException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class HmacUtil {

    private static final String HMAC_ALGORITHM = "HmacSHA512";

    /**
     * Verify the HMAC signature provided by Paymob.
     * 
     * @param providedHmac The HMAC signature received in the query param 'hmac'.
     * @param data         The concatenated data string constructed from the
     *                     callback parameters.
     * @return true if the signature is valid, false otherwise.
     */
    public static boolean verifyHmac(String providedHmac, String data) {
        if (providedHmac == null || data == null) {
            return false;
        }
        String generatedHmac = generateHmac(data);
        return generatedHmac.equalsIgnoreCase(providedHmac);
    }

    /**
     * Generate HMAC SHA512 signature for the given data using the configured HMAC
     * Secret.
     * 
     * @param data The data to sign.
     * @return The hex-encoded HMAC signature.
     */
    public static String generateHmac(String data) {
        String secret = Paymob.getHmacSecret();
        if (secret == null) {
            throw new PaymobException("HMAC Secret is not initialized. Call Paymob.init(..., hmacSecret, ...)");
        }

        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), HMAC_ALGORITHM);
            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            return HexFormat.of().formatHex(rawHmac);

        } catch (NoSuchAlgorithmException e) {
            throw new PaymobException("Failed to set HMAC Algorithm: " + HMAC_ALGORITHM, e);
        } catch (InvalidKeyException e) {
            throw new PaymobException("Failed to set the HMAC Secret Key", e);
        }
    }
}
