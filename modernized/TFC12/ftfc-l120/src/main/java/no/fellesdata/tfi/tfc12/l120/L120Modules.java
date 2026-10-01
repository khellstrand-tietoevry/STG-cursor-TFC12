package no.fellesdata.tfi.tfc12.l120;

import no.fellesdata.tfi.tfc12.l120.persistence.L120PersistencePorts;
import no.fellesdata.tfi.tfc12.persistence.ContractKey;
import no.fellesdata.tfi.tfc12.persistence.TradeFinancePersistence;

public final class L120Modules {

    private L120Modules() {
    }

    public static FtfcL120Module permissiveDefaults() {
        TradeFinancePersistence persistence = TradeFinancePersistence.inMemory();
        persistence.seedStandardFixtures(new ContractKey("123456789012345678", "LC", "0001234"));
        return L120PersistencePorts.permissiveDefaults(persistence);
    }
}
