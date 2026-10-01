# Verification — TFC12 modernized transform

**Date:** 2026-09-30  
**Command:** `/modernize-verify TFC12` → `modernized/TFC12/scripts/modernize-verify-tfc12.sh`  
**Evidence:** JUnit 5 characterization tests under `modernized/TFC12/` (no local COBOL dual-run; see [`TOOLCHAIN.md`](TOOLCHAIN.md)).

## Gate result

| Check | Status |
|-------|--------|
| `cd modernized/TFC12 && mvn test` | Required green |
| P0 rules characterized | See table below |
| Equivalence inventory | [`equivalence/cases.json`](equivalence/cases.json) |
| H boundary ports F791TRAC / F7918030 | `ftfc-h120` `ServoTracePort`, `ServoErrorMessagePort` |

## P0 behavior contract

| Rule | Status | Automated evidence |
|------|--------|-------------------|
| RULE-001 | **PROVEN** | `InitialCheckModuleTest#rule001_abendsWhenFunctionOrMediumMissing` |
| RULE-002 | **PROVEN** | `InitialCheckModuleTest#rule002_invalidStatusOperationYieldsError` |
| RULE-003 | **PROVEN** | `InitialCheckModuleTest#rule003_permanentStatusBlocksInProgressContract` |
| RULE-004 | **PROVEN** | `CreateChargeK120InputControlTest#rule004_rejectsMissingMandatoryFinInstNo` |
| RULE-005 | **PROVEN** | `CreateChargeK120InputControlTest#rule005_chargeTypeRequiresSelected` |
| RULE-006 | **PROVEN** | `CreateChargeK120InputControlTest#rule006_*` |
| RULE-007 | **PROVEN** | `CreateChargeK120InputControlTest#rule007_chargeTypeRequiresPayingParty` |
| RULE-008 | **PROVEN** | `CreateChargeK120UserDependenciesTest#rule008_mixedReturnPayingPartiesRejected` |
| RULE-009 | **PROVEN** | `CreateChargeK120UserDependenciesTest#rule009_settleIlcPresBankChargesMustBeCustm` |
| RULE-010 | **PROVEN** | `CreateChargeK120UserDependenciesTest#rule010_acceptIlcRequiresCustm` |
| RULE-011 | **PROVEN** | `CreateChargeK120UserDependenciesTest#rule011_*` (incl. LC EXPORT/IMPORT, COLL) |
| RULE-012 | **PROVEN** | `CreateChargeK120InitialCheckTest#rule012_*` |
| RULE-015 | **PROVEN** | `FtfcH120OrchestratorTest#rule015_*` |
| RULE-016 | **PROVEN** | `DecimalCommaSupportTest#rule016_formatsChargeAmountWithDecimalComma` |

## P1 / P2 (documented, not P0 gate)

| Rule | Status | Notes |
|------|--------|-------|
| RULE-013 | **PROVEN** (P1) | `InitialCheckModuleTest`, `FtfcL120ModuleTest#rule013_*` |
| RULE-014 | **PROVEN** (P1) | `FtfcL120ModuleTest`, `L120L140IntegrationTest` |
| RULE-017 | **PROVEN** (P2, confirmed) | `InitialCheckModuleTest#rule017_updateTimestampCheckNotInvokedOnMainPath` |

## Documented gaps (non-P0 / partial transform scope)

| Area | Gap | Risk |
|------|-----|------|
| FTFCH120 S000 | K **Return-Warning** / AW restore after L | **Hardened** — see [`HARDENING.md`](HARDENING.md) |
| F7918030 | Out-message rewrite from central service | **Hardened** — `CentralServoErrorMessagePort` (local fake) |
| F115L140 | Full ~2.9k LOC shadow graph vs create-shadow slice | External ICC0/IRP0/IEI0/L040 are recording/no-op ports |
| FTFCL120 | L calc/switch paragraphs beyond D000/E000/G000 slice | Charge amount persistence not end-to-end |
| R400CH02 | Full STG header copybook mapping | [`MODERNIZATION_BRIEF.md`](MODERNIZATION_BRIEF.md) §7 open |
| DB2 / Oracle | I-modules use in-memory repository, not production SQL | Phase 4 happy-path only |

## Phase 6 exit

All **P0** rules in [`MODERNIZATION_BRIEF.md`](MODERNIZATION_BRIEF.md) §5 are **PROVEN** via automated tests. Gaps above are **PARTLY PROVEN** at system level only where noted; they do not block the P0 characterization gate.
