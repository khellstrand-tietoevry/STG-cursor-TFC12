package no.fellesdata.tfi.tfc12.l120;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.ServoFieldIndicator;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.l050.boundary.MainContractPort;
import no.fellesdata.tfi.tfc12.l050.boundary.MainContractSnapshot;
import no.fellesdata.tfi.tfc12.l120.boundary.RecordingShadowCreationPort;
import no.fellesdata.tfi.tfc12.l120.compare.ChargeCompareHelper;
import no.fellesdata.tfi.tfc12.l120.compare.RecordingChargeCompareHelperPort;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FtfcL120ModuleTest {

    @Test
    void rule014_createShadowWhenMainContractNotInProgress() {
        RecordingShadowCreationPort shadowPort = new RecordingShadowCreationPort();
        RecordingChargeCompareHelperPort comparePort = new RecordingChargeCompareHelperPort();
        MainContractPort mainContractPort =
                (request, tableVersion) -> MainContractSnapshot.active("CLOSED", false);

        FtfcL120Module module = new FtfcL120Module(mainContractPort, shadowPort, comparePort);
        module.execute(header(), validFunction(), new ServoErrorArea());

        assertTrue(shadowPort.wasInvoked());
    }

    @Test
    void rule014_skipsShadowWhenMainContractInProgress() {
        RecordingShadowCreationPort shadowPort = new RecordingShadowCreationPort();
        RecordingChargeCompareHelperPort comparePort = new RecordingChargeCompareHelperPort();
        MainContractPort mainContractPort =
                (request, tableVersion) -> MainContractSnapshot.active("ACTIVE", true);

        FtfcL120Module module = new FtfcL120Module(mainContractPort, shadowPort, comparePort);
        module.execute(header(), validFunction(), new ServoErrorArea());

        assertFalse(shadowPort.wasInvoked());
    }

    @Test
    void compareHelpersInvokedFromF100AndD200Paragraphs() {
        RecordingChargeCompareHelperPort comparePort = new RecordingChargeCompareHelperPort();
        FtfcL120Module module = newModule(comparePort, MainContractSnapshot.active("ACTIVE", true));

        CreateChargeFunction data = validFunction();
        data.setPartAmountNoIndicator(ServoFieldIndicator.SUPPLIED);
        data.setPartAmountNo(2);

        module.execute(header(), data, new ServoErrorArea());

        List<RecordingChargeCompareHelperPort.Invocation> calls = comparePort.invocations();
        assertEquals(
                List.of(
                        ChargeCompareHelper.F115L080,
                        ChargeCompareHelper.F115L070,
                        ChargeCompareHelper.F115L060,
                        ChargeCompareHelper.F115L030),
                calls.stream().map(RecordingChargeCompareHelperPort.Invocation::helper).toList());
        assertEquals(
                "D200-Move-Charges",
                calls.get(0).legacyParagraph());
        assertEquals(
                "F100-Modify-Input-Charges",
                calls.get(1).legacyParagraph());
    }

    @Test
    void rule013_paSeqZeroSkipsD200MoveChargesAtLBoundary() {
        RecordingChargeCompareHelperPort comparePort = new RecordingChargeCompareHelperPort();
        FtfcL120Module module = newModule(comparePort, MainContractSnapshot.active("ACTIVE", true));

        CreateChargeFunction data = validFunction();
        data.setPartAmountNoIndicator(ServoFieldIndicator.SUPPLIED);
        data.setPartAmountNo(0);

        module.execute(header(), data, new ServoErrorArea());

        assertFalse(
                comparePort.invocations().stream()
                        .anyMatch(i -> i.helper() == ChargeCompareHelper.F115L080));
        assertTrue(
                comparePort.invocations().stream()
                        .anyMatch(i -> i.helper() == ChargeCompareHelper.F115L070));
    }

    @Test
    void shouldMoveCharges_requiresSuppliedIndicatorAndPositiveSeq() {
        CreateChargeFunction data = validFunction();
        data.setPartAmountNoIndicator(ServoFieldIndicator.SUPPLIED);
        data.setPartAmountNo(1);
        assertTrue(FtfcL120Module.shouldMoveCharges(data));

        data.setPartAmountNo(0);
        assertFalse(FtfcL120Module.shouldMoveCharges(data));

        data.setPartAmountNo(3);
        data.setPartAmountNoIndicator(' ');
        assertFalse(FtfcL120Module.shouldMoveCharges(data));
    }

    private static FtfcL120Module newModule(
            RecordingChargeCompareHelperPort comparePort, MainContractSnapshot contract) {
        MainContractPort mainContractPort = (request, tableVersion) -> contract;
        return new FtfcL120Module(mainContractPort, (h, f, e) -> {}, comparePort);
    }

    private static StgHeader header() {
        StgHeader header = new StgHeader();
        header.setFunction("TFC1201");
        header.setMedium("01");
        header.setBank("1234");
        return header;
    }

    private static CreateChargeFunction validFunction() {
        CreateChargeFunction data = new CreateChargeFunction();
        data.setFinInstNo("123456789012345678");
        data.setContractType("LC");
        data.setContractNo("0001234");
        data.setOperationType("CREATE");
        return data;
    }
}
