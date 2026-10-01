package no.fellesdata.tfi.tfc12.h120.boundary;

import no.fellesdata.tfi.tfc12.envelope.StgHeader;

/** F7918030 — publish header message to central error/logging (Z000-ErrorMessage). */
public interface ServoErrorMessagePort {

    void publish(StgHeader header, boolean logYes);
}
