# Scope boundary — TFC12 (closed)

**Status:** Approved for modernization in this repo  
**Date:** 2026-09-30  
**System label:** `TFC12` / `STG/TFC12`  
**Legacy root:** `legacy/STG/TFC12/`

## What is “the system” here?

The system under modernization is **not** the full mainframe Trade Finance estate. It is the **import-locked min + one-hop COBOL closure** copied into this repository:

- **Entry (H/K/L):** `FTFCH120`, `FTFCK120`, `FTFCL120`
- **In-tree helpers:** 14 one-hop programs and 19 copybooks (see root `README.md` and `legacy/STG/TFC12/corpus-manifest.json`)

Discovery (`assess`, `map`, `extract-rules`) and transform work treat **this tree as the complete legacy source of truth** for TFC12 in the Cursor modernization track.

## Outbound dependencies (outside the tree)

Programs and copybooks **referenced but not shipped** in the import closure are **external boundaries**, not missing source to pull from sibling repos at runtime.

| Kind | Count (distinct) | Call / reference sites | Catalog |
|------|------------------|------------------------|---------|
| External `CALL` targets (no `.src` in-tree) | 45 | 178 | [`BOUNDARY_CATALOG.md`](BOUNDARY_CATALOG.md) |
| Unresolved `COPY` members (no `.copy` in-tree) | 35 | 83 | [`BOUNDARY_CATALOG.md`](BOUNDARY_CATALOG.md) |

Modernized code must model these as **ports/adapters** (or equivalent stubs documented in the brief). Expanding `legacy/` requires an explicit **Legacy expansion log** entry in `PREFLIGHT.md` and manifest regeneration — not ad hoc reads from `tfi-workspace` siblings.

## Inbound dependencies (consumers of this tree)

**None identified.** This is a standalone git repository. No in-repo modules, build files, or manifests reference `legacy/STG/TFC12/` from outside paths.

Breaking external mainframe consumers is **not in scope** for this repo: there is no in-place replacement of a shared module consumed by other checkout paths here.

## Relationship to preflight Check 6

Preflight Check 6 is **closed** under **alternative B**:

1. Scope remains the min+onehop slice (human answer: external boundaries acceptable).
2. Outbound deps are **enumerated** in `BOUNDARY_CATALOG.md` (derived from `corpus-manifest.json`).
3. Inbound blast radius is **none** in-repo.
4. Further imports are **opt-in** via documented expansion, not silent scope creep.

## Toolchain (Check 3)

Legacy mainframe COBOL is **not** compiled locally; target proof is **Java 21 + Maven** on the host. See [`TOOLCHAIN.md`](TOOLCHAIN.md) (option A).

## Check 4 (source completeness) for this scope

For this closed boundary, “missing” COPY/CALL members listed in the catalog are **intentional interface points**, not incomplete inventory. JCL, CICS CSD, and DDL are **out of scope** for this repo unless explicitly imported later under the expansion log.

## Sign-off

| Role | Decision |
|------|----------|
| Product / SME (2026-09-30) | Proceed with min+onehop; document externals as ports |
| Engineering | Standalone repo; no inbound consumers |
