package no.fellesdata.tfi.tfc12.l120.compare;

/** Legacy compare/timestamp helpers invoked from FTFCL120. */
public enum ChargeCompareHelper {
    F115L080("D200-Move-Charges"),
    F115L070("F100-Modify-Input-Charges"),
    F115L060("H100-Call-LMOD-Comp-Charge"),
    F115L030("H900-Upd-Timestamp");

    private final String legacyParagraph;

    ChargeCompareHelper(String legacyParagraph) {
        this.legacyParagraph = legacyParagraph;
    }

    public String legacyParagraph() {
        return legacyParagraph;
    }
}
