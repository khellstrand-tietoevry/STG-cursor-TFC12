package no.fellesdata.tfi.tfc12.l120.boundary;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;

/** Port for F115L140 (G060-Create-Shadow). */
public interface ShadowCreationPort {

    void createShadow(StgHeader header, CreateChargeFunction function, ServoErrorArea error);
}
