package no.fellesdata.tfi.tfc12.persistence;

import java.util.Optional;

/** F115IPA0 — part amount status. */
public interface PartAmountRepository {

    Optional<String> selectStatus(ContractKey key, int partAmountSeqNo, String tableVersion);
}
