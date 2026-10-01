package no.fellesdata.tfi.tfc12.h120;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.h120.boundary.H120TracePoint;
import no.fellesdata.tfi.tfc12.k120.FtfcK120Module;
import no.fellesdata.tfi.tfc12.l120.FtfcL120ModulePort;

/**
 * FTFCH120 A000-Main path (K then L) with F791TRAC / F7918030 boundary ports.
 */
public class FtfcH120Orchestrator {

    private final FtfcK120Module kModule;
    private final FtfcL120ModulePort lModule;
    private final H120BoundaryPorts boundaries;
    private final H120StatusCodeHandler statusCodeHandler = new H120StatusCodeHandler();

    public FtfcH120Orchestrator(FtfcK120Module kModule, FtfcL120ModulePort lModule) {
        this(kModule, lModule, H120BoundaryPorts.noop());
    }

    public FtfcH120Orchestrator(
            FtfcK120Module kModule, FtfcL120ModulePort lModule, H120BoundaryPorts boundaries) {
        this.kModule = kModule;
        this.lModule = lModule;
        this.boundaries = boundaries;
    }

    public void execute(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        statusCodeHandler.reset();
        initialize(error);
        traceIfEnabled(header, data, H120TracePoint.A000, H120TracePoint.A001);

        callKModule(header, data, error);
        traceIfEnabled(header, data, H120TracePoint.C000, H120TracePoint.C001);

        if (statusCodeHandler.afterKModule(header, error, boundaries.errorMessage())
                == H120StatusCodeHandler.FlowDecision.STOP) {
            return;
        }

        callLModule(header, data, error);
        traceIfEnabled(header, data, H120TracePoint.D000, H120TracePoint.D001);
        statusCodeHandler.afterLModule(header, error, boundaries.errorMessage());

        traceIfEnabled(header, data, H120TracePoint.A002, H120TracePoint.A003);
    }

    private void initialize(ServoErrorArea error) {
        error.setReturnOk();
    }

    private void callKModule(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        kModule.execute(header, data, error);
    }

    private void callLModule(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        lModule.execute(header, data, error);
    }

    private void traceIfEnabled(
            StgHeader header, CreateChargeFunction data, H120TracePoint first, H120TracePoint second) {
        if (!header.isTraceOn()) {
            return;
        }
        boundaries.trace().trace(first, header, data);
        boundaries.trace().trace(second, header, data);
    }
}
