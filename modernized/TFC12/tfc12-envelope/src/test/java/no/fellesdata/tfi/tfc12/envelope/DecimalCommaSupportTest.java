package no.fellesdata.tfi.tfc12.envelope;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DecimalCommaSupportTest {

    @Test
    void rule016_formatsChargeAmountWithDecimalComma() {
        assertEquals(',', DecimalCommaSupport.decimalSeparator());
        assertEquals("1,500", DecimalCommaSupport.formatChargeAmount(new BigDecimal("1.5")));
    }
}
