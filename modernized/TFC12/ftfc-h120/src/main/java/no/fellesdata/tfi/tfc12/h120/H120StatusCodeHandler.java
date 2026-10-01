package no.fellesdata.tfi.tfc12.h120;

import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.h120.boundary.ServoErrorMessagePort;

/**
 * FTFCH120 S000-Status-Code: AE/AS, AW warning with K-message restore after L OK.
 */
public class H120StatusCodeHandler {

    public enum FlowDecision {
        CONTINUE,
        STOP
    }

    private String savedStatusCode;
    private String savedMessageCode;
    private String savedMessageText;

    public void reset() {
        savedStatusCode = null;
        savedMessageCode = null;
        savedMessageText = null;
    }

    public FlowDecision afterKModule(StgHeader header, ServoErrorArea error, ServoErrorMessagePort errorMessagePort) {
        return mapStatus(header, error, errorMessagePort, true);
    }

    public void afterLModule(StgHeader header, ServoErrorArea error, ServoErrorMessagePort errorMessagePort) {
        mapStatus(header, error, errorMessagePort, false);
    }

    private FlowDecision mapStatus(
            StgHeader header, ServoErrorArea error, ServoErrorMessagePort errorMessagePort, boolean fromK) {
        if (error.isReturnAbend()) {
            header.setStatusCode("AS");
            errorMessagePort.publish(header, true);
            return fromK ? FlowDecision.STOP : FlowDecision.CONTINUE;
        }
        if (error.isReturnError()) {
            header.setStatusCode("AE");
            errorMessagePort.publish(header, true);
            return fromK ? FlowDecision.STOP : FlowDecision.CONTINUE;
        }
        if (fromK && error.isReturnWarningGoback()) {
            header.setStatusCode("AW");
            errorMessagePort.publish(header, true);
            return FlowDecision.STOP;
        }
        if (fromK && error.isReturnWarning()) {
            errorMessagePort.publish(header, true);
            savedStatusCode = "AW";
            savedMessageCode = header.getMessageCode();
            savedMessageText = header.getMessageText();
            header.setStatusCode("AW");
            return FlowDecision.CONTINUE;
        }
        if (fromK) {
            return FlowDecision.CONTINUE;
        }
        if (error.isReturnOk() && savedStatusCode != null) {
            header.setStatusCode(savedStatusCode);
            header.setMessageCode(savedMessageCode);
            header.setMessageText(savedMessageText);
            return FlowDecision.CONTINUE;
        }
        if (error.isReturnOk() && header.getStatusCode() == null) {
            header.setStatusCode("OK");
        }
        return FlowDecision.CONTINUE;
    }
}
