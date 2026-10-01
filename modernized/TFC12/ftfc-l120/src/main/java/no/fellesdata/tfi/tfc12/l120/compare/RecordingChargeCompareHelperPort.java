package no.fellesdata.tfi.tfc12.l120.compare;

import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecordingChargeCompareHelperPort implements ChargeCompareHelperPort {

    public record Invocation(ChargeCompareHelper helper, String legacyParagraph) {
    }

    private final List<Invocation> invocations = new ArrayList<>();

    @Override
    public void invoke(ChargeCompareHelper helper, ChargeCompareContext context, ServoErrorArea error) {
        invocations.add(new Invocation(helper, helper.legacyParagraph()));
    }

    public List<Invocation> invocations() {
        return Collections.unmodifiableList(invocations);
    }

    public void clear() {
        invocations.clear();
    }
}
