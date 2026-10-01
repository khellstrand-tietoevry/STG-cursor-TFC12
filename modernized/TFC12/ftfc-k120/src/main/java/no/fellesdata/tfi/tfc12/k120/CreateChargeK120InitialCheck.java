package no.fellesdata.tfi.tfc12.k120;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.ServoFieldIndicator;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.l050.InitialCheckModule;
import no.fellesdata.tfi.tfc12.l050.InitialCheckRequest;

/**
 * FTFCK120 E100-Initial-Checks — CALL F115L050 and map return to E101/E102 + FM.
 */
public class CreateChargeK120InitialCheck {

    private final InitialCheckModule initialCheckModule;

    public CreateChargeK120InitialCheck(InitialCheckModule initialCheckModule) {
        this.initialCheckModule = initialCheckModule;
    }

    public void runInitialChecks(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        InitialCheckRequest request = mapRequest(header, data);
        initialCheckModule.execute(request, error);

        if (error.isReturnAbend()) {
            header.setMessageCode("FM");
            error.setParagraph("E101");
            return;
        }
        if (error.isReturnError()) {
            header.setMessageCode("FM");
            error.setParagraph("E102");
        }
    }

    InitialCheckModule initialCheckModule() {
        return initialCheckModule;
    }

    private static InitialCheckRequest mapRequest(StgHeader header, CreateChargeFunction data) {
        InitialCheckRequest request = new InitialCheckRequest();
        request.setFunction(header.getFunction());
        request.setMedium(header.getMedium());
        request.setFinInstNo(data.getFinInstNo());
        request.setContractType(data.getContractType());
        request.setContractNo(data.getContractNo());
        if (ServoFieldIndicator.isSupplied(data.getPartAmountNoIndicator())) {
            request.setPartAmountSeqNo(data.getPartAmountNo());
        }
        request.setOperationType(data.getOperationType());
        request.setLastUpdateTimestamp(data.getLastSavedDate());
        return request;
    }
}
