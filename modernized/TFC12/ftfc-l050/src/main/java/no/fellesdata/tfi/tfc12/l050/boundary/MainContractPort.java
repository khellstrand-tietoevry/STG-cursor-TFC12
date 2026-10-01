package no.fellesdata.tfi.tfc12.l050.boundary;

import no.fellesdata.tfi.tfc12.l050.InitialCheckRequest;

/** Port for F115IMC0 main-contract read (C200). */
public interface MainContractPort {

    MainContractSnapshot read(InitialCheckRequest request, String tableVersion);
}
