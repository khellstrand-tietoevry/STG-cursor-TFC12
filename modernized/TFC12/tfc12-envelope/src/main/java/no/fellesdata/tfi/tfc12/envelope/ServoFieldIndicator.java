package no.fellesdata.tfi.tfc12.envelope;

/**
 * Servo STG field indicator ({@code *-I} / {@code *-O} in RTFCE120).
 * {@code U} means the client supplied the field in this request; any other value means unchanged.
 */
public final class ServoFieldIndicator {

    public static final char SUPPLIED = 'U';

    private ServoFieldIndicator() {
    }

    public static boolean isSupplied(Character indicator) {
        return indicator != null && indicator == SUPPLIED;
    }

    public static boolean isUnchanged(Character indicator) {
        return !isSupplied(indicator);
    }
}
