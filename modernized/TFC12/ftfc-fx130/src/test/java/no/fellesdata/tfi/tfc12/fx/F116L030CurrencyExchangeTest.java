package no.fellesdata.tfi.tfc12.fx;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Treasury persona: cross-currency charge base conversion (NOK → EUR).
 */
class F116L030CurrencyExchangeTest {

    private final F116L030CurrencyExchange exchange = new F116L030CurrencyExchange();

    @Test
    void crossCurrencyPersona_nokToEurUsesDecimalCommaFormatting() {
        var result = exchange.exchange(
                new CurrencyExchangeRequest("123456789012345678", "NOK", "EUR", new BigDecimal("1000.000")));

        assertTrue(result.ok());
        assertEquals(new BigDecimal("85.000"), result.toAmount());
        assertEquals("85,000", result.formattedAmount());
    }

    @Test
    void sameCurrencyIsIdentity() {
        var result = exchange.exchange(
                new CurrencyExchangeRequest("123456789012345678", "NOK", "NOK", new BigDecimal("12.500")));
        assertTrue(result.ok());
        assertEquals(new BigDecimal("12.500"), result.toAmount());
    }
}
