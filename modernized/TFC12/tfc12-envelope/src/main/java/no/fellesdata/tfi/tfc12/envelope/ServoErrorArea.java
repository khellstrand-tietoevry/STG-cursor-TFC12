package no.fellesdata.tfi.tfc12.envelope;

/**
 * R7919999 standard error area (subset used by H/K/L in TFC12).
 */
public class ServoErrorArea {

    public enum ReturnKind {
        OK,
        WARNING,
        WARNING_GOBACK,
        ERROR,
        ABEND
    }

    private ReturnKind returnKind = ReturnKind.OK;
    private String paragraph;
    private String message;

    public ReturnKind getReturnKind() {
        return returnKind;
    }

    public void setReturnOk() {
        this.returnKind = ReturnKind.OK;
    }

    public void setReturnError() {
        this.returnKind = ReturnKind.ERROR;
    }

    public void setReturnAbend() {
        this.returnKind = ReturnKind.ABEND;
    }

    public void setReturnWarning() {
        this.returnKind = ReturnKind.WARNING;
    }

    public void setReturnWarningGoback() {
        this.returnKind = ReturnKind.WARNING_GOBACK;
    }

    public boolean isReturnOk() {
        return returnKind == ReturnKind.OK;
    }

    public boolean isReturnWarning() {
        return returnKind == ReturnKind.WARNING;
    }

    public boolean isReturnWarningGoback() {
        return returnKind == ReturnKind.WARNING_GOBACK;
    }

    public boolean isReturnError() {
        return returnKind == ReturnKind.ERROR;
    }

    public boolean isReturnAbend() {
        return returnKind == ReturnKind.ABEND;
    }

    public String getParagraph() {
        return paragraph;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
