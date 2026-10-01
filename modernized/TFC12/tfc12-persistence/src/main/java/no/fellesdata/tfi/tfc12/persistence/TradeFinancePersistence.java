package no.fellesdata.tfi.tfc12.persistence;

/**
 * Bundles I-module repositories for K/L/L050 wiring.
 */
public final class TradeFinancePersistence {

    private final InMemoryTradeFinanceStore store;

    private TradeFinancePersistence(InMemoryTradeFinanceStore store) {
        this.store = store;
    }

    public static TradeFinancePersistence inMemory() {
        return new TradeFinancePersistence(new InMemoryTradeFinanceStore());
    }

    public InMemoryTradeFinanceStore store() {
        return store;
    }

    public MainContractRepository mainContracts() {
        return store;
    }

    public ContractAmountRepository contractAmounts() {
        return store;
    }

    public MainChargeRepository mainCharges() {
        return store;
    }

    public PartAmountRepository partAmounts() {
        return store;
    }

    public PartChargeRepository partCharges() {
        return store;
    }

    public SysCodeRepository sysCodes() {
        return store;
    }

    public SysCodeRelationRepository sysCodeRelations() {
        return store;
    }

    public void seedStandardFixtures(ContractKey key) {
        store.seedDefaults(key);
    }
}
