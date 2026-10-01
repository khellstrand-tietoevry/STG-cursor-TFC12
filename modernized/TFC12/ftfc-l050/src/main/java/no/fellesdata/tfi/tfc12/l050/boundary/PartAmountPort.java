package no.fellesdata.tfi.tfc12.l050.boundary;

import no.fellesdata.tfi.tfc12.l050.InitialCheckRequest;

/** Port for F115IPA0 (C300). */
public interface PartAmountPort {

    String readPartAmountStatus(InitialCheckRequest request, String tableVersion, String contractCurrency);
}
