package no.fellesdata.tfi.tfc12.l140.boundary;

import no.fellesdata.tfi.tfc12.persistence.ContractKey;

/** F115IRP0 — related party rows for shadow propagation. */
public interface RelatedPartyPort {

    void replicateForShadow(ContractKey key, String tableSource, String tableDest);
}
