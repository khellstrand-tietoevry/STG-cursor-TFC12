package no.fellesdata.tfi.tfc12.l140;

import no.fellesdata.tfi.tfc12.persistence.ContractKey;

import java.util.ArrayList;
import java.util.List;

public class RecordingShadowPorts {

    public record ExternalCall(String port, ContractKey key, String source, String dest) {
    }

    private final List<ExternalCall> calls = new ArrayList<>();

    public void recordIcc0(ContractKey key) {
        calls.add(new ExternalCall("F115ICC0", key, "ACTUAL", "WORK"));
    }

    public void recordIrp0(ContractKey key, String source, String dest) {
        calls.add(new ExternalCall("F115IRP0", key, source, dest));
    }

    public void recordIei0(ContractKey key, String source, String dest) {
        calls.add(new ExternalCall("F115IEI0", key, source, dest));
    }

    public void recordL040(ContractKey key, String source, String dest) {
        calls.add(new ExternalCall("F115L040", key, source, dest));
    }

    public List<ExternalCall> calls() {
        return List.copyOf(calls);
    }
}
