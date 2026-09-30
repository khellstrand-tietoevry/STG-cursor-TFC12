# Toolchain — TFC12 (Check 3 closed, option A)

**Date:** 2026-09-30  
**Target stack:** Java 21 / Spring Boot (transform track)

## Legacy COBOL — Level 2 N/A

Local compilation of mainframe COBOL in this repo is **not required** and **not expected to succeed** on GnuCOBOL:

- **Boundary COPY** — members listed in [`BOUNDARY_CATALOG.md`](BOUNDARY_CATALOG.md) are intentional ports, not build inputs.
- **EXEC SQL / DB2** — H/K/L and I-modules use mainframe SQL includes; no DB2 precompiler or stub layer is maintained here.

`cobc` (GnuCOBOL 3.2) may be present on the host for ad hoc inspection; preflight does **not** treat a full `cobc` compile as a gate.

**Legacy equivalence strategy:** characterization from source, call/COPY graph, rules extraction, and (when available) recorded native traces — not dual local COBOL execution in this workspace.

## Target stack — verified on host

| Tool | Version | Smoke test |
|------|---------|------------|
| OpenJDK | 21.0.10 | Minimal Maven project `mvn -q -DskipTests package` (2026-09-30) |
| Apache Maven | 3.9.11 | Same |

Once `modernized/TFC12/` exists, the project gate is **`mvn test`** under that reactor (per modernization brief).

## Preflight Check 3 status

**Closed (option A):** Target toolchain proven; legacy mainframe compile explicitly out of scope. See [`PREFLIGHT.md`](PREFLIGHT.md).
