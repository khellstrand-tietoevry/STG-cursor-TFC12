package no.fellesdata.tfi.tfc12.persistence;

/** F115ISR0 — VALID-ST-OP / VALID-OP-ST relations. */
public interface SysCodeRelationRepository {

    boolean isValidStatusOperation(
            String finInstNo, String fromStatus, String operationType, String toStatusWildcard);

    boolean isValidOperationStatus(String finInstNo, String operationType, String toStatusWildcard);
}
