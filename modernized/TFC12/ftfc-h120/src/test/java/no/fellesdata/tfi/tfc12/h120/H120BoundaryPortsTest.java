package no.fellesdata.tfi.tfc12.h120;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.ServoFieldIndicator;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.h120.boundary.H120TracePoint;
import no.fellesdata.tfi.tfc12.h120.boundary.RecordingServoErrorMessagePort;
import no.fellesdata.tfi.tfc12.h120.boundary.RecordingServoTracePort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class H120BoundaryPortsTest {

    @Test
    void tracePortRecordsA000SequenceWhenTraceOn() {
        RecordingServoTracePort tracePort = new RecordingServoTracePort();
        H120BoundaryPorts boundaries = new H120BoundaryPorts(tracePort, (header, logYes) -> {});
        FtfcH120Orchestrator orchestrator = H120Modules.defaultStack(
                no.fellesdata.tfi.tfc12.k120.K120Modules.seededPersistence(), boundaries);

        StgHeader header = stgHeader();
        header.setTraceOn(true);
        orchestrator.execute(header, validData(), new ServoErrorArea());

        assertEquals(
                H120TracePoint.A000,
                tracePort.events().getFirst().point());
        assertTrue(tracePort.events().stream().anyMatch(e -> e.point() == H120TracePoint.C000));
    }

    @Test
    void errorMessagePortInvokedOnKValidationFailure() {
        RecordingServoErrorMessagePort errorPort = new RecordingServoErrorMessagePort();
        H120BoundaryPorts boundaries = new H120BoundaryPorts((point, header, data) -> {}, errorPort);
        FtfcH120Orchestrator orchestrator = H120Modules.defaultStack(
                no.fellesdata.tfi.tfc12.k120.K120Modules.seededPersistence(), boundaries);

        StgHeader header = stgHeader();
        CreateChargeFunction data = validData();
        data.setFinInstNoIndicator(' ');
        ServoErrorArea error = new ServoErrorArea();
        orchestrator.execute(header, data, error);

        assertEquals("AE", header.getStatusCode());
        assertEquals(1, errorPort.publications().size());
    }

    private static StgHeader stgHeader() {
        StgHeader header = new StgHeader();
        header.setFunction("TFC1201");
        header.setMedium("01");
        return header;
    }

    private static CreateChargeFunction validData() {
        CreateChargeFunction data = new CreateChargeFunction();
        data.setFinInstNoIndicator(ServoFieldIndicator.SUPPLIED);
        data.setFinInstNo("123456789012345678");
        data.setContractTypeIndicator(ServoFieldIndicator.SUPPLIED);
        data.setContractType("LC");
        data.setContractNoIndicator(ServoFieldIndicator.SUPPLIED);
        data.setContractNo("0001234");
        data.setLastSavedDateIndicator(ServoFieldIndicator.SUPPLIED);
        data.setLastSavedDate("2026-09-30T12:00:00.000000");
        data.setOperationTypeIndicator(ServoFieldIndicator.SUPPLIED);
        data.setOperationType("CREATE");
        return data;
    }
}
