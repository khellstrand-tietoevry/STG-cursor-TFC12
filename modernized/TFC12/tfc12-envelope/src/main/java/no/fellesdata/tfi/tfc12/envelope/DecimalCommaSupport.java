package no.fellesdata.tfi.tfc12.envelope;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * European decimal comma (COBOL {@code Decimal-Point Is Comma} on FTFCH120 / FTFCK120 / FTFCL120).
 */
public final class DecimalCommaSupport {

    private static final DecimalFormat CHARGE_AMOUNT_FORMAT;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("nb-NO"));
        symbols.setDecimalSeparator(',');
        CHARGE_AMOUNT_FORMAT = new DecimalFormat("0.000", symbols);
        CHARGE_AMOUNT_FORMAT.setGroupingUsed(false);
    }

    private DecimalCommaSupport() {
    }

    public static char decimalSeparator() {
        return CHARGE_AMOUNT_FORMAT.getDecimalFormatSymbols().getDecimalSeparator();
    }

    public static String formatChargeAmount(BigDecimal amount) {
        return CHARGE_AMOUNT_FORMAT.format(amount);
    }
}
