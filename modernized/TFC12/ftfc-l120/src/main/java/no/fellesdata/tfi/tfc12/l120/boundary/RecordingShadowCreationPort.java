package no.fellesdata.tfi.tfc12.l120.boundary;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;

import java.util.concurrent.atomic.AtomicBoolean;

public class RecordingShadowCreationPort implements ShadowCreationPort {

    private final AtomicBoolean invoked = new AtomicBoolean();

    @Override
    public void createShadow(StgHeader header, CreateChargeFunction function, ServoErrorArea error) {
        invoked.set(true);
    }

    public boolean wasInvoked() {
        return invoked.get();
    }
}
