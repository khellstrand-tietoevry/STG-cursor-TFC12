# Preflight — TFC12

**Date:** 2026-09-30  
**Target stack:** Java 21 Spring Boot — Check 3 closed (option A); see [`TOOLCHAIN.md`](TOOLCHAIN.md)  
**Legacy path:** `legacy/TFC12/` → `legacy/STG/TFC12/` (symlink in workspace)

## Answers (human preflight)

### 1. Scope

**Answer:** Slice (min+onehop) — external CALL/COPY boundaries OK.

### 2. Build & test locally

**Answer:** Java/Maven locally once `modernized/` exists; no mainframe COBOL compile.

### 3. Bespoke build infrastructure

**Answer:** None in this repo; legacy assumes mainframe DB2/CICS.

### 4. Prior attempts

**Answer:** Parity/Java work exists elsewhere; this repo stays self-contained.

### 5. Off limits

**Answer:** Everything under `legacy/**` is frozen; only `analysis/` and `modernized/` change.

## Check 3 — Build toolchain

**Closed (option A).** **Legacy COBOL:** Level 2 **N/A** — mainframe `EXEC SQL`/DB2 and boundary COPY (per [`SCOPE.md`](SCOPE.md)) mean GnuCOBOL compile is not a gate; equivalence uses source analysis + traces + Java tests, not local dual COBOL. **Target:** OpenJDK **21.0.10** + Maven **3.9.11** scratch build verified 2026-09-30. Detail: [`TOOLCHAIN.md`](TOOLCHAIN.md).

## Check 6 — Scope boundary

**Closed (alternative B).** Standalone git repo; the modernization unit is the min+one-hop closure at `legacy/STG/TFC12/`. Outbound dependencies are **documented interfaces**, not silent gaps — see [`SCOPE.md`](SCOPE.md) and [`BOUNDARY_CATALOG.md`](BOUNDARY_CATALOG.md) (**45** external `CALL` targets, **35** unresolved `COPY` members; counts from `corpus-manifest.json`). **Inbound:** none in-repo. Expanding legacy requires an explicit Legacy expansion log entry.

## Summary table

| Check | Status | Finding |
|-------|--------|---------|
| 0 Answers | ✅ | All five captured from preflight prompts |
| 1 Stack | ✅ | 17 `.src`, 19 `.copy`, ~24.1k lines; fixed-format COBOL (H/K/L + one-hop helpers) |
| 2 Analysis tools | ✅ | `python3` 3.14.3; `scc` 4.1.0; `cloc` 2.10; `lizard` 2.1.0 — full assess/map tooling available (verified 2026-09-30) |
| 3 Build toolchain | ✅ | Legacy COBOL compile **N/A** (option A); target Java 21 + Maven 3.9.11 verified — [`TOOLCHAIN.md`](TOOLCHAIN.md) |
| 4 Source completeness | ✅ | Closure complete **for declared scope**; intentional externals in `BOUNDARY_CATALOG.md`; JCL/CSD/DDL out of scope unless imported |
| 5 Optional context | ✅ | Git history on `main` (post-initial push); **no APM** — runtime overlay not used for this repo |
| 6 Scope | ✅ | Boundary closed per `SCOPE.md` (alternative B); outbound = ports, inbound = none |
| 7 Legacy protection | ✅ | `AGENTS.md` + `.cursor/rules/repository-boundary.mdc` enforce read-only `legacy/` and repo boundary (no Claude `Edit(legacy/**)` deny in project settings) |

Permission rules cover agent conventions, not OS-level write blocking. Treat `legacy/**` as immutable regardless.

## Verdict

| Command | Verdict |
|---------|---------|
| assess, map, extract-rules | **Ready** (externals catalogued as boundaries) |
| brief | **Ready** after discovery artifacts |
| transform | **Ready-with-gaps** — model external CALL/COPY as ports/adapters per brief |
| verify | **Ready-with-gaps** (no local COBOL dual-run; Java/trace equivalence per `TOOLCHAIN.md`) |
| harden | **Ready-with-gaps** |
| reimagine | **Ready-with-gaps** (same toolchain constraints) |
| uplift | **Not assessed** (no `$target_stack` version bump requested) |

**Overall:** **Ready** for discovery (`assess` → `map` → `extract-rules`); transform/verify remain **Ready-with-gaps** until Java exists and ports are defined in the brief.

## Legacy expansion log (approved imports)

| Date | Artifact | Note |
|------|----------|------|
| 2026-09-30 | Initial import | 17 programs + 19 copybooks from `tfi-java-cobol-parity-testing` TFC12_CHARGES_PRICING closure (see root `README.md`) |
| 2026-09-30 | Scope closure | `SCOPE.md` + `BOUNDARY_CATALOG.md` — alternative B (documented ports, no corpus expansion) |
| 2026-09-30 | Analysis tooling | Host: `scc` 4.1.0, `cloc` 2.10, `lizard` 2.1.0 (Homebrew) — Check 2 green |
| 2026-09-30 | Toolchain closure | `TOOLCHAIN.md` — Check 3 option A (legacy compile N/A, Java/Maven verified) |
| 2026-09-30 | VCS + Check 5 | Initial commit; public GitHub; no APM telemetry planned |

**Next:** `/modernize-assess TFC12`
