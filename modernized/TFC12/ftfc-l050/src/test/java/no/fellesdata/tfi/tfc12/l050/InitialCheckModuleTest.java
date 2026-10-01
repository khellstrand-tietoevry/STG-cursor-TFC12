package no.fellesdata.tfi.tfc12.l050;

import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.l050.boundary.ContractAmountPort;
import no.fellesdata.tfi.tfc12.l050.boundary.MainContractPort;
import no.fellesdata.tfi.tfc12.l050.boundary.MainContractSnapshot;
import no.fellesdata.tfi.tfc12.l050.boundary.PartAmountPort;
import no.fellesdata.tfi.tfc12.l050.boundary.SysCodeLookupPort;
import no.fellesdata.tfi.tfc12.l050.boundary.SysCodeRelationPort;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InitialCheckModuleTest {

    @Test
    void rule001_abendsWhenFunctionOrMediumMissing() {
        InitialCheckModule module = InitialCheckModules.permissiveDefaults();
        InitialCheckRequest request = baseRequest();
        request.setFunction("  ");
        ServoErrorArea error = new ServoErrorArea();

        module.execute(request, error);

        assertTrue(error.isReturnAbend());
        assertEquals("C102", error.getParagraph());
        assertEquals("TF-SY-FUNC-AND-MEDIUM", error.getMessage());
    }

    @Test
    void rule002_invalidStatusOperationYieldsError() {
        SysCodeRelationPort relationPort =
                (request, workflowStatus) -> SysCodeRelationPort.Result.INVALID_STATUS_OPERATION;
        InitialCheckModule module = moduleWith(relationPort, (fi, status) -> "WORKFLOW");

        ServoErrorArea error = new ServoErrorArea();
        module.execute(baseRequest(), error);

        assertTrue(error.isReturnError());
        assertEquals("F102", error.getParagraph());
        assertEquals("TF-SY-INVALID-STATUS-OPERTYP", error.getMessage());
    }

    @Test
    void rule003_permanentStatusBlocksInProgressContract() {
        MainContractPort inProgressPort =
                (request, tableVersion) -> MainContractSnapshot.active("ACTIVE", true);
        SysCodeLookupPort permProperty = (fi, status) -> "STATUS-PERM-LOCK";
        InitialCheckModule module = new InitialCheckModule(
                inProgressPort,
                (request, workflowStatus) -> SysCodeRelationPort.Result.OK,
                permProperty,
                (request, tableVersion) -> "NOK",
                (request, tableVersion, currency) -> "ACTIVE");

        ServoErrorArea error = new ServoErrorArea();
        module.execute(baseRequest(), error);

        assertTrue(error.isReturnError());
        assertEquals("G001", error.getParagraph());
        assertEquals("TF-SY-MC-INPROG-Y", error.getMessage());
    }

    @Test
    void rule013_paSeqZeroSkipsPartAmountReads() {
        AtomicInteger contractAmountCalls = new AtomicInteger();
        AtomicInteger partAmountCalls = new AtomicInteger();
        InitialCheckModule module = new InitialCheckModule(
                (request, tableVersion) -> MainContractSnapshot.active("MAIN-STATUS", false),
                (request, workflowStatus) -> SysCodeRelationPort.Result.OK,
                (fi, status) -> "WORKFLOW",
                (request, tableVersion) -> {
                    contractAmountCalls.incrementAndGet();
                    return "NOK";
                },
                (request, tableVersion, currency) -> {
                    partAmountCalls.incrementAndGet();
                    return "PART-STATUS";
                });

        InitialCheckRequest request = baseRequest();
        request.setPartAmountSeqNo(0);
        module.execute(request, new ServoErrorArea());

        assertEquals(0, contractAmountCalls.get());
        assertEquals(0, partAmountCalls.get());
    }

    @Test
    void rule017_updateTimestampCheckNotInvokedOnMainPath() {
        MainContractPort staleTimestampPort = (request, tableVersion) ->
                new MainContractSnapshot("ACTIVE", false, "STALE-TIMESTAMP");
        InitialCheckModule module = new InitialCheckModule(
                staleTimestampPort,
                (request, workflowStatus) -> SysCodeRelationPort.Result.OK,
                (fi, status) -> "WORKFLOW",
                (request, tableVersion) -> "NOK",
                (request, tableVersion, currency) -> "ACTIVE");

        InitialCheckRequest request = baseRequest();
        request.setLastUpdateTimestamp("DIFFERENT-TS");
        ServoErrorArea error = new ServoErrorArea();
        module.execute(request, error);

        assertTrue(error.isReturnOk());
        assertFalse(module.wasUpdateTimestampCheckInvoked());
    }

    private static InitialCheckModule moduleWith(
            SysCodeRelationPort relationPort, SysCodeLookupPort lookupPort) {
        return new InitialCheckModule(
                (request, tableVersion) -> MainContractSnapshot.active("ACTIVE", false),
                relationPort,
                lookupPort,
                (request, tableVersion) -> "NOK",
                (request, tableVersion, currency) -> "ACTIVE");
    }

    private static InitialCheckRequest baseRequest() {
        InitialCheckRequest request = new InitialCheckRequest();
        request.setFunction("TFC1201");
        request.setMedium("01");
        request.setFinInstNo("123456789012345678");
        request.setContractType("LC");
        request.setContractNo("0001234");
        request.setOperationType("CREATE");
        request.setLastUpdateTimestamp("2026-09-30T12:00:00.000000");
        return request;
    }
}
