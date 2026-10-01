package no.fellesdata.tfi.tfc12.l140.boundary;

import no.fellesdata.tfi.tfc12.persistence.ContractKey;

/** F115ICC0 — contract collection / shadow copy tier. */
public interface ContractCollectionPort {

    void copyActualToWork(ContractKey key);
}
