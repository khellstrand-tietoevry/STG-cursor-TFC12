package no.fellesdata.tfi.tfc12.smoke;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.ServoFieldIndicator;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.h120.FtfcH120Orchestrator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CreateChargeSmokeController {

    private final FtfcH120Orchestrator orchestrator;

    public CreateChargeSmokeController(FtfcH120Orchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @GetMapping("/tfc12/smoke/ping")
    Map<String, String> ping() {
        return Map.of("system", "TFC12", "status", "up");
    }

    @PostMapping("/tfc12/smoke/create-charge")
    ResponseEntity<Map<String, String>> createChargeSmoke() {
        StgHeader header = new StgHeader();
        header.setFunction("TFC1201");
        header.setMedium("01");
        CreateChargeFunction data = new CreateChargeFunction();
        data.setFinInstNoIndicator(ServoFieldIndicator.SUPPLIED);
        data.setFinInstNo("123456789012345678");
        data.setContractTypeIndicator(ServoFieldIndicator.SUPPLIED);
        data.setContractType("LC");
        data.setContractNoIndicator(ServoFieldIndicator.SUPPLIED);
        data.setContractNo("0001234");
        data.setLastSavedDateIndicator(ServoFieldIndicator.SUPPLIED);
        data.setLastSavedDate("2026-09-30T12:00:00.000000");
        data.setOperationTypeIndicator(ServoFieldIndicator.SUPPLIED);
        data.setOperationType("CREATE");
        ServoErrorArea error = new ServoErrorArea();
        orchestrator.execute(header, data, error);
        return ResponseEntity.ok(Map.of(
                "headerStatus", nullToEmpty(header.getStatusCode()),
                "returnKind", error.getReturnKind().name(),
                "messageCode", nullToEmpty(header.getMessageCode())));
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
