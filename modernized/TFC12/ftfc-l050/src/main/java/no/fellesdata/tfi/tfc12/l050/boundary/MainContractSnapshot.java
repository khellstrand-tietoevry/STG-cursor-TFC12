package no.fellesdata.tfi.tfc12.l050.boundary;

public record MainContractSnapshot(String status, boolean inProgress, String updateTimestamp) {

    public static MainContractSnapshot active(String status, boolean inProgress) {
        return new MainContractSnapshot(status, inProgress, "2026-09-30T10:00:00.000000");
    }
}
