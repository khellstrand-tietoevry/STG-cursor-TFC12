package no.fellesdata.tfi.tfc12.l140;

import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.persistence.ContractKey;
import no.fellesdata.tfi.tfc12.persistence.MainContractRecord;
import no.fellesdata.tfi.tfc12.persistence.TradeFinancePersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FtfcL140ShadowModuleTest {

    private static final ContractKey KEY = new ContractKey("123456789012345678", "LC", "0001234");

    private TradeFinancePersistence persistence;
    private RecordingShadowPorts ports;
    private FtfcL140ShadowModule module;

    @BeforeEach
    void setUp() {
        persistence = TradeFinancePersistence.inMemory();
        persistence.seedStandardFixtures(KEY);
        ports = new RecordingShadowPorts();
        module = L140Modules.recording(persistence, ports);
    }

    @Test
    void rejectsShadowWhenMainContractInProgress() {
        persistence.store().upsertActual(KEY, MainContractRecord.active("ACTIVE", true));
        ServoErrorArea error = new ServoErrorArea();

        module.createShadow(request(true), error);

        assertTrue(error.isReturnError());
        assertEquals("TF-SY-SHADOW-AE", error.getMessage());
        assertTrue(ports.calls().isEmpty());
    }

    @Test
    void happyPathInvokesImc0AndExternalPorts() {
        ServoErrorArea error = new ServoErrorArea();

        module.createShadow(request(true), error);

        assertTrue(error.isReturnOk());
        List<String> portNames =
                ports.calls().stream().map(RecordingShadowPorts.ExternalCall::port).toList();
        assertEquals(List.of("F115ICC0", "F115IRP0", "F115IEI0", "F115L040"), portNames);
        assertTrue(
                persistence.mainContracts().selectWork(KEY).orElseThrow().inProgress(),
                "WORK copy marked in progress after shadow");
    }

    @Test
    void skipsExternalPortsWhenCreateShadowFalse() {
        ServoErrorArea error = new ServoErrorArea();
        module.createShadow(request(false), error);
        assertTrue(error.isReturnOk());
        assertTrue(ports.calls().isEmpty());
    }

    @Test
    void missingFunctionAbends() {
        ServoErrorArea error = new ServoErrorArea();
        module.createShadow(new ShadowCreateRequest("", "M", KEY, true), error);
        assertTrue(error.isReturnAbend());
    }

    private static ShadowCreateRequest request(boolean createShadow) {
        return new ShadowCreateRequest("TFC12", "01", KEY, createShadow);
    }
}
