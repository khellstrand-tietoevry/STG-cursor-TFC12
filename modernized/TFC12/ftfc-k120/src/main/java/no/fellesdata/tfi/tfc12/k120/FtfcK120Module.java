package no.fellesdata.tfi.tfc12.k120;

import no.fellesdata.tfi.tfc12.envelope.CreateChargeFunction;
import no.fellesdata.tfi.tfc12.envelope.ServoErrorArea;
import no.fellesdata.tfi.tfc12.envelope.StgHeader;
/**
 * FTFCK120 control module — B000, C000, D000 stub, E000 initial checks.
 */
public class FtfcK120Module {

    private final CreateChargeK120InputControl inputControl;
    private final CreateChargeK120InitialCheck initialCheck;

    public FtfcK120Module() {
        this(K120Modules.seededPersistence());
    }

    FtfcK120Module(no.fellesdata.tfi.tfc12.persistence.TradeFinancePersistence persistence) {
        this(
                new CreateChargeK120InputControl(new CreateChargeK120UserDependencies(persistence.sysCodes())),
                new CreateChargeK120InitialCheck(
                        no.fellesdata.tfi.tfc12.l050.persistence.L050PersistencePorts.initialCheckModule(
                                persistence)));
    }

    public FtfcK120Module(CreateChargeK120InitialCheck initialCheck) {
        this(new CreateChargeK120InputControl(), initialCheck);
    }

    public FtfcK120Module(CreateChargeK120InputControl inputControl, CreateChargeK120InitialCheck initialCheck) {
        this.inputControl = inputControl;
        this.initialCheck = initialCheck;
    }

    public void execute(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        initialize(error);
        inputControl.runInputControl(header, data, error);
        if (error.isReturnError()) {
            return;
        }
        prepareDbRead();
        controlBase(header, data, error);
    }

    CreateChargeK120InitialCheck initialCheck() {
        return initialCheck;
    }

    private void initialize(ServoErrorArea error) {
        error.setReturnOk();
    }

    private void prepareDbRead() {
        // D000-Prepare-DB-Read — empty in legacy
    }

    private void controlBase(StgHeader header, CreateChargeFunction data, ServoErrorArea error) {
        initialCheck.runInitialChecks(header, data, error);
    }
}
