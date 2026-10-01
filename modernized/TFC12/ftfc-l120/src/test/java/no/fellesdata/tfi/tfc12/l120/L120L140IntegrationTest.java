package no.fellesdata.tfi.tfc12.l120;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
import no.fellesdata.tfi.tfc12.l120.persistence.L120PersistencePorts;
import no.fellesdata.tfi.tfc12.persistence.ContractKey;
import no.fellesdata.tfi.tfc12.persistence.TradeFinancePersistence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class L120L140IntegrationTest {

    @Test
    void rule014_defaultStackUsesL140ShadowAndImc0() {
        TradeFinancePersistence persistence = TradeFinancePersistence.inMemory();
        ContractKey key = new ContractKey("123456789012345678", "LC", "0001234");
        persistence.seedStandardFixtures(key);

        FtfcL120Module module = L120PersistencePorts.permissiveDefaults(persistence);
        module.execute(header(), function(key), new ServoErrorArea());

        assertTrue(
                persistence.mainContracts().selectWork(key).orElseThrow().inProgress(),
                "L140 shadow should upsert WORK via IMC0 port");
    }

    private static StgHeader header() {
        StgHeader header = new StgHeader();
        header.setFunction("TFC12");
        header.setMedium("01");
        return header;
    }

    private static CreateChargeFunction function(ContractKey key) {
        CreateChargeFunction data = new CreateChargeFunction();
        data.setFinInstNo(key.finInstNo());
        data.setContractType(key.contractType());
        data.setContractNo(key.contractNo());
        return data;
    }
}
