package no.fellesdata.tfi.tfc12.h120;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.ServoFieldIndicator;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.k120.FtfcK120Module;
import no.fellesdata.tfi.tfc12.l120.L120Modules;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FtfcH120OrchestratorTest {

    @Test
    void rule015_invokesKModuleBeforeLModule() {
        List<String> calls = new ArrayList<>();
        FtfcK120Module kModule = new FtfcK120Module() {
            @Override
            public void execute(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
                calls.add("K");
                super.execute(header, data, error);
            }
        };
        var lModule = L120Modules.permissiveDefaults();
        FtfcH120Orchestrator orchestrator = new FtfcH120Orchestrator(
                kModule,
                (header, data, error) -> {
                    calls.add("L");
                    lModule.execute(header, data, error);
                });

        StgHeader header = stgHeader();
        ServoErrorArea error = new ServoErrorArea();
        orchestrator.execute(header, validData(), error);

        assertEquals(List.of("K", "L"), calls);
        assertTrue(error.isReturnOk());
    }

    @Test
    void hardenedFlow_kWarningRunsLAndRestoresAwStatus() {
        FtfcH120Orchestrator orchestrator = new FtfcH120Orchestrator(
                new FtfcK120Module() {
                    @Override
                    public void execute(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
                        header.setMessageCode("TF-SY-WARN");
                        header.setMessageText("from K");
                        error.setReturnWarning();
                    }
                },
                (header, data, error) -> error.setReturnOk(),
                H120BoundaryPorts.hardened());

        StgHeader header = stgHeader();
        ServoErrorArea error = new ServoErrorArea();
        orchestrator.execute(header, validData(), error);

        assertEquals("AW", header.getStatusCode());
        assertEquals("TF-SY-WARN", header.getMessageCode());
        assertTrue(error.isReturnOk());
    }

    @Test
    void rule015_skipsLWhenKReturnsError() {
        List<String> calls = new ArrayList<>();
        FtfcH120Orchestrator orchestrator =
                new FtfcH120Orchestrator(new FtfcK120Module(), (header, data, error) -> calls.add("L"));

        StgHeader header = stgHeader();
        CreateChargeFunction data = validData();
        data.setFinInstNoIndicator(' ');
        ServoErrorArea error = new ServoErrorArea();
        orchestrator.execute(header, data, error);

        assertTrue(calls.isEmpty());
        assertTrue(error.isReturnError());
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
