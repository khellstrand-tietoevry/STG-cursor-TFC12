package no.fellesdata.tfi.tfc12.k120;

import no.fellesdata.tfi.tfc12.envelope.ChargeTableRow;
import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.ServoFieldIndicator;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateChargeK120InputControlTest {

    private CreateChargeK120InputControl control;
    private StgHeader header;
    private ServoErrorArea error;

    @BeforeEach
    void setUp() {
        control = new CreateChargeK120InputControl();
        header = new StgHeader();
        error = new ServoErrorArea();
    }

    @Test
    void rule004_rejectsMissingMandatoryFinInstNo() {
        CreateChargeFunction data = validEnvelope();
        data.setFinInstNoIndicator(' ');

        control.runInputControl(header, data, error);

        assertTrue(error.isReturnError());
        assertEquals("GL-ANGI-FELT&", header.getMessageCode());
        assertEquals("Fin-Inst-No", header.getMessageData(0));
        assertEquals('F', data.getFinInstNoOutput());
    }

    @Test
    void rule005_chargeTypeRequiresSelected() {
        CreateChargeFunction data = validEnvelope();
        data.setChargeTypeActiveCount(1);
        ChargeTableRow row = data.getChargeRow(0);
        row.setChargeTypeIndicator(ServoFieldIndicator.SUPPLIED);
        row.setChargeType("PRES-BANK-CHARGES");
        row.setSelectedIndicator(' ');

        control.runInputControl(header, data, error);

        assertEquals("TF-SY-FIELD2-AND-FIELD1", header.getMessageCode());
        assertEquals("Valgt", header.getMessageData(1));
    }

    @Test
    void rule006_chargeTypeRequiresAmountWhenTypeSupplied() {
        CreateChargeFunction data = validEnvelope();
        data.setChargeTypeActiveCount(1);
        ChargeTableRow row = data.getChargeRow(0);
        row.setChargeTypeIndicator(ServoFieldIndicator.SUPPLIED);
        row.setChargeType("FEE");
        row.setSelectedIndicator(ServoFieldIndicator.SUPPLIED);
        row.setSelected("Y");
        row.setChargeAmountIndicator(' ');
        row.setPercentCommissionIndicator(ServoFieldIndicator.SUPPLIED);
        row.setPercentCommission("1,000");

        control.runInputControl(header, data, error);

        assertEquals("TF-SY-FIELD2-AND-FIELD1", header.getMessageCode());
        assertEquals("Beløp", header.getMessageData(1));
    }

    @Test
    void rule006_chargeTypeRequiresPercentWhenTypeSupplied() {
        CreateChargeFunction data = validEnvelope();
        data.setChargeTypeActiveCount(1);
        ChargeTableRow row = data.getChargeRow(0);
        row.setChargeTypeIndicator(ServoFieldIndicator.SUPPLIED);
        row.setChargeType("FEE");
        row.setSelectedIndicator(ServoFieldIndicator.SUPPLIED);
        row.setSelected("Y");
        row.setChargeAmountIndicator(ServoFieldIndicator.SUPPLIED);
        row.setChargeAmount("100,000");
        row.setPercentCommissionIndicator(' ');
        row.setPayingPartyIndicator(ServoFieldIndicator.SUPPLIED);
        row.setPayingParty("CUSTM");

        control.runInputControl(header, data, error);

        assertEquals("Prosent-Provisjon", header.getMessageData(1));
    }

    @Test
    void rule007_chargeTypeRequiresPayingParty() {
        CreateChargeFunction data = validEnvelope();
        data.setChargeTypeActiveCount(1);
        ChargeTableRow row = data.getChargeRow(0);
        row.setChargeTypeIndicator(ServoFieldIndicator.SUPPLIED);
        row.setChargeType("FEE");
        row.setSelectedIndicator(ServoFieldIndicator.SUPPLIED);
        row.setSelected("Y");
        row.setChargeAmountIndicator(ServoFieldIndicator.SUPPLIED);
        row.setChargeAmount("100,000");
        row.setPercentCommissionIndicator(ServoFieldIndicator.SUPPLIED);
        row.setPercentCommission("0,000");
        row.setPayingPartyIndicator(' ');

        control.runInputControl(header, data, error);

        assertEquals("Betalende-Part", header.getMessageData(1));
    }

    @Test
    void validMandatoryEnvelopePassesInputControl() {
        CreateChargeFunction data = validEnvelope();
        control.runInputControl(header, data, error);
        assertTrue(error.isReturnOk());
    }

    private static CreateChargeFunction validEnvelope() {
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
        return data;
    }
}
