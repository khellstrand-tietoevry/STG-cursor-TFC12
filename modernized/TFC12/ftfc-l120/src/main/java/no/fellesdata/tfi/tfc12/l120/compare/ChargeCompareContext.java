package no.fellesdata.tfi.tfc12.l120.compare;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;

public record ChargeCompareContext(StgHeader header, CreateChargeFunction function) {
}
