package no.fellesdata.tfi.tfc12.persistence;

/** F115IPC0 — part charge CRUD (happy-path subset). */
public interface PartChargeRepository {

    void insertWork(ContractKey key, int partAmountSeqNo, String chargeType);

    int countWork(ContractKey key, int partAmountSeqNo);
}
