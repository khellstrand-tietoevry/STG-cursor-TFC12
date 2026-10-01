package no.fellesdata.tfi.tfc12.k120;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.ServoFieldIndicator;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.l050.InitialCheckModule;
import no.fellesdata.tfi.tfc12.l050.InitialCheckRequest;
import no.fellesdata.tfi.tfc12.l050.boundary.MainContractSnapshot;
import no.fellesdata.tfi.tfc12.l050.boundary.SysCodeRelationPort;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateChargeK120InitialCheckTest {

    @Test
    void rule012_kModuleCallsInitialCheckAndMapsE102OnError() {
        InitialCheckModule module = new InitialCheckModule(
                (request, tableVersion) -> MainContractSnapshot.active("ACTIVE", false),
                (request, workflowStatus) -> SysCodeRelationPort.Result.INVALID_STATUS_OPERATION,
                (fi, status) -> "WORKFLOW",
                (request, tableVersion) -> "NOK",
                (request, tableVersion, currency) -> "ACTIVE");

        CreateChargeK120InitialCheck initialCheck = new CreateChargeK120InitialCheck(module);
        StgHeader header = header();
        ServoErrorArea error = new ServoErrorArea();

        initialCheck.runInitialChecks(header, validFunction(), error);

        assertTrue(error.isReturnError());
        assertEquals("FM", header.getMessageCode());
        assertEquals("E102", error.getParagraph());
    }

    @Test
    void rule012_kModuleMapsE101OnAbend() {
        InitialCheckModule module = new InitialCheckModule(
                (request, tableVersion) -> MainContractSnapshot.active("ACTIVE", false),
                (request, workflowStatus) -> SysCodeRelationPort.Result.OK,
                (fi, status) -> "WORKFLOW",
                (request, tableVersion) -> "NOK",
                (request, tableVersion, currency) -> "ACTIVE") {
            @Override
            public void execute(InitialCheckRequest request, ServoErrorArea error) {
                error.setMessage("TF-SY-FUNC-AND-MEDIUM");
                error.setParagraph("C102");
                error.setReturnAbend();
            }
        };

        CreateChargeK120InitialCheck initialCheck = new CreateChargeK120InitialCheck(module);
        StgHeader header = header();
        ServoErrorArea error = new ServoErrorArea();

        initialCheck.runInitialChecks(header, validFunction(), error);

        assertTrue(error.isReturnAbend());
        assertEquals("FM", header.getMessageCode());
        assertEquals("E101", error.getParagraph());
    }

    @Test
    void rule012_ftfcK120ModuleInvokesInitialCheck() {
        AtomicBoolean invoked = new AtomicBoolean();
        InitialCheckModule module = new InitialCheckModule(
                (request, tableVersion) -> MainContractSnapshot.active("ACTIVE", false),
                (request, workflowStatus) -> SysCodeRelationPort.Result.OK,
                (fi, status) -> "WORKFLOW",
                (request, tableVersion) -> "NOK",
                (request, tableVersion, currency) -> "ACTIVE") {
            @Override
            public void execute(InitialCheckRequest request, ServoErrorArea error) {
                invoked.set(true);
                super.execute(request, error);
            }
        };

        FtfcK120Module kModule = new FtfcK120Module(new CreateChargeK120InitialCheck(module));
        StgHeader header = header();
        ServoErrorArea error = new ServoErrorArea();

        kModule.execute(header, validFunction(), error);

        assertTrue(invoked.get());
        assertTrue(error.isReturnOk());
    }

    private static StgHeader header() {
        StgHeader header = new StgHeader();
        header.setFunction("TFC1201");
        header.setMedium("01");
        return header;
    }

    private static CreateChargeFunction validFunction() {
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
