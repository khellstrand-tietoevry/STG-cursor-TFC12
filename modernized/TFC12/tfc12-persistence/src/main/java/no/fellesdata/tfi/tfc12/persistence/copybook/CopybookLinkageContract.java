package no.fellesdata.tfi.tfc12.persistence.copybook;

import java.util.List;

/**
 * Documents linkage fields from in-repo copybooks used by Java ports (contract tests).
 */
public enum CopybookLinkageContract {
    R115IMC0("20000619", List.of("R115IMC0-In-Fin-Inst-No", "R115IMC0-In-Contract-Type", "R115IMC0-In-Contract-No", "R115IMC0-Status", "R115IMC0-In-Progress", "R115IMC0-Upd-Timestamp")),
    R115ICA0("20000619", List.of("R115ICA0-In-Fin-Inst-No", "R115ICA0-In-Contract-Type", "R115ICA0-In-Contract-No", "R115ICA0-Currency")),
    R115ICH0("20010208", List.of("R115ICH0-In-Fin-Inst-No", "R115ICH0-In-Contract-Type", "R115ICH0-In-Contract-No", "R115ICH0-In-CA-Currency")),
    R115IPA0("20000619", List.of("R115IPA0-In-Fin-Inst-No", "R115IPA0-In-Contract-Type", "R115IPA0-In-Contract-No", "R115IPA0-In-PA-Seq-No", "R115IPA0-Status")),
    R115IPC0("19990916", List.of("R115IPC0-In-Fin-Inst-No", "R115IPC0-In-Contract-Type", "R115IPC0-In-Contract-No", "R115IPC0-In-PA-Seq-No")),
    R115ISC0("19990317", List.of("R115ISC0-In-ST-Type", "R115ISC0-In-Fin-Inst-No", "R115ISC0-In-SC-Code", "R115ISC0-Contract-Name", "R115ISC0-Contr-Imp-Exp", "R115ISC0-Property")),
    R115ISR0("19990317", List.of("R115ISR0-In-F-I-No", "R115ISR0-In-From-ST-Type", "R115ISR0-In-From-SC-Code", "R115ISR0-In-To-ST-Type", "R115ISR0-In-To-SC-Code", "R115ISR0-In-Relation-Type"));

    private final String versionNo;
    private final List<String> linkageFields;

    CopybookLinkageContract(String versionNo, List<String> linkageFields) {
        this.versionNo = versionNo;
        this.linkageFields = linkageFields;
    }

    public String versionNo() {
        return versionNo;
    }

    public List<String> linkageFields() {
        return linkageFields;
    }
}
