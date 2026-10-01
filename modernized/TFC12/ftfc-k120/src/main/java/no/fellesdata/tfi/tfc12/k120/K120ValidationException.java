package no.fellesdata.tfi.tfc12.k120;

/**
 * Mirrors COBOL {@code Z700-Error-AE} early {@code Goback} after validation failure.
 */
public class K120ValidationException extends RuntimeException {

    public K120ValidationException() {
        super("K-module validation failed (AE)");
    }
}
