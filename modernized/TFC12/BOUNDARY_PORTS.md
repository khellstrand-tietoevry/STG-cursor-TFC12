# Boundary ports — TFC12 modernized stack

Java ports replace in-repo **F115I*** COBOL database modules for local happy-path execution. External variants (`*AR`, `*AU`, `*CR`) remain out of scope until Oracle adapters land in a later phase.

## Persistence (`tfc12-persistence`)

| Legacy | Port | Copybook linkage |
|--------|------|------------------|
| F115IMC0 | `MainContractRepository` | `R115IMC0` |
| F115ICA0 | `ContractAmountRepository` | `R115ICA0` |
| F115ICH0 | `MainChargeRepository` | `R115ICH0` |
| F115IPA0 | `PartAmountRepository` | `R115IPA0` |
| F115IPC0 | `PartChargeRepository` | `R115IPC0` |
| F115ISC0 | `SysCodeRepository` | `R115ISC0` |
| F115ISR0 | `SysCodeRelationRepository` | `R115ISR0` |

## H-module runtime (`ftfc-h120`)

| Legacy | Port | Notes |
|--------|------|--------|
| F791TRAC | `ServoTracePort` | Trace points A000–A003, C000–C001, D000–D001 when `StgHeader.traceOn` |
| F7918030 | `ServoErrorMessagePort` | Z000-ErrorMessage on AE/AS from S000 subset |

## Adapters to H/K/L modules

| Consumer | Port source |
|----------|-------------|
| F115L050 / `ftfc-l050` | `MainContractPort`, `SysCodeRelationPort`, `SysCodeLookupPort`, `ContractAmountPort`, `PartAmountPort` via `TradeFinancePersistence` |
| FTFCK120 C520 | `ContractTypeSysCodeRepository` (F115ISC0 CONTRACT-TYPE read) |
| FTFCL120 G030 | `MainContractRepository` via `MainContractPortAdapter` |

## Shadow orchestration (`ftfc-l140`)

| Legacy | Port | Notes |
|--------|------|--------|
| F115ICC0 | `ContractCollectionPort` | ACTUAL → WORK contract collection copy |
| F115IRP0 | `RelatedPartyPort` | Related-party shadow replication |
| F115IEI0 | `EventInstrumentPort` | Event/instrument index shadow copy |
| F115L040 | `ShadowTierListPort` | Shadow tier list maintenance |
| F115IMC0 | `MainContractRepository` | In-progress guard + WORK upsert (via L140 module) |

`L140ShadowCreationAdapter` implements L120 `ShadowCreationPort` and delegates to `FtfcL140ShadowModule`.

## FX (`ftfc-fx130`)

| Legacy | Java | Notes |
|--------|------|--------|
| F116L030 | `F116L030CurrencyExchange` | Cross-currency conversion subset for treasury persona tests |

## Default local runtime

`TradeFinancePersistence.inMemory()` — thread-safe in-memory store with CRUD used by contract tests and module defaults.
