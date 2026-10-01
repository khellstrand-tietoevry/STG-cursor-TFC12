package no.fellesdata.tfi.tfc12.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryTradeFinanceStore
        implements MainContractRepository,
                ContractAmountRepository,
                MainChargeRepository,
                PartAmountRepository,
                PartChargeRepository,
                SysCodeRepository,
                SysCodeRelationRepository {

    private final Map<ContractKey, MainContractRecord> actualContracts = new ConcurrentHashMap<>();
    private final Map<ContractKey, MainContractRecord> workContracts = new ConcurrentHashMap<>();
    private final Map<ContractKey, String> contractCurrencies = new ConcurrentHashMap<>();
    private final Map<String, String> statusProperties = new ConcurrentHashMap<>();
    private final Map<String, ContractTypeSysCodeRecord> contractTypes = new ConcurrentHashMap<>();
    private final Map<ContractKey, Integer> mainChargeCounts = new ConcurrentHashMap<>();
    private final Map<String, Integer> partChargeCounts = new ConcurrentHashMap<>();
    private final Map<String, String> partAmountStatuses = new ConcurrentHashMap<>();
    private boolean validRelations = true;

    @Override
    public Optional<MainContractRecord> selectActual(ContractKey key) {
        return Optional.ofNullable(actualContracts.get(key));
    }

    @Override
    public Optional<MainContractRecord> selectWork(ContractKey key) {
        return Optional.ofNullable(workContracts.get(key));
    }

    @Override
    public void upsertActual(ContractKey key, MainContractRecord record) {
        actualContracts.put(key, record);
    }

    @Override
    public void upsertWork(ContractKey key, MainContractRecord record) {
        workContracts.put(key, record);
    }

    @Override
    public Optional<String> selectCurrency(ContractKey key, String tableVersion) {
        return Optional.ofNullable(contractCurrencies.get(key));
    }

    @Override
    public void insertWork(ContractKey key, String chargeType, String payingParty) {
        mainChargeCounts.merge(key, 1, Integer::sum);
    }

    @Override
    public int countWork(ContractKey key) {
        return mainChargeCounts.getOrDefault(key, 0);
    }

    @Override
    public Optional<String> selectStatus(ContractKey key, int partAmountSeqNo, String tableVersion) {
        return Optional.ofNullable(partAmountStatuses.get(partKey(key, partAmountSeqNo)));
    }

    @Override
    public void insertWork(ContractKey key, int partAmountSeqNo, String chargeType) {
        partChargeCounts.merge(partKey(key, partAmountSeqNo), 1, Integer::sum);
    }

    @Override
    public int countWork(ContractKey key, int partAmountSeqNo) {
        return partChargeCounts.getOrDefault(partKey(key, partAmountSeqNo), 0);
    }

    @Override
    public Optional<String> selectStatusProperty(String finInstNo, String statusCode) {
        return Optional.ofNullable(statusProperties.get(finInstNo + "|STATUS|" + statusCode));
    }

    @Override
    public Optional<ContractTypeSysCodeRecord> selectContractType(String finInstNo, String contractTypeCode) {
        return Optional.ofNullable(contractTypes.get(finInstNo + "|CONTRACT-TYPE|" + contractTypeCode));
    }

    @Override
    public boolean isValidStatusOperation(
            String finInstNo, String fromStatus, String operationType, String toStatusWildcard) {
        return validRelations;
    }

    @Override
    public boolean isValidOperationStatus(String finInstNo, String operationType, String toStatusWildcard) {
        return validRelations;
    }

    public void seedDefaults(ContractKey key) {
        upsertActual(key, MainContractRecord.active("ACTIVE", false));
        contractCurrencies.put(key, "NOK");
        statusProperties.put(key.finInstNo() + "|STATUS|ACTIVE", "WORKFLOW");
        contractTypes.put(
                key.finInstNo() + "|CONTRACT-TYPE|" + key.contractType(),
                new ContractTypeSysCodeRecord("LC", "IMPORT", "WORKFLOW"));
    }

    public void setValidRelations(boolean validRelations) {
        this.validRelations = validRelations;
    }

    public void putContractType(String finInstNo, String contractTypeCode, ContractTypeSysCodeRecord record) {
        contractTypes.put(finInstNo + "|CONTRACT-TYPE|" + contractTypeCode, record);
    }

    public void putPartAmountStatus(ContractKey key, int seq, String status) {
        partAmountStatuses.put(partKey(key, seq), status);
    }

    private static String partKey(ContractKey key, int seq) {
        return key.finInstNo() + "|" + key.contractType() + "|" + key.contractNo() + "|PA|" + seq;
    }
}
