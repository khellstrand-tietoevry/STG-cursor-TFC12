package no.fellesdata.tfi.tfc12.l050;

import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.l050.boundary.ContractAmountPort;
import no.fellesdata.tfi.tfc12.l050.boundary.MainContractPort;
import no.fellesdata.tfi.tfc12.l050.boundary.MainContractSnapshot;
import no.fellesdata.tfi.tfc12.l050.boundary.PartAmountPort;
import no.fellesdata.tfi.tfc12.l050.boundary.SysCodeLookupPort;
import no.fellesdata.tfi.tfc12.l050.boundary.SysCodeRelationPort;

/**
 * F115L050 C000-Interface path (E000 update-timestamp block omitted per legacy comment — RULE-017).
 */
public class InitialCheckModule {

    private final MainContractPort mainContractPort;
    private final SysCodeRelationPort sysCodeRelationPort;
    private final SysCodeLookupPort sysCodeLookupPort;
    private final ContractAmountPort contractAmountPort;
    private final PartAmountPort partAmountPort;

    private boolean updateTimestampCheckInvoked;

    public InitialCheckModule(
            MainContractPort mainContractPort,
            SysCodeRelationPort sysCodeRelationPort,
            SysCodeLookupPort sysCodeLookupPort,
            ContractAmountPort contractAmountPort,
            PartAmountPort partAmountPort) {
        this.mainContractPort = mainContractPort;
        this.sysCodeRelationPort = sysCodeRelationPort;
        this.sysCodeLookupPort = sysCodeLookupPort;
        this.contractAmountPort = contractAmountPort;
        this.partAmountPort = partAmountPort;
    }

    public boolean wasUpdateTimestampCheckInvoked() {
        return updateTimestampCheckInvoked;
    }

    public void execute(InitialCheckRequest request, ServoErrorArea error) {
        error.setReturnOk();
        updateTimestampCheckInvoked = false;

        if (!validateFunctionAndMedium(request, error)) {
            return;
        }

        MainContractSnapshot contract = mainContractPort.read(request, "ACTUAL");
        if (contract.inProgress()) {
            contract = mainContractPort.read(request, "WORK");
        }

        String workflowStatus;
        if (request.getPartAmountSeqNo() == 0) {
            workflowStatus = contract.status();
        } else {
            String currency = contractAmountPort.readContractCurrency(request, "ACTUAL");
            workflowStatus = partAmountPort.readPartAmountStatus(request, "ACTUAL", currency);
        }

        // E000-Check-Upd-Timestamp intentionally not called (legacy lines 119–123 commented out).

        SysCodeRelationPort.Result relation =
                sysCodeRelationPort.validateStatusOperation(request, workflowStatus);
        if (relation == SysCodeRelationPort.Result.INVALID_STATUS_OPERATION) {
            error.setMessage("TF-SY-INVALID-STATUS-OPERTYP");
            error.setParagraph("F102");
            error.setReturnError();
            return;
        }

        String statusProperty = sysCodeLookupPort.readStatusProperty(request.getFinInstNo(), workflowStatus);

        if (shouldRunPropertyChecks(request)) {
            checkPermanentInProgress(request, contract, statusProperty, error);
        }
    }

    private boolean validateFunctionAndMedium(InitialCheckRequest request, ServoErrorArea error) {
        if (isBlank(request.getFunction()) || isBlank(request.getMedium())) {
            error.setMessage("TF-SY-FUNC-AND-MEDIUM");
            error.setParagraph("C102");
            error.setReturnAbend();
            return false;
        }
        return true;
    }

    private boolean shouldRunPropertyChecks(InitialCheckRequest request) {
        return !(request.functionThirdCharacter() == 'R' && "SHOW".equals(request.getOperationType()));
    }

    private void checkPermanentInProgress(
            InitialCheckRequest request,
            MainContractSnapshot contract,
            String statusProperty,
            ServoErrorArea error) {
        if (error.isReturnError() || error.isReturnAbend()) {
            return;
        }
        boolean permanent = statusProperty != null && statusProperty.contains("PERM");
        boolean exemptFunction = "TFD05".equals(request.getFunction());
        if (permanent && contract.inProgress() && !exemptFunction) {
            error.setMessage("TF-SY-MC-INPROG-Y");
            error.setParagraph("G001");
            error.setReturnError();
        }
    }

    /**
     * Retained for parity documentation; not invoked on the main path (RULE-017).
     */
    @SuppressWarnings("unused")
    private void performUpdateTimestampCheck(InitialCheckRequest request, MainContractSnapshot contract, ServoErrorArea error) {
        updateTimestampCheckInvoked = true;
        if (request.isCheckUpdateTimestamp()
                && request.functionThirdCharacter() != 'R'
                && contract.updateTimestamp() != null
                && !contract.updateTimestamp().equals(request.getLastUpdateTimestamp())) {
            error.setMessage("TF-SY-UPD-DATED-BY-VAR1");
            error.setParagraph("E001");
            error.setReturnError();
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
