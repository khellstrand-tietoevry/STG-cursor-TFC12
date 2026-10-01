package no.fellesdata.tfi.tfc12.persistence.copybook;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CopybookLinkageContractTest {

    private static final Path COPYBOOK_ROOT =
            Path.of("..", "..", "..", "legacy", "STG", "TFC12", "copybooks").toAbsolutePath().normalize();

    @Test
    void linkageFieldsExistInLegacyCopybooks() throws Exception {
        for (CopybookLinkageContract contract : CopybookLinkageContract.values()) {
            Path copybook = COPYBOOK_ROOT.resolve(contract.name() + ".copy");
            String text = Files.readString(copybook, Charset.forName("ISO-8859-1"));
            assertTrue(text.contains(contract.versionNo()), contract.name() + " version");
            for (String field : contract.linkageFields()) {
                assertTrue(text.contains(field), contract.name() + " missing " + field);
            }
        }
    }

    @Test
    void allTargetCopybooksDocumented() {
        assertEquals(7, CopybookLinkageContract.values().length);
    }
}
