package com.paymob.sdk.models.common;

import com.paymob.sdk.models.enums.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmountTest {

    @Test
    void constructor_validParams_setsFields() {
        Amount amount = new Amount(1050, Currency.EGP);
        assertEquals(1050, amount.getValue());
        assertEquals(Currency.EGP, amount.getCurrency());
    }

    @Test
    void constructor_invalidValue_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> new Amount(0, Currency.EGP));
        assertThrows(IllegalArgumentException.class, () -> new Amount(-1, Currency.EGP));
    }

    @Test
    void toDecimalString_formatsCorrectly() {
        assertEquals("10.50", new Amount(1050, Currency.EGP).toDecimalString());
        assertEquals("1.00", new Amount(100, Currency.SAR).toDecimalString());
        assertEquals("0.99", new Amount(99, Currency.EGP).toDecimalString());
    }
}
