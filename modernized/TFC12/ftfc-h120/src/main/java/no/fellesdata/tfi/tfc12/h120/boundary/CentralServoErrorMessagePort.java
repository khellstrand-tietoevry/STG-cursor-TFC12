package no.fellesdata.tfi.tfc12.h120.boundary;

import no.fellesdata.tfi.tfc12.envelope.StgHeader;

/**
 * F7918030 fake: applies central out-message rewrite (Z000 subset for local hardening).
 */
public class CentralServoErrorMessagePort implements ServoErrorMessagePort {

    @Override
    public void publish(StgHeader header, boolean logYes) {
        if (header.getMessageCode() == null) {
            return;
        }
        if (header.getMessageText() == null || header.getMessageText().isBlank()) {
            header.setMessageText("FTFCH120");
        }
        if ("FM".equals(header.getMessageCode())) {
            header.setMessageText("FTFCH120");
        }
    }
}
