package no.fellesdata.tfi.tfc12.envelope;

/**
 * Subset of R400CH01 STG header fields used by TFC12 H/K validation and status mapping.
 */
public class StgHeader {

    public static final int MESSAGE_SLOTS = 2;

    private boolean traceOn;
    private String statusCode;
    private String messageCode;
    private String messageText;
    private final String[] messageKeys = new String[MESSAGE_SLOTS];
    private final String[] messageData = new String[MESSAGE_SLOTS];
    private String function;
    private String medium;
    private String userid;
    private String terminalId;
    private String bank;
    private String execDate;
    private String systemDate;
    private String systemTime;
    private String targetStatusAfterSysRel;

    public boolean isTraceOn() {
        return traceOn;
    }

    public void setTraceOn(boolean traceOn) {
        this.traceOn = traceOn;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageKey(int index) {
        return messageKeys[index];
    }

    public void setMessageKey(int index, String key) {
        messageKeys[index] = key;
    }

    public String getMessageData(int index) {
        return messageData[index];
    }

    public void setMessageData(int index, String data) {
        messageData[index] = data;
    }

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getExecDate() {
        return execDate;
    }

    public void setExecDate(String execDate) {
        this.execDate = execDate;
    }

    public String getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(String systemDate) {
        this.systemDate = systemDate;
    }

    public String getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(String systemTime) {
        this.systemTime = systemTime;
    }

    public String getTargetStatusAfterSysRel() {
        return targetStatusAfterSysRel;
    }

    public void setTargetStatusAfterSysRel(String targetStatusAfterSysRel) {
        this.targetStatusAfterSysRel = targetStatusAfterSysRel;
    }
}
