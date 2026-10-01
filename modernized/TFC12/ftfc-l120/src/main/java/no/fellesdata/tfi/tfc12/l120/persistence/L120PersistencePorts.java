package no.fellesdata.tfi.tfc12.l120.persistence;

import no.fellesdata.tfi.tfc12.l050.persistence.L050PersistencePorts;
import no.fellesdata.tfi.tfc12.l120.FtfcL120Module;
import no.fellesdata.tfi.tfc12.l120.boundary.L140ShadowCreationAdapter;
import no.fellesdata.tfi.tfc12.l120.boundary.ShadowCreationPort;
import no.fellesdata.tfi.tfc12.l140.L140Modules;
import no.fellesdata.tfi.tfc12.l120.compare.ChargeCompareHelperPort;
import no.fellesdata.tfi.tfc12.l120.compare.RecordingChargeCompareHelperPort;
import no.fellesdata.tfi.tfc12.persistence.TradeFinancePersistence;

public final class L120PersistencePorts {

    private L120PersistencePorts() {
    }

    public static FtfcL120Module logicModule(
            TradeFinancePersistence persistence,
            ShadowCreationPort shadowCreationPort,
            ChargeCompareHelperPort compareHelperPort) {
        return new FtfcL120Module(
                L050PersistencePorts.mainContractPort(persistence),
                shadowCreationPort,
                compareHelperPort);
    }

    public static FtfcL120Module permissiveDefaults(TradeFinancePersistence persistence) {
        var shadowModule = L140Modules.fromPersistence(persistence);
        ShadowCreationPort shadowPort = new L140ShadowCreationAdapter(shadowModule);
        return logicModule(persistence, shadowPort, new RecordingChargeCompareHelperPort());
    }
}
