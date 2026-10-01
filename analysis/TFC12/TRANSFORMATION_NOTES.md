# Transformation notes — TFC12

**Phase 1 pilot** (2026-09-30): `FTFCK120` + `FTFCH120`  
**Phase 2** (2026-09-30): `F115L050` + K `E100` integration  
**Phase 3** (2026-09-30): `FTFCL120` core orchestration + compare-helper ports  
**Phase 4** (2026-09-30): `tfc12-persistence` I-module ports; K C520 (RULE-008–011 partial)  
**Phase 5** (2026-09-30): `ftfc-l140` shadow + boundary ports; `ftfc-fx130` F116L030; K C520 LC EXPORT/IMPORT (RULE-011)  
**Phase 6** (2026-09-30): H F791TRAC/F7918030 ports; S000 subset; `VERIFICATION.md` + `equivalence/cases.json`; `scripts/modernize-verify-tfc12.sh`  
**Harden** (2026-09-30): S000 AW/warning restore; `CentralServoErrorMessagePort`; `tfc12-boot-smoke`; `HARDENING.md`

## Scope delivered

| Legacy | Java module | Notes |
|--------|-------------|--------|
| `FTFCK120` C100/C500 (META) | `ftfc-k120` `CreateChargeK120InputControl` | RULE-004–007 |
| `FTFCK120` E100 | `ftfc-k120` `CreateChargeK120InitialCheck` | RULE-012 FM / E101 / E102 |
| `F115L050` C000 | `ftfc-l050` `InitialCheckModule` | RULE-001–003; E000 omitted (RULE-017) |
| `FTFCK120` B000, D000 stub | `ftfc-k120` `FtfcK120Module` | D/E user deps still partial |
| `FTFCH120` A000 K→L | `ftfc-h120` `FtfcH120Orchestrator` | F791TRAC/F7918030 ports; S000 AE/AS subset |
| `FTFCL120` D000/E000/G000 (slice) | `ftfc-l120` `FtfcL120Module` | RULE-014 shadow; L080/L070/L060/L030 ports |
| `F115L140` shadow | `ftfc-l140` + `L140ShadowCreationAdapter` | ICC0/IRP0/IEI0/L040 ports; IMC0 in-progress guard |
| `F116L030` FX | `ftfc-fx130` `F116L030CurrencyExchange` | Fixed-rate NOK cross subset for persona tests |
| `FTFCK120` C520 LC/COLL | `CreateChargeK120UserDependencies` | RULE-008–011 including LC EXPORT/IMPORT |

## Servo indicator semantics (verified against source)

- `*-I = 'U'` → field **supplied** in this request.
- Any other indicator → **unchanged**; mandatory C100 fails when unchanged.

## META vs user sections

- Generated C100/C500 translated literally; C520 user ILC/COLL/LC rules in `CreateChargeK120UserDependencies`.
- No edits under `legacy/`.

## Open from brief §7

- R400CH02 full header mapping still partial (`StgHeader` subset).
- Spring Boot deployable app not added — library modules only (default).
