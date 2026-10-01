package no.fellesdata.tfi.tfc12.l050.boundary;

import no.fellesdata.tfi.tfc12.l050.InitialCheckRequest;

/** Port for F115ISR0 VALID-ST-OP relation (F100). */
public interface SysCodeRelationPort {

    enum Result {
        OK,
        INVALID_STATUS_OPERATION
    }

    Result validateStatusOperation(InitialCheckRequest request, String workflowStatus);
}
