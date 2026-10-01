# Hardening — TFC12

**Date:** 2026-09-30  
**Command:** `/modernize-harden TFC12` → `modernized/TFC12/scripts/modernize-harden-tfc12.sh`

## Scope delivered

| Gap (from [`VERIFICATION.md`](VERIFICATION.md)) | Hardening action |
|------------------------------------------------|------------------|
| FTFCH120 S000 K **Return-Warning** / AW restore | `H120StatusCodeHandler` — AW on K warning, restore K message after L OK; warning-goback stops before L |
| F7918030 out-message rewrite | `CentralServoErrorMessagePort` + `H120BoundaryPorts.hardened()` |
| Spring Boot embedding (brief §7 default) | `tfc12-boot-smoke` — actuator health, `/tfc12/smoke/*` H→K→L smoke |
| Servo error area | `ServoErrorArea` — `setReturnWarning` / `setReturnWarningGoback` |

## Tests added

- `H120StatusCodeHandlerTest` — AW restore, warning-goback, central error rewrite  
- `FtfcH120OrchestratorTest#hardenedFlow_kWarningRunsLAndRestoresAwStatus`  
- `Tfc12SmokeApplicationTest` — Spring context + HTTP smoke endpoints  

## Remaining gaps (unchanged)

- Full **F115L140** shadow graph vs slice  
- **FTFCL120** charge calc / persistence beyond current slice  
- **R400CH02** full STG header copybook  
- **Oracle/DB2** production I-module SQL (in-memory only)  

## Run locally

```bash
cd modernized/TFC12
./scripts/modernize-harden-tfc12.sh
./scripts/run-boot-smoke.sh   # builds ftfc-h120 etc. (-am), then spring-boot:run
```
