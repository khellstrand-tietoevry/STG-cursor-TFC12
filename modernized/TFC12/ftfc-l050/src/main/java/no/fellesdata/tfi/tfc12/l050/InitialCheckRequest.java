package no.fellesdata.tfi.tfc12.l050;

/**
 * R115L050-Initial-Check linkage subset (qualification + keys).
 */
public class InitialCheckRequest {

    private String function;
    private String medium;
    private String finInstNo;
    private String contractType;
    private String contractNo;
    private int partAmountSeqNo;
    private String operationType;
    private String lastUpdateTimestamp;
    private boolean checkUpdateTimestamp = true;

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getFinInstNo() {
        return finInstNo;
    }

    public void setFinInstNo(String finInstNo) {
        this.finInstNo = finInstNo;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public int getPartAmountSeqNo() {
        return partAmountSeqNo;
    }

    public void setPartAmountSeqNo(int partAmountSeqNo) {
        this.partAmountSeqNo = partAmountSeqNo;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public boolean isCheckUpdateTimestamp() {
        return checkUpdateTimestamp;
    }

    public void setCheckUpdateTimestamp(boolean checkUpdateTimestamp) {
        this.checkUpdateTimestamp = checkUpdateTimestamp;
    }

    /** COBOL {@code R115L050-Function(3:1)} — third character of the 8-byte function id. */
    public char functionThirdCharacter() {
        if (function == null || function.length() < 3) {
            return ' ';
        }
        return function.charAt(2);
    }
}
