package no.fellesdata.tfi.tfc12.persistence;

/** R115IMC0 output subset (TF_Main_Contract). */
public record MainContractRecord(
        String status,
        boolean inProgress,
        String updateTimestamp,
        String updUserId) {

    public static MainContractRecord active(String status, boolean inProgress) {
        return new MainContractRecord(status, inProgress, "2026-09-30T10:00:00.000000", "SYS");
    }
}
