package no.fellesdata.tfi.tfc12.k120;

import no.fellesdata.tfi.tfc12.envelope.ChargeTableRow;
import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.ServoFieldIndicator;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.persistence.ContractKey;
import no.fellesdata.tfi.tfc12.persistence.ContractTypeSysCodeRecord;
import no.fellesdata.tfi.tfc12.persistence.TradeFinancePersistence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateChargeK120UserDependenciesTest {

    private TradeFinancePersistence persistence;
    private CreateChargeK120UserDependencies dependencies;
    private StgHeader header;
    private ServoErrorArea error;

    @BeforeEach
    void setUp() {
        persistence = TradeFinancePersistence.inMemory();
        ContractKey key = new ContractKey("123456789012345678", "LC", "0001234");
        persistence.seedStandardFixtures(key);
        dependencies = new CreateChargeK120UserDependencies(persistence.sysCodes());
        header = new StgHeader();
        error = new ServoErrorArea();
    }

    @Test
    void rule008_mixedReturnPayingPartiesRejected() {
        CreateChargeFunction data = baseData();
        data.setOperationTypeIndicator(ServoFieldIndicator.SUPPLIED);
        data.setOperationType("RETURN");
        data.setChargeTypeActiveCount(2);
        chargeRow(data, 0, "CUSTM");
        chargeRow(data, 1, "BENEF");

        try {
            dependencies.runUserDependencies(header, data, error);
        } catch (K120ValidationException ignored) {
        }

        assertTrue(error.isReturnError());
        assertEquals("TF-SY-OP-RETURN-PAY-PTY", header.getMessageCode());
    }

    @Test
    void rule009_settleIlcPresBankChargesMustBeCustm() {
        CreateChargeFunction data = baseData();
        data.setOperationType("SETTLE-ILC");
        data.setChargeTypeActiveCount(1);
        ChargeTableRow row = data.getChargeRow(0);
        row.setChargeType("PRES-BANK-CHARGES");
        row.setPayingParty("BENEF");

        try {
            dependencies.runUserDependencies(header, data, error);
        } catch (K120ValidationException ignored) {
        }

        assertEquals("TF-SY-ROLE-VAR1-CH-VAR2-INV", header.getMessageCode());
    }

    @Test
    void rule010_acceptIlcRequiresCustm() {
        CreateChargeFunction data = baseData();
        data.setOperationType("ACCEPT-ILC");
        data.setChargeTypeActiveCount(1);
        data.getChargeRow(0).setPayingParty("BENEF");

        try {
            dependencies.runUserDependencies(header, data, error);
        } catch (K120ValidationException ignored) {
        }

        assertEquals("TF-SY-ROLE-VAR1-CH-VAR2-INV", header.getMessageCode());
    }

    @Test
    void rule011_selectedChargeRequiresPayingPartyIndicator() {
        CreateChargeFunction data = baseData();
        data.setChargeTypeActiveCount(1);
        ChargeTableRow row = data.getChargeRow(0);
        row.setSelected("Y");
        row.setPayingPartyIndicator(' ');
        row.setPayingParty("CUSTM");

        try {
            dependencies.runUserDependencies(header, data, error);
        } catch (K120ValidationException ignored) {
        }

        assertEquals("TF-SY-TYPE-MISSING", header.getMessageCode());
    }

    @Test
    void rule011_lcExportSelectedChargeRequiresBenefOrIssui() {
        persistence.store()
                .putContractType(
                        "123456789012345678",
                        "LC",
                        new ContractTypeSysCodeRecord("LC", "EXPORT", "WORKFLOW"));

        CreateChargeFunction data = baseData();
        data.setChargeTypeActiveCount(1);
        ChargeTableRow row = data.getChargeRow(0);
        row.setSelected("Y");
        row.setSelectedIndicator(ServoFieldIndicator.SUPPLIED);
        row.setPayingPartyIndicator(ServoFieldIndicator.SUPPLIED);
        row.setPayingParty("CUSTM");

        try {
            dependencies.runUserDependencies(header, data, error);
        } catch (K120ValidationException ignored) {
        }

        assertEquals("TF-SY-PAY-PTY-VAR1-VAR2", header.getMessageCode());
        assertEquals("BENEF", header.getMessageData(0));
        assertEquals("ISSUI", header.getMessageData(1));
    }

    @Test
    void rule011_lcImportSelectedChargeRequiresBenefOrCustm() {
        CreateChargeFunction data = baseData();
        data.setChargeTypeActiveCount(1);
        ChargeTableRow row = data.getChargeRow(0);
        row.setSelected("Y");
        row.setSelectedIndicator(ServoFieldIndicator.SUPPLIED);
        row.setPayingPartyIndicator(ServoFieldIndicator.SUPPLIED);
        row.setPayingParty("DRWEE");

        try {
            dependencies.runUserDependencies(header, data, error);
        } catch (K120ValidationException ignored) {
        }

        assertEquals("TF-SY-PAY-PTY-VAR1-VAR2", header.getMessageCode());
        assertEquals("BENEF", header.getMessageData(0));
        assertEquals("CUSTM", header.getMessageData(1));
    }

    @Test
    void rule011_collContractTypeUsesIsc0Fake() {
        persistence.store()
                .putContractType(
                        "123456789012345678",
                        "COLL",
                        new ContractTypeSysCodeRecord("COLL", "IMPORT", "WORKFLOW"));

        CreateChargeFunction data = baseData();
        data.setContractType("COLL");
        data.setChargeTypeActiveCount(1);
        ChargeTableRow row = data.getChargeRow(0);
        row.setSelected("Y");
        row.setSelectedIndicator(ServoFieldIndicator.SUPPLIED);
        row.setPayingPartyIndicator(ServoFieldIndicator.SUPPLIED);
        row.setPayingParty("BENEF");
        row.setChargeType("FEE");

        try {
            dependencies.runUserDependencies(header, data, error);
        } catch (K120ValidationException ignored) {
        }

        assertEquals("TF-SY-PAY-PTY-VAR1-VAR2", header.getMessageCode());
    }

    private static CreateChargeFunction baseData() {
        CreateChargeFunction data = new CreateChargeFunction();
        data.setFinInstNo("123456789012345678");
        data.setContractType("LC");
        data.setContractNo("0001234");
        return data;
    }

    private static void chargeRow(CreateChargeFunction data, int index, String payingParty) {
        ChargeTableRow row = data.getChargeRow(index);
        row.setPayingPartyIndicator(ServoFieldIndicator.SUPPLIED);
        row.setPayingParty(payingParty);
    }
}
