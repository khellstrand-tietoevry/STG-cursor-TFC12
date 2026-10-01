package no.fellesdata.tfi.tfc12.l120.boundary;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.l140.FtfcL140ShadowModule;
import no.fellesdata.tfi.tfc12.l140.ShadowCreateRequest;
import no.fellesdata.tfi.tfc12.persistence.ContractKey;

public class L140ShadowCreationAdapter implements ShadowCreationPort {

    private final FtfcL140ShadowModule shadowModule;

    public L140ShadowCreationAdapter(FtfcL140ShadowModule shadowModule) {
        this.shadowModule = shadowModule;
    }

    @Override
    public void createShadow(StgHeader header, CreateChargeFunction function, ServoErrorArea error) {
        ContractKey key = new ContractKey(function.getFinInstNo(), function.getContractType(), function.getContractNo());
        ShadowCreateRequest request =
                new ShadowCreateRequest(header.getFunction(), header.getMedium(), key, true);
        shadowModule.createShadow(request, error);
    }
}
