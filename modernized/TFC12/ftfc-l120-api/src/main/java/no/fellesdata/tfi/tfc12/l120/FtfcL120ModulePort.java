package no.fellesdata.tfi.tfc12.l120;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;

public interface FtfcL120ModulePort {

    void execute(StgHeader header, CreateChargeFunction data, ServoErrorArea error);
}
