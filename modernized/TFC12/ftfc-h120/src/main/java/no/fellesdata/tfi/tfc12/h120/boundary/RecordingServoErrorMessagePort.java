package no.fellesdata.tfi.tfc12.h120.boundary;

import no.fellesdata.tfi.tfc12.envelope.StgHeader;

import java.util.ArrayList;
import java.util.List;

public class RecordingServoErrorMessagePort implements ServoErrorMessagePort {

    public record Publication(String messageCode, String messageText, boolean logYes) {}

    private final List<Publication> publications = new ArrayList<>();

    @Override
    public void publish(StgHeader header, boolean logYes) {
        publications.add(new Publication(header.getMessageCode(), header.getMessageText(), logYes));
    }

    public List<Publication> publications() {
        return List.copyOf(publications);
    }
}
