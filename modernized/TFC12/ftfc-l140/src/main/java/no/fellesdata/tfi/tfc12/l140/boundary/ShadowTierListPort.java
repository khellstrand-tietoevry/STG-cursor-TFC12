package no.fellesdata.tfi.tfc12.l140.boundary;

import no.fellesdata.tfi.tfc12.persistence.ContractKey;

/** F115L040 — shadow tier list maintenance during create-shadow. */
public interface ShadowTierListPort {

    void synchronizeTiers(ContractKey key, String tableSource, String tableDest);
}
