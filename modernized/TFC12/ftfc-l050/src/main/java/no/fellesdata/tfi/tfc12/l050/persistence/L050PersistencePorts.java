package no.fellesdata.tfi.tfc12.l050.persistence;

import no.fellesdata.tfi.tfc12.l050.InitialCheckModule;
import no.fellesdata.tfi.tfc12.l050.InitialCheckRequest;
import no.fellesdata.tfi.tfc12.l050.boundary.ContractAmountPort;
import no.fellesdata.tfi.tfc12.l050.boundary.MainContractPort;
import no.fellesdata.tfi.tfc12.l050.boundary.MainContractSnapshot;
import no.fellesdata.tfi.tfc12.l050.boundary.PartAmountPort;
import no.fellesdata.tfi.tfc12.l050.boundary.SysCodeLookupPort;
import no.fellesdata.tfi.tfc12.l050.boundary.SysCodeRelationPort;
import no.fellesdata.tfi.tfc12.persistence.ContractKey;
import no.fellesdata.tfi.tfc12.persistence.MainContractRecord;
import no.fellesdata.tfi.tfc12.persistence.TradeFinancePersistence;

public final class L050PersistencePorts {

    private L050PersistencePorts() {
    }

    public static InitialCheckModule initialCheckModule(TradeFinancePersistence persistence) {
        return new InitialCheckModule(
                mainContractPort(persistence),
                sysCodeRelationPort(persistence),
                sysCodeLookupPort(persistence),
                contractAmountPort(persistence),
                partAmountPort(persistence));
    }

    public static MainContractPort mainContractPort(TradeFinancePersistence persistence) {
        return (request, tableVersion) -> {
            ContractKey key = toKey(request);
            MainContractRecord record = "WORK".equals(tableVersion)
                    ? persistence.mainContracts().selectWork(key).orElse(null)
                    : persistence.mainContracts().selectActual(key).orElse(null);
            if (record == null) {
                return MainContractSnapshot.active("ACTIVE", false);
            }
            return new MainContractSnapshot(record.status(), record.inProgress(), record.updateTimestamp());
        };
    }

    private static SysCodeRelationPort sysCodeRelationPort(TradeFinancePersistence persistence) {
        return (request, workflowStatus) ->
                persistence.sysCodeRelations()
                                .isValidStatusOperation(
                                        request.getFinInstNo(),
                                        workflowStatus,
                                        request.getOperationType(),
                                        workflowStatus)
                        ? SysCodeRelationPort.Result.OK
                        : SysCodeRelationPort.Result.INVALID_STATUS_OPERATION;
    }

    private static SysCodeLookupPort sysCodeLookupPort(TradeFinancePersistence persistence) {
        return (finInstNo, statusCode) ->
                persistence.sysCodes()
                        .selectStatusProperty(finInstNo, statusCode)
                        .orElse("WORKFLOW");
    }

    private static ContractAmountPort contractAmountPort(TradeFinancePersistence persistence) {
        return (request, tableVersion) ->
                persistence.contractAmounts()
                        .selectCurrency(toKey(request), tableVersion)
                        .orElse("NOK");
    }

    private static PartAmountPort partAmountPort(TradeFinancePersistence persistence) {
        return (request, tableVersion, currency) ->
                persistence.partAmounts()
                        .selectStatus(toKey(request), request.getPartAmountSeqNo(), tableVersion)
                        .orElse("ACTIVE");
    }

    private static ContractKey toKey(InitialCheckRequest request) {
        return new ContractKey(request.getFinInstNo(), request.getContractType(), request.getContractNo());
    }
}
