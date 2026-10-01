package no.fellesdata.tfi.tfc12.persistence;

import java.util.Optional;

/** F115ISC0 — sys code SELECT. */
public interface SysCodeRepository {

    Optional<String> selectStatusProperty(String finInstNo, String statusCode);

    Optional<ContractTypeSysCodeRecord> selectContractType(String finInstNo, String contractTypeCode);
}
