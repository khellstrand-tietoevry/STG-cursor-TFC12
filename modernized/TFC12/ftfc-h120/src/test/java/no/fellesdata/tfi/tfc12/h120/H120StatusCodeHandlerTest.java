package no.fellesdata.tfi.tfc12.h120;

import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.h120.boundary.CentralServoErrorMessagePort;
import no.fellesdata.tfi.tfc12.h120.boundary.RecordingServoErrorMessagePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class H120StatusCodeHandlerTest {

    private H120StatusCodeHandler handler;
    private StgHeader header;
    private ServoErrorArea error;

    @BeforeEach
    void setUp() {
        handler = new H120StatusCodeHandler();
        handler.reset();
        header = new StgHeader();
        error = new ServoErrorArea();
    }

    @Test
    void kWarningContinuesAndRestoresMessageAfterLOk() {
        header.setMessageCode("TF-SY-WARN");
        header.setMessageText("K warning");
        error.setReturnWarning();
        RecordingServoErrorMessagePort port = new RecordingServoErrorMessagePort();

        assertEquals(
                H120StatusCodeHandler.FlowDecision.CONTINUE,
                handler.afterKModule(header, error, port));
        assertEquals("AW", header.getStatusCode());

        error.setReturnOk();
        handler.afterLModule(header, error, port);

        assertEquals("AW", header.getStatusCode());
        assertEquals("TF-SY-WARN", header.getMessageCode());
        assertEquals("K warning", header.getMessageText());
    }

    @Test
    void kWarningGobackStopsFlow() {
        error.setReturnWarningGoback();
        assertEquals(
                H120StatusCodeHandler.FlowDecision.STOP,
                handler.afterKModule(header, error, (h, log) -> {}));
        assertEquals("AW", header.getStatusCode());
    }

    @Test
    void centralErrorPortRewritesBlankMessageText() {
        header.setMessageCode("TF-SY-OP-RETURN-PAY-PTY");
        new CentralServoErrorMessagePort().publish(header, true);
        assertEquals("FTFCH120", header.getMessageText());
    }
}
