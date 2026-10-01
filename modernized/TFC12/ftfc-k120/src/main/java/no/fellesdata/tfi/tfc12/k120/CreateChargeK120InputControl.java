package no.fellesdata.tfi.tfc12.k120;

import no.fellesdata.tfi.tfc12.envelope.ChargeTableRow;
import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.ServoFieldIndicator;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;

/**
 * Generated C100/C500 paragraphs from FTFCK120 (Phase 1 scope: RULE-004–007).
 */
public class CreateChargeK120InputControl {

    private static final char FIELD_FAILED = 'F';

    private final CreateChargeK120UserDependencies userDependencies;

    public CreateChargeK120InputControl() {
        this(null);
    }

    public CreateChargeK120InputControl(CreateChargeK120UserDependencies userDependencies) {
        this.userDependencies = userDependencies;
    }

    public void runInputControl(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        try {
            mandatoryFields(header, data, error);
            chargeDependencies(header, data, error);
            if (userDependencies != null) {
                userDependencies.runUserDependencies(header, data, error);
            }
        } catch (K120ValidationException ex) {
            // AE already applied
        }
    }

    private void mandatoryFields(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        if (ServoFieldIndicator.isUnchanged(data.getFinInstNoIndicator())) {
            failMandatory(header, error, data::setFinInstNoOutput, "Fin-Inst-No");
        }
        if (ServoFieldIndicator.isUnchanged(data.getContractTypeIndicator())) {
            failMandatory(header, error, data::setContractTypeOutput, "Kontrakt-Type");
        }
        if (ServoFieldIndicator.isUnchanged(data.getContractNoIndicator())) {
            failMandatory(header, error, data::setContractNoOutput, "Kontrakt-Nr");
        }
        if (ServoFieldIndicator.isUnchanged(data.getLastSavedDateIndicator())) {
            failMandatory(header, error, data::setLastSavedDateOutput, "Sist-Lagret-Dato");
        }
        if (ServoFieldIndicator.isUnchanged(data.getOperationTypeIndicator())) {
            failMandatory(header, error, data::setOperationTypeOutput, "Operasjons-Type");
        }
    }

    private void failMandatory(
            StgHeader header,
            ServoErrorArea error,
            java.util.function.Consumer<Character> outputSetter,
            String fieldLabel) {
        outputSetter.accept(FIELD_FAILED);
        header.setMessageKey(0, "FELT1");
        header.setMessageData(0, fieldLabel);
        header.setMessageCode("GL-ANGI-FELT&");
        throwValidationError(header, error);
    }

    private void chargeDependencies(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        int typeLimit = data.getChargeTypeActiveCount();
        for (int tally = 0; tally < typeLimit; tally++) {
            ChargeTableRow row = data.getChargeRow(tally);
            if (ServoFieldIndicator.isSupplied(row.getChargeTypeIndicator())
                    && ServoFieldIndicator.isUnchanged(row.getSelectedIndicator())) {
                fieldPairError(header, error, row, "Valgt");
            }
            if (ServoFieldIndicator.isSupplied(row.getChargeTypeIndicator())
                    && ServoFieldIndicator.isUnchanged(row.getChargeAmountIndicator())) {
                fieldPairError(header, error, row, "Beløp");
            }
            if (ServoFieldIndicator.isSupplied(row.getChargeTypeIndicator())
                    && ServoFieldIndicator.isUnchanged(row.getPayingPartyIndicator())) {
                fieldPairError(header, error, row, "Betalende-Part");
            }
            if (ServoFieldIndicator.isSupplied(row.getChargeTypeIndicator())
                    && ServoFieldIndicator.isUnchanged(row.getPercentCommissionIndicator())) {
                fieldPairError(header, error, row, "Prosent-Provisjon");
            }
        }

        int amountLimit = data.getChargeAmountActiveCount();
        for (int tally = 0; tally < amountLimit; tally++) {
            ChargeTableRow row = data.getChargeRow(tally);
            if (ServoFieldIndicator.isUnchanged(row.getChargeAmountIndicator())
                    && ServoFieldIndicator.isUnchanged(row.getPercentCommissionIndicator())) {
                row.setChargeAmountOutput(FIELD_FAILED);
                header.setMessageKey(0, "FELT1");
                header.setMessageData(0, "Beløp");
                header.setMessageKey(1, "FELT2");
                header.setMessageData(1, "Prosent-Provisjon");
                header.setMessageCode("TF-SY-FIELD1-NOT-FIELD2");
                throwValidationError(header, error);
            }
        }

        int percentLimit = data.getPercentCommissionActiveCount();
        for (int tally = 0; tally < percentLimit; tally++) {
            ChargeTableRow row = data.getChargeRow(tally);
            if (ServoFieldIndicator.isUnchanged(row.getPercentCommissionIndicator())
                    && ServoFieldIndicator.isUnchanged(row.getChargeAmountIndicator())) {
                row.setPercentCommissionOutput(FIELD_FAILED);
                header.setMessageKey(0, "FELT1");
                header.setMessageData(0, "Prosent-Provisjon");
                header.setMessageKey(1, "FELT2");
                header.setMessageData(1, "Beløp");
                header.setMessageCode("TF-SY-FIELD1-NOT-FIELD2");
                throwValidationError(header, error);
            }
        }
    }

    private void fieldPairError(StgHeader header, ServoErrorArea error, ChargeTableRow row, String field2Label) {
        row.setChargeTypeOutput(FIELD_FAILED);
        header.setMessageKey(0, "FELT1");
        header.setMessageData(0, "Omkostnings-type");
        header.setMessageKey(1, "FELT2");
        header.setMessageData(1, field2Label);
        header.setMessageCode("TF-SY-FIELD2-AND-FIELD1");
        throwValidationError(header, error);
    }

    private void throwValidationError(StgHeader header, ServoErrorArea error) {
        error.setReturnError();
        if (!"FM".equals(header.getMessageCode())) {
            header.setMessageText("FTFCK120");
        }
        throw new K120ValidationException();
    }
}
