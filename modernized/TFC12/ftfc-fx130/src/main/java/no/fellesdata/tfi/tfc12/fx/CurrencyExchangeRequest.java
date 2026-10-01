package no.fellesdata.tfi.tfc12.fx;

import java.math.BigDecimal;

public record CurrencyExchangeRequest(
        String finInstNo, String fromCurrency, String toCurrency, BigDecimal fromAmount) {
}
