package no.fellesdata.tfi.tfc12.l140;

import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.l140.boundary.ContractCollectionPort;
import no.fellesdata.tfi.tfc12.l140.boundary.EventInstrumentPort;
import no.fellesdata.tfi.tfc12.l140.boundary.RelatedPartyPort;
import no.fellesdata.tfi.tfc12.l140.boundary.ShadowTierListPort;
import no.fellesdata.tfi.tfc12.persistence.ContractKey;
import no.fellesdata.tfi.tfc12.persistence.MainContractRecord;
import no.fellesdata.tfi.tfc12.persistence.MainContractRepository;

/**
 * F115L140 create-shadow slice (C100 + D000 ACTUAL→WORK) with IMC0 read and external ports.
 */
public class FtfcL140ShadowModule {

    private static final String TABLE_SOURCE = "ACTUAL";
    private static final String TABLE_DEST = "WORK";

    private final MainContractRepository mainContractRepository;
    private final ContractCollectionPort contractCollectionPort;
    private final RelatedPartyPort relatedPartyPort;
    private final EventInstrumentPort eventInstrumentPort;
    private final ShadowTierListPort shadowTierListPort;

    public FtfcL140ShadowModule(
            MainContractRepository mainContractRepository,
            ContractCollectionPort contractCollectionPort,
            RelatedPartyPort relatedPartyPort,
            EventInstrumentPort eventInstrumentPort,
            ShadowTierListPort shadowTierListPort) {
        this.mainContractRepository = mainContractRepository;
        this.contractCollectionPort = contractCollectionPort;
        this.relatedPartyPort = relatedPartyPort;
        this.eventInstrumentPort = eventInstrumentPort;
        this.shadowTierListPort = shadowTierListPort;
    }

    public void createShadow(ShadowCreateRequest request, ServoErrorArea error) {
        error.setReturnOk();
        if (!request.createShadow()) {
            return;
        }
        if (isBlank(request.function()) || isBlank(request.medium())) {
            error.setParagraph("C102");
            error.setMessage("TF-SY-FUNC-AND-MEDIUM");
            error.setReturnAbend();
            return;
        }

        ContractKey key = request.contractKey();
        MainContractRecord mainContract = mainContractRepository
                .selectActual(key)
                .orElse(MainContractRecord.active("ACTIVE", false));

        if (mainContract.inProgress()) {
            error.setParagraph("C104");
            error.setMessage("TF-SY-SHADOW-AE");
            error.setReturnError();
            return;
        }

        contractCollectionPort.copyActualToWork(key);
        relatedPartyPort.replicateForShadow(key, TABLE_SOURCE, TABLE_DEST);
        eventInstrumentPort.replicateForShadow(key, TABLE_SOURCE, TABLE_DEST);
        shadowTierListPort.synchronizeTiers(key, TABLE_SOURCE, TABLE_DEST);

        MainContractRecord workCopy = new MainContractRecord(
                mainContract.status(), true, mainContract.updateTimestamp(), mainContract.updUserId());
        mainContractRepository.upsertWork(key, workCopy);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
