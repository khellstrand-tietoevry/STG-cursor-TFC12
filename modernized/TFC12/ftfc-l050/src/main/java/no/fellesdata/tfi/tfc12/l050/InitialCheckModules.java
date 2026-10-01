package no.fellesdata.tfi.tfc12.l050;

import no.fellesdata.tfi.tfc12.l050.persistence.L050PersistencePorts;
import no.fellesdata.tfi.tfc12.persistence.ContractKey;
import no.fellesdata.tfi.tfc12.persistence.TradeFinancePersistence;

/** Default port wiring for smoke tests and local execution. */
public final class InitialCheckModules {

    private InitialCheckModules() {
    }

    public static InitialCheckModule permissiveDefaults() {
        TradeFinancePersistence persistence = TradeFinancePersistence.inMemory();
        persistence.seedStandardFixtures(new ContractKey("123456789012345678", "LC", "0001234"));
        return L050PersistencePorts.initialCheckModule(persistence);
    }
}
