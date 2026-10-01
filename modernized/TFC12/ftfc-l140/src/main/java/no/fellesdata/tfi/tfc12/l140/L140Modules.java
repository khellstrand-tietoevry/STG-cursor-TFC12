package no.fellesdata.tfi.tfc12.l140;

import no.fellesdata.tfi.tfc12.l140.boundary.ContractCollectionPort;
import no.fellesdata.tfi.tfc12.l140.boundary.EventInstrumentPort;
import no.fellesdata.tfi.tfc12.l140.boundary.RelatedPartyPort;
import no.fellesdata.tfi.tfc12.l140.boundary.ShadowTierListPort;
import no.fellesdata.tfi.tfc12.persistence.TradeFinancePersistence;

public final class L140Modules {

    private L140Modules() {
    }

    public static FtfcL140ShadowModule fromPersistence(TradeFinancePersistence persistence) {
        ContractCollectionPort noopCollection = (key) -> {};
        RelatedPartyPort noopRelated = (key, source, dest) -> {};
        EventInstrumentPort noopEvent = (key, source, dest) -> {};
        ShadowTierListPort noopTier = (key, source, dest) -> {};
        return new FtfcL140ShadowModule(
                persistence.mainContracts(), noopCollection, noopRelated, noopEvent, noopTier);
    }

    public static FtfcL140ShadowModule recording(TradeFinancePersistence persistence, RecordingShadowPorts ports) {
        return new FtfcL140ShadowModule(
                persistence.mainContracts(),
                ports::recordIcc0,
                ports::recordIrp0,
                ports::recordIei0,
                ports::recordL040);
    }
}
