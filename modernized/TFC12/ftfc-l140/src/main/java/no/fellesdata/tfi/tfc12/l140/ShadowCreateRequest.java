package no.fellesdata.tfi.tfc12.l140;

import no.fellesdata.tfi.tfc12.persistence.ContractKey;

public record ShadowCreateRequest(
        String function,
        String medium,
        ContractKey contractKey,
        boolean createShadow) {
}
