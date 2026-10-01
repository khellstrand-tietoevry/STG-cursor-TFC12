package no.fellesdata.tfi.tfc12.k120;

import no.fellesdata.tfi.tfc12.l050.persistence.L050PersistencePorts;
import no.fellesdata.tfi.tfc12.persistence.ContractKey;
import no.fellesdata.tfi.tfc12.persistence.TradeFinancePersistence;

public final class K120Modules {

    private K120Modules() {
    }

    public static FtfcK120Module defaultModule(TradeFinancePersistence persistence) {
        CreateChargeK120InputControl inputControl =
                new CreateChargeK120InputControl(new CreateChargeK120UserDependencies(persistence.sysCodes()));
        CreateChargeK120InitialCheck initialCheck =
                new CreateChargeK120InitialCheck(L050PersistencePorts.initialCheckModule(persistence));
        return new FtfcK120Module(inputControl, initialCheck);
    }

    public static TradeFinancePersistence seededPersistence() {
        TradeFinancePersistence persistence = TradeFinancePersistence.inMemory();
        persistence.seedStandardFixtures(
                new ContractKey("123456789012345678", "LC", "0001234"));
        return persistence;
    }
}
