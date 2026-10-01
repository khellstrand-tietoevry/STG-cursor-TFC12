package no.fellesdata.tfi.tfc12.k120;

import no.fellesdata.tfi.tfc12.envelope.ChargeTableRow;
import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.ServoFieldIndicator;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.persistence.ContractTypeSysCodeRecord;
import no.fellesdata.tfi.tfc12.persistence.SysCodeRepository;

/**
 * FTFCK120 C520-Dependencies-UA (RULE-008–011) using F115ISC0 contract-type port.
 */
public class CreateChargeK120UserDependencies {

    private final SysCodeRepository sysCodeRepository;

    public CreateChargeK120UserDependencies(SysCodeRepository sysCodeRepository) {
        this.sysCodeRepository = sysCodeRepository;
    }

    public void runUserDependencies(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        validateReturnExpirePayingParties(header, data, error);
        if (!error.isReturnOk()) {
            throw new K120ValidationException();
        }
        validateSettleIlcPresBankCharges(header, data, error);
        if (!error.isReturnOk()) {
            throw new K120ValidationException();
        }
        validateAcceptIlcPayingParty(header, data, error);
        if (!error.isReturnOk()) {
            throw new K120ValidationException();
        }
        validateSelectedChargeRules(header, data, error);
        if (!error.isReturnOk()) {
            throw new K120ValidationException();
        }
    }

    private void validateReturnExpirePayingParties(
            StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        if (!ServoFieldIndicator.isSupplied(data.getOperationTypeIndicator())) {
            return;
        }
        String op = data.getOperationType();
        if (op == null || (!"RETURN".equals(op) && !op.startsWith("EXPIRE-"))) {
            return;
        }
        int buyer = 0;
        int seller = 0;
        int other = 0;
        int limit = data.getChargeTypeActiveCount();
        for (int i = 0; i < limit; i++) {
            ChargeTableRow row = data.getChargeRow(i);
            if (!ServoFieldIndicator.isSupplied(row.getPayingPartyIndicator())) {
                continue;
            }
            String party = row.getPayingParty();
            if ("DRWEE".equals(party) || "CUSTM".equals(party)) {
                buyer++;
            } else if ("DRWER".equals(party) || "BENEF".equals(party)) {
                seller++;
            } else {
                other++;
            }
        }
        if ((buyer > 0 && seller > 0) || other > 0) {
            header.setMessageCode("TF-SY-OP-RETURN-PAY-PTY");
            raiseAe(header, error);
        }
    }

    private void validateSettleIlcPresBankCharges(
            StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        if (!"SETTLE-ILC".equals(data.getOperationType())) {
            return;
        }
        int limit = data.getChargeTypeActiveCount();
        for (int i = 0; i < limit; i++) {
            ChargeTableRow row = data.getChargeRow(i);
            if ("PRES-BANK-CHARGES".equals(row.getChargeType())
                    && !"CUSTM".equals(row.getPayingParty())) {
                header.setMessageCode("TF-SY-ROLE-VAR1-CH-VAR2-INV");
                header.setMessageKey(0, "VAR1");
                header.setMessageData(0, "Benefisiant");
                header.setMessageKey(1, "VAR2");
                header.setMessageData(1, "Pres bank charges");
                raiseAe(header, error);
            }
        }
    }

    private void validateAcceptIlcPayingParty(
            StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        if (!"ACCEPT-ILC".equals(data.getOperationType())) {
            return;
        }
        int limit = data.getChargeTypeActiveCount();
        for (int i = 0; i < limit; i++) {
            ChargeTableRow row = data.getChargeRow(i);
            if (!"CUSTM".equals(row.getPayingParty())) {
                header.setMessageCode("TF-SY-ROLE-VAR1-CH-VAR2-INV");
                header.setMessageKey(0, "VAR1");
                header.setMessageData(0, "Benefisiant");
                header.setMessageKey(1, "VAR2");
                header.setMessageData(1, "ACCEPT-ILC");
                raiseAe(header, error);
            }
        }
    }

    private void validateSelectedChargeRules(
            StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        ContractTypeSysCodeRecord contractType = sysCodeRepository
                .selectContractType(data.getFinInstNo(), data.getContractType())
                .orElse(new ContractTypeSysCodeRecord("LC", "IMPORT", "WORKFLOW"));

        int limit = data.getChargeTypeActiveCount();
        for (int i = 0; i < limit; i++) {
            ChargeTableRow row = data.getChargeRow(i);
            if (!"Y".equals(row.getSelected())) {
                continue;
            }
            if (!ServoFieldIndicator.isSupplied(row.getPayingPartyIndicator())) {
                header.setMessageCode("TF-SY-TYPE-MISSING");
                raiseAe(header, error);
                return;
            }
            if ("COLL".equals(contractType.contractName())) {
                if (!"DRWEE".equals(row.getPayingParty())
                        && !"DRWER".equals(row.getPayingParty())
                        && !"OTH-INTERB-CHARGES".equals(row.getChargeType())) {
                    rejectPayPartyVar1Var2(header, error, "DRWEE", "DRWER");
                    return;
                }
                continue;
            }
            if ("LC".equals(contractType.contractName()) && "EXPORT".equals(contractType.contrImpExp())) {
                if (!"BENEF".equals(row.getPayingParty()) && !"ISSUI".equals(row.getPayingParty())) {
                    rejectPayPartyVar1Var2(header, error, "BENEF", "ISSUI");
                    return;
                }
                continue;
            }
            if ("LC".equals(contractType.contractName())
                    && ("IMPORT".equals(contractType.contrImpExp()) || contractType.contrImpExp() == null)) {
                if (!"BENEF".equals(row.getPayingParty()) && !"CUSTM".equals(row.getPayingParty())) {
                    rejectPayPartyVar1Var2(header, error, "BENEF", "CUSTM");
                    return;
                }
            }
        }
    }

    private void rejectPayPartyVar1Var2(
            StgHeader header, ServoErrorArea error, String var1, String var2) {
        header.setMessageCode("TF-SY-PAY-PTY-VAR1-VAR2");
        header.setMessageKey(0, "VAR1");
        header.setMessageData(0, var1);
        header.setMessageKey(1, "VAR2");
        header.setMessageData(1, var2);
        raiseAe(header, error);
    }

    private void raiseAe(StgHeader header, ServoErrorArea error) {
        error.setReturnError();
        if (!"FM".equals(header.getMessageCode())) {
            header.setMessageText("FTFCK120");
        }
        throw new K120ValidationException();
    }
}
