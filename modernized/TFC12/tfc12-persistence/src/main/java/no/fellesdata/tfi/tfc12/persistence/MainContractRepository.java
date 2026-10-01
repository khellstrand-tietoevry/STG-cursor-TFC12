package no.fellesdata.tfi.tfc12.persistence;

import java.util.Optional;

/** F115IMC0 — SELECT happy path. */
public interface MainContractRepository {

    Optional<MainContractRecord> selectActual(ContractKey key);

    Optional<MainContractRecord> selectWork(ContractKey key);

    void upsertActual(ContractKey key, MainContractRecord record);

    void upsertWork(ContractKey key, MainContractRecord record);
}
