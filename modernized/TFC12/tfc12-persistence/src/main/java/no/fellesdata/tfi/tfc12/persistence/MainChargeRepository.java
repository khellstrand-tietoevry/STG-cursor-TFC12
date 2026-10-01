package no.fellesdata.tfi.tfc12.persistence;

/** F115ICH0 — main charge CRUD (happy-path subset). */
public interface MainChargeRepository {

    void insertWork(ContractKey key, String chargeType, String payingParty);

    int countWork(ContractKey key);
}
