package no.fellesdata.tfi.tfc12.smoke;

import no.fellesdata.tfi.tfc12.h120.FtfcH120Orchestrator;
import no.fellesdata.tfi.tfc12.h120.H120BoundaryPorts;
import no.fellesdata.tfi.tfc12.h120.H120Modules;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Tfc12SmokeApplication {

    public static void main(String[] args) {
        SpringApplication.run(Tfc12SmokeApplication.class, args);
    }

    @Bean
    FtfcH120Orchestrator createChargeOrchestrator() {
        return H120Modules.defaultStack(
                no.fellesdata.tfi.tfc12.k120.K120Modules.seededPersistence(), H120BoundaryPorts.hardened());
    }
}
