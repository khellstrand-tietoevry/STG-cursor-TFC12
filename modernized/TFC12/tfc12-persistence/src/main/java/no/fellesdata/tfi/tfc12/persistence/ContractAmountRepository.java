package no.fellesdata.tfi.tfc12.persistence;

import java.util.Optional;

/** F115ICA0 — contract amount / currency. */
public interface ContractAmountRepository {

    Optional<String> selectCurrency(ContractKey key, String tableVersion);
}
