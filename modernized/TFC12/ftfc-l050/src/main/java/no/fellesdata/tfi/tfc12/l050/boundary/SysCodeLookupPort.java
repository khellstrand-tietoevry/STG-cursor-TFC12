package no.fellesdata.tfi.tfc12.l050.boundary;

/** Port for F115ISC0 status property read (F200). */
public interface SysCodeLookupPort {

    String readStatusProperty(String finInstNo, String statusCode);
}
