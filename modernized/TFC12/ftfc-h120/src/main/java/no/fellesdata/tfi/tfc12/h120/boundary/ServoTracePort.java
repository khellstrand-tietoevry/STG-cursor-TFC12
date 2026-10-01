package no.fellesdata.tfi.tfc12.h120.boundary;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;

/** F791TRAC — control trace for H-module trace points (X999-Control-Trace). */
public interface ServoTracePort {

    void trace(H120TracePoint tracePoint, StgHeader header, CreateChargeFunction data);
}
