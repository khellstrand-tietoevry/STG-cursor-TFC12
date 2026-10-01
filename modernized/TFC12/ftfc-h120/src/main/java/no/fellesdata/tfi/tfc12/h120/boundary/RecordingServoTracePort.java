package no.fellesdata.tfi.tfc12.h120.boundary;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;

import java.util.ArrayList;
import java.util.List;

public class RecordingServoTracePort implements ServoTracePort {

    public record TraceEvent(H120TracePoint point, String traceArea) {}

    private final List<TraceEvent> events = new ArrayList<>();

    @Override
    public void trace(H120TracePoint tracePoint, StgHeader header, CreateChargeFunction data) {
        String area = switch (tracePoint) {
            case A000, A002, C000, C001, D000, D001 -> "R400CH01";
            case A001, A003 -> "RTFCE120";
        };
        events.add(new TraceEvent(tracePoint, area));
    }

    public List<TraceEvent> events() {
        return List.copyOf(events);
    }
}
