package no.fellesdata.tfi.tfc12.l050.boundary;

import no.fellesdata.tfi.tfc12.l050.InitialCheckRequest;

/** Port for F115ICA0 (C250). */
public interface ContractAmountPort {

    String readContractCurrency(InitialCheckRequest request, String tableVersion);
}
