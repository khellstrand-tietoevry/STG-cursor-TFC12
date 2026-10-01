# Assessment — TFC12

**Date:** 2026-09-30  
**Legacy path:** `legacy/TFC12/` → `legacy/STG/TFC12/`  
**Pattern:** **Transform** → Java 21 / Spring Boot  
**Scope:** Min + one-hop closure ([`SCOPE.md`](SCOPE.md)); local COBOL compile N/A ([`TOOLCHAIN.md`](TOOLCHAIN.md))

## Executive Summary

TFC12 **Create charge / charges and pricing** (`TFC12/01` in module headers) is a **Servo H/K/L** STG flow for trade-finance **omkostninger** (charges): **FTFCH120** orchestrates **FTFCK120** (control/validation) and **FTFCL120** (logic/persistence). The in-repo corpus is **17 programs** and **19 copybooks** (~15k logical COBOL lines per `cloc`, ~21.8k physical lines in `src/`). Complexity concentrates in **F115L140** (shadow/charge orchestration, ~2.9k lines) and the **F115I*** DB helper modules (each ~1.2–1.6k lines with **EXEC SQL**). **45** external `CALL` targets and **35** unresolved `COPY` members are **documented ports** ([`BOUNDARY_CATALOG.md`](BOUNDARY_CATALOG.md)), not inventory gaps. **Recommended pattern: Transform** — phased Java modules with adapters for external CALL/COPY and trace-based equivalence (no local dual COBOL). **Next:** `/modernize-map TFC12`.

## System Inventory

| Metric | Value | Tool / note |
|--------|-------|-------------|
| Programs (`.src`) | 17 | `legacy/STG/TFC12/src/` |
| Copybooks (`.copy`) | 19 | `legacy/STG/TFC12/copybooks/` |
| COBOL code lines | 15,037 | `cloc --force-lang=COBOL` on `src` + `copybooks` |
| Comment lines | 8,761 | Fixed-format / change-history heavy |
| Physical `src` lines | 21,779 | `wc -l` (includes comment bands) |
| KSLOC (cloc code) | 15.0 | Ranking basis below |
| Complexity index | **~58** | `2.94 × KSLOC^1.10` — **relative scale only**, not schedule or cost |
| External CALL sites | 178 | `corpus-manifest.json` |
| Unresolved COPY sites | 83 | intentional boundaries per scope |

**Complexity signal (decision keywords in `src/`):** highest in `F115L140.src` (754 hits), then `FTFCL120.src` (369), `F115L060.src` (333). `scc` does not classify `.src` as COBOL without custom mapping; inventory uses **cloc + keyword ranking**.

### Technology fingerprint

| Area | Evidence |
|------|----------|
| Language | Fixed-format COBOL, `Decimal-Point Is Comma` on H/K/L |
| Runtime (legacy) | Mainframe STG, DB2 via `EXEC SQL` in all seven **F115I*** helpers in-tree |
| Envelope | `RTFCE120` (function I/O), `R400CH02` (STG header) — referenced; `R400CH02` copybook **not shipped** (boundary) |
| Build | None in-repo; target **Java 21 + Maven** on host |
| Tests | None in legacy tree |
| Integration | Literal `CALL` graph; external trace/error (`F7918030`, `F791TRAC`) from H-module |

## Architecture at a Glance

| Domain | Programs | Role |
|--------|----------|------|
| **STG orchestration** | `FTFCH120` | Calls K then L; trace/error plumbing to external modules |
| **Control / validation** | `FTFCK120` | Paying party, charge rules, **F115L050** initial check, sys-code |
| **Charge logic** | `FTFCL120` | Main vs part charges, percent/fixed, shadow, timestamps |
| **Initial / compare helpers** | `F115L030`, `F115L050`, `F115L060`, `F115L070`, `F115L080` | Timestamp, initial check, charge comparison tiers |
| **Shadow / status** | `F115L140`, `F115ISR0` | Shadow processing; L140 calls **external** ICC0/IRP0/IEI0/L040 |
| **DB I-modules** | `F115ICA0`, `F115ICH0`, `F115IMC0`, `F115IPA0`, `F115IPC0`, `F115ISC0` | Contract/main/part/charge persistence (SQL) |
| **FX** | `F116L030` | Currency exchange helper (called from L) |

Domain diagram: [`ARCHITECTURE.mmd`](ARCHITECTURE.mmd).

**Dangling references (by design):** `F115L140` calls **F115L040**, **F115ICC0**, **F115IRP0**, **F115IEI0** with no `.src` in-tree; H calls **F7918030** / **F791TRAC**; many I-module SQL includes reference COPY members in [`BOUNDARY_CATALOG.md`](BOUNDARY_CATALOG.md).

## Production Runtime Profile

