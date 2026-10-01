package no.fellesdata.tfi.tfc12.l120.compare;

import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;

public interface ChargeCompareHelperPort {

    void invoke(ChargeCompareHelper helper, ChargeCompareContext context, ServoErrorArea error);
}
