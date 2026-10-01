package no.fellesdata.tfi.tfc12.h120;

import no.fellesdata.tfi.tfc12.h120.boundary.CentralServoErrorMessagePort;
import no.fellesdata.tfi.tfc12.h120.boundary.ServoErrorMessagePort;
import no.fellesdata.tfi.tfc12.h120.boundary.ServoTracePort;

public record H120BoundaryPorts(ServoTracePort trace, ServoErrorMessagePort errorMessage) {

    public static H120BoundaryPorts noop() {
        return new H120BoundaryPorts((point, header, data) -> {}, (header, logYes) -> {});
    }

    /** Local hardening default: central error service rewrite via {@link CentralServoErrorMessagePort}. */
    public static H120BoundaryPorts hardened() {
        return new H120BoundaryPorts((point, header, data) -> {}, new CentralServoErrorMessagePort());
    }
}