No APM MCP or batch telemetry supplied ([`PREFLIGHT.md`](PREFLIGHT.md) Check 5 — APM not used). Runtime overlay **not available**; prioritize domains by static complexity (L140, L, K) and external CALL fan-out.

## Technical Debt (top 10 by remediation value)

| # | Issue | Location | Remediation hint |
|---|--------|----------|------------------|
| 1 | **God module** — shadow/charge aggregation | `legacy/STG/TFC12/src/F115L140.src` (~2,892 lines) | Split Java by paragraph sections / rules from `extract-rules` |
| 2 | **External fan-in on god module** | `F115L140.src` e.g. lines ~215, 448, 616, 779 | Ports for L040, ICC0, IRP0, IEI0 per boundary catalog |
| 3 | **Large L-module** multi-path charge calc | `FTFCL120.src` (~1,930 lines) | Phase transform: main vs part vs percent switches (`WA-Part-Main-Switch`) |
| 4 | **Duplicated I-module pattern** | `F115ICA0`–`F115IPC0` (similar CALL/SQL structure) | Shared Java repository layer with per-entity mappers |
| 5 | **EXEC SQL** not locally compilable | All `F115I*.src` (7 files) | DB boundary adapters; do not inline SQL without schema imports |
| 6 | **META-tagged “do not modify”** regions | `FTFCK120.src`, `FTFCL120.src` headers | Treat as generated/servo contract; parity over refactor |
| 7 | **Decimal comma** locale | H/K/L `Special-Names` | Preserve in Java `BigDecimal` formatting tests |
| 8 | **Hardcoded sys id** | `FTFCH120.src` ~line 30 `WC-Sysid` Value `'TF'` | Config externalization in modernized app |
| 9 | **Error/trace coupling** | `FTFCH120.src` ~lines 319, 346 | Port interfaces for F791TRAC / F7918030 |
| 10 | **Incomplete COPY closure** | H/K/L reference `R7918030`, `R7919040`, etc. | Already scoped as ports; import only if brief expands legacy |

No credentials identified in legacy sources; no `SECRETS.local.md` required.

## Security Findings

| Severity | CWE | Finding | Location |
|----------|-----|---------|----------|
| Medium | CWE-1104 | Reliance on external DB/auth modules not in repo | I-modules → external SQL/CALL boundaries |
| Medium | CWE-200 | Trace/error paths may log business fields | `FTFCH120` → `F791TRAC` / `R7918030` pattern |
| Low | CWE-20 | Validation concentrated in K; trust in STG header linkage | `FTFCK120` + `R400CH02` (copybook external) |
| Info | — | No hardcoded passwords/API keys found in corpus | — |

Credential inventory: **none** (no `SECRETS.local.md`).

## Documentation Gaps (top 5)

1. **Charge type matrix** — K/L rules for percent vs fixed vs min charge (`FTFCL120` switches) not summarized outside COBOL comments.
2. **Paying party rules** — K-module ILC/customer logic (change history NDL) needs a business rules sheet for Java parity.
3. **Shadow semantics** — `F115L140` purpose and when L invokes it vs direct charge writes.
4. **External port contracts** — 45 CALL targets lack interface docs in-repo (catalog lists names only).
5. **DB table mapping** — EXEC SQL includes (`R115TFCA`-style) not in COPY closure; table/column mapping lives on mainframe only.

## Relative Scale

| Measure | Value |
|---------|--------|
| KSLOC (cloc code) | 15.0 |
| Complexity index | ~58 (relative ranking vs other STG repos) |
| vs TFC11 (reference) | Similar order of magnitude (~16 programs, ~21k src lines); TFC12 adds charge/shadow/FX helpers |

This index is **not** a timeline, budget, or team-size estimate.

## Recommended Modernization Pattern

**Transform (Refactor to new stack in place under `modernized/TFC12/`)** — Rewrite H/K/L and in-tree helpers as **Java 21 / Spring Boot** modules with **ports** for the boundary catalog and **trace-based verification** per [`TOOLCHAIN.md`](TOOLCHAIN.md). Not **Rehost** (no mainframe move as-is) or **Replace** (no COTS product). **Uplift** does not apply (not a version bump of the same COBOL stack).

Suggested transform phasing (align with [`ARCHITECTURE.mmd`](ARCHITECTURE.mmd)):

1. H skeleton + K/L linkage (RTFCE120 / header adapters)  
2. **F115L050** + K validation path  
3. **FTFCL120** core charge paths (L060/L070/L080)  
4. I-modules (ICA0, ICH0, IMC0, IPA0, IPC0) as persistence ports  
5. **F115L140** shadow + external ICC0/IRP0/IEI0 ports  
6. **F116L030** FX + remaining ISC0/ISR0  

## Next command

`/modernize-map TFC12`
