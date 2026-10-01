package no.fellesdata.tfi.tfc12.h120;

import no.fellesdata.tfi.tfc12.k120.FtfcK120Module;
import no.fellesdata.tfi.tfc12.k120.K120Modules;
import no.fellesdata.tfi.tfc12.l120.persistence.L120PersistencePorts;
import no.fellesdata.tfi.tfc12.persistence.TradeFinancePersistence;

public final class H120Modules {

    private H120Modules() {
    }

    public static FtfcH120Orchestrator defaultStack() {
        return defaultStack(K120Modules.seededPersistence(), H120BoundaryPorts.noop());
    }

    public static FtfcH120Orchestrator defaultStack(
            TradeFinancePersistence persistence, H120BoundaryPorts boundaries) {
        FtfcK120Module kModule = K120Modules.defaultModule(persistence);
        var lModule = L120PersistencePorts.permissiveDefaults(persistence);
        return new FtfcH120Orchestrator(kModule, lModule, boundaries);
    }
}
