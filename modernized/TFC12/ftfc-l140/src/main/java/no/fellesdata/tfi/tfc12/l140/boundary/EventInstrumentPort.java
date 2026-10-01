package no.fellesdata.tfi.tfc12.l140.boundary;

import no.fellesdata.tfi.tfc12.persistence.ContractKey;

/** F115IEI0 — event/instrument index shadow copy. */
public interface EventInstrumentPort {

    void replicateForShadow(ContractKey key, String tableSource, String tableDest);
}
