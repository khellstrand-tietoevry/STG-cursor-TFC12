package no.fellesdata.tfi.tfc12.l120;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.ServoFieldIndicator;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.l050.InitialCheckRequest;
import no.fellesdata.tfi.tfc12.l050.boundary.MainContractPort;
import no.fellesdata.tfi.tfc12.l050.boundary.MainContractSnapshot;
import no.fellesdata.tfi.tfc12.l120.boundary.ShadowCreationPort;
import no.fellesdata.tfi.tfc12.l120.compare.ChargeCompareContext;
import no.fellesdata.tfi.tfc12.l120.compare.ChargeCompareHelper;
import no.fellesdata.tfi.tfc12.l120.compare.ChargeCompareHelperPort;

/**
 * FTFCL120 Phase 3 slice: D000 preparations, E000/F100, G000/H100/H900 compare-helper orchestration.
 */
public class FtfcL120Module implements FtfcL120ModulePort {

    private final MainContractPort mainContractPort;
    private final ShadowCreationPort shadowCreationPort;
    private final ChargeCompareHelperPort compareHelperPort;

    public FtfcL120Module(
            MainContractPort mainContractPort,
            ShadowCreationPort shadowCreationPort,
            ChargeCompareHelperPort compareHelperPort) {
        this.mainContractPort = mainContractPort;
        this.shadowCreationPort = shadowCreationPort;
        this.compareHelperPort = compareHelperPort;
    }

    @Override
    public void execute(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        error.setReturnOk();
        if (!preparations(header, data, error)) {
            return;
        }
        if (!processing(header, data, error)) {
            return;
        }
        update(header, data, error);
    }

    private boolean preparations(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        InitialCheckRequest contractKey = toContractKey(data);
        MainContractSnapshot contract = mainContractPort.read(contractKey, "ACTUAL");

        if (!contract.inProgress()) {
            shadowCreationPort.createShadow(header, data, error);
            if (!error.isReturnOk()) {
                return false;
            }
        }

        if (shouldMoveCharges(data)) {
            compareHelperPort.invoke(
                    ChargeCompareHelper.F115L080, new ChargeCompareContext(header, data), error);
            if (!error.isReturnOk()) {
                return false;
            }
        }
        return true;
    }

    private boolean processing(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        compareHelperPort.invoke(
                ChargeCompareHelper.F115L070, new ChargeCompareContext(header, data), error);
        return error.isReturnOk();
    }

    private void update(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        if (!error.isReturnOk()) {
            return;
        }
        compareHelperPort.invoke(
                ChargeCompareHelper.F115L060, new ChargeCompareContext(header, data), error);
        if (!error.isReturnOk()) {
            return;
        }
        compareHelperPort.invoke(
                ChargeCompareHelper.F115L030, new ChargeCompareContext(header, data), error);
    }

    static boolean shouldMoveCharges(CreateChargeFunction data) {
        return ServoFieldIndicator.isSupplied(data.getPartAmountNoIndicator())
                && data.getPartAmountNo() > 0;
    }

    private static InitialCheckRequest toContractKey(CreateChargeFunction data) {
        InitialCheckRequest request = new InitialCheckRequest();
        request.setFinInstNo(data.getFinInstNo());
        request.setContractType(data.getContractType());
        request.setContractNo(data.getContractNo());
        return request;
    }
}
