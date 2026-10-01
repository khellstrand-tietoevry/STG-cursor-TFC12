package no.fellesdata.tfi.tfc12.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryTradeFinanceStoreTest {

    @Test
    void happyPathCrudForImc0Ica0Ich0Ipa0Ipc0() {
        InMemoryTradeFinanceStore store = new InMemoryTradeFinanceStore();
        ContractKey key = new ContractKey("123456789012345678", "LC", "0001234");
        store.seedDefaults(key);

        assertTrue(store.selectActual(key).isPresent());
        assertEquals("NOK", store.selectCurrency(key, "ACTUAL").orElseThrow());

        store.insertWork(key, "FEE", "CUSTM");
        assertEquals(1, store.countWork(key));

        store.putPartAmountStatus(key, 2, "ACTIVE");
        assertEquals("ACTIVE", store.selectStatus(key, 2, "ACTUAL").orElseThrow());

        store.insertWork(key, 2, "FEE");
        assertEquals(1, store.countWork(key, 2));

        assertTrue(store.selectContractType(key.finInstNo(), key.contractType()).isPresent());
        assertTrue(store.isValidOperationStatus(key.finInstNo(), "CREATE", "*"));
    }
}
