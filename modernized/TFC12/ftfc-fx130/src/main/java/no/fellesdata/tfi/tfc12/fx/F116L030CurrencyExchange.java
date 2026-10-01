package no.fellesdata.tfi.tfc12.fx;

import no.fellesdata.tfi.tfc12.envelope.DecimalCommaSupport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * F116L030 happy-path subset: fixed local rates for cross-currency charge-base conversion.
 */
public class F116L030CurrencyExchange {

    private static final Map<String, BigDecimal> NOK_CROSS_RATE = Map.of(
            "EUR", new BigDecimal("0.085"),
            "USD", new BigDecimal("0.092"),
            "NOK", BigDecimal.ONE);

    public ExchangeResult exchange(CurrencyExchangeRequest request) {
        if (request.fromAmount() == null) {
            return ExchangeResult.error("TF-SY-INVALID-AMOUNT");
        }
        BigDecimal rate = rate(request.fromCurrency(), request.toCurrency());
        if (rate == null) {
            return ExchangeResult.error("TF-SY-INVALID-CURRENCY");
        }
        BigDecimal toAmount = request.fromAmount().multiply(rate).setScale(3, RoundingMode.HALF_UP);
        String formatted = DecimalCommaSupport.formatChargeAmount(toAmount);
        return ExchangeResult.ok(toAmount, formatted);
    }

    private static BigDecimal rate(String from, String to) {
        if (from == null || to == null) {
            return null;
        }
        if (from.equals(to)) {
            return BigDecimal.ONE;
        }
        if ("NOK".equals(from)) {
            return NOK_CROSS_RATE.get(to);
        }
        if ("NOK".equals(to)) {
            BigDecimal nokPerFrom = NOK_CROSS_RATE.get(from);
            return nokPerFrom == null ? null : BigDecimal.ONE.divide(nokPerFrom, 6, RoundingMode.HALF_UP);
        }
        return null;
    }

    public record ExchangeResult(boolean ok, BigDecimal toAmount, String formattedAmount, String messageCode) {
        static ExchangeResult ok(BigDecimal amount, String formatted) {
            return new ExchangeResult(true, amount, formatted, null);
        }

        static ExchangeResult error(String code) {
            return new ExchangeResult(false, null, null, code);
        }
    }
}
