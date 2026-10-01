package no.fellesdata.tfi.tfc12.envelope;

import java.util.ArrayList;
import java.util.List;

/**
 * RTFCE120 CreateCharge function payload (TFC12/01 subset for Phase 1).
 */
public class CreateChargeFunction {

    public static final int MAX_CHARGE_ROWS = 50;

    private Character finInstNoIndicator;
    private Character finInstNoOutput;
    private String finInstNo;

    private Character contractTypeIndicator;
    private Character contractTypeOutput;
    private String contractType;

    private Character contractNoIndicator;
    private Character contractNoOutput;
    private String contractNo;

    private Character lastSavedDateIndicator;
    private Character lastSavedDateOutput;
    private String lastSavedDate;

    private Character operationTypeIndicator;
    private Character operationTypeOutput;
    private String operationType;

    private Character partAmountNoIndicator;
    private Character partAmountNoOutput;
    private int partAmountNo;

    private int chargeTypeActiveCount;
    private int chargeAmountActiveCount;
    private int percentCommissionActiveCount;
    private final List<ChargeTableRow> chargeRows = new ArrayList<>();

    public CreateChargeFunction() {
        for (int i = 0; i < MAX_CHARGE_ROWS; i++) {
            chargeRows.add(new ChargeTableRow());
        }
        chargeTypeActiveCount = 0;
        chargeAmountActiveCount = 0;
        percentCommissionActiveCount = 0;
    }

    public Character getFinInstNoIndicator() {
        return finInstNoIndicator;
    }

    public void setFinInstNoIndicator(Character finInstNoIndicator) {
        this.finInstNoIndicator = finInstNoIndicator;
    }

    public Character getFinInstNoOutput() {
        return finInstNoOutput;
    }

    public void setFinInstNoOutput(Character finInstNoOutput) {
        this.finInstNoOutput = finInstNoOutput;
    }

    public String getFinInstNo() {
        return finInstNo;
    }

    public void setFinInstNo(String finInstNo) {
        this.finInstNo = finInstNo;
    }

    public Character getContractTypeIndicator() {
        return contractTypeIndicator;
    }

    public void setContractTypeIndicator(Character contractTypeIndicator) {
        this.contractTypeIndicator = contractTypeIndicator;
    }

    public Character getContractTypeOutput() {
        return contractTypeOutput;
    }

    public void setContractTypeOutput(Character contractTypeOutput) {
        this.contractTypeOutput = contractTypeOutput;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Character getContractNoIndicator() {
        return contractNoIndicator;
    }

    public void setContractNoIndicator(Character contractNoIndicator) {
        this.contractNoIndicator = contractNoIndicator;
    }

    public Character getContractNoOutput() {
        return contractNoOutput;
    }

    public void setContractNoOutput(Character contractNoOutput) {
        this.contractNoOutput = contractNoOutput;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Character getLastSavedDateIndicator() {
        return lastSavedDateIndicator;
    }

    public void setLastSavedDateIndicator(Character lastSavedDateIndicator) {
        this.lastSavedDateIndicator = lastSavedDateIndicator;
    }

    public Character getLastSavedDateOutput() {
        return lastSavedDateOutput;
    }

    public void setLastSavedDateOutput(Character lastSavedDateOutput) {
        this.lastSavedDateOutput = lastSavedDateOutput;
    }

    public String getLastSavedDate() {
        return lastSavedDate;
    }

    public void setLastSavedDate(String lastSavedDate) {
        this.lastSavedDate = lastSavedDate;
    }

    public Character getOperationTypeIndicator() {
        return operationTypeIndicator;
    }

    public void setOperationTypeIndicator(Character operationTypeIndicator) {
        this.operationTypeIndicator = operationTypeIndicator;
    }

    public Character getOperationTypeOutput() {
        return operationTypeOutput;
    }

    public void setOperationTypeOutput(Character operationTypeOutput) {
        this.operationTypeOutput = operationTypeOutput;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Character getPartAmountNoIndicator() {
        return partAmountNoIndicator;
    }

    public void setPartAmountNoIndicator(Character partAmountNoIndicator) {
        this.partAmountNoIndicator = partAmountNoIndicator;
    }

    public Character getPartAmountNoOutput() {
        return partAmountNoOutput;
    }

    public void setPartAmountNoOutput(Character partAmountNoOutput) {
        this.partAmountNoOutput = partAmountNoOutput;
    }

    public int getPartAmountNo() {
        return partAmountNo;
    }

    public void setPartAmountNo(int partAmountNo) {
        this.partAmountNo = partAmountNo;
    }

    public int getChargeTypeActiveCount() {
        return chargeTypeActiveCount;
    }

    public void setChargeTypeActiveCount(int chargeTypeActiveCount) {
        this.chargeTypeActiveCount = chargeTypeActiveCount;
    }

    public int getChargeAmountActiveCount() {
        return chargeAmountActiveCount;
    }

    public void setChargeAmountActiveCount(int chargeAmountActiveCount) {
        this.chargeAmountActiveCount = chargeAmountActiveCount;
    }

    public int getPercentCommissionActiveCount() {
        return percentCommissionActiveCount;
    }

    public void setPercentCommissionActiveCount(int percentCommissionActiveCount) {
        this.percentCommissionActiveCount = percentCommissionActiveCount;
    }

    public ChargeTableRow getChargeRow(int index) {
        return chargeRows.get(index);
    }
}
