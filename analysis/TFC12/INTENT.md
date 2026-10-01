# Intent — TFC12 (Create charge / omkostninger)

Recorded: 2026-09-30 (Cursor + code-modernization plugin)

## Track

**Transform** — rewrite module-by-module to **Java 21 / Spring Boot 3.x** (strangler-fig).

## Parity

**Match legacy behavior exactly, including known quirks.** Equivalence uses characterization tests and trace-based comparisons; local COBOL dual-run is out of scope ([`TOOLCHAIN.md`](TOOLCHAIN.md)).

## Target stack

Java 21 (OpenJDK), Spring Boot 3.x, Maven multi-module under `modernized/TFC12/`.

## Legacy

- **Label:** `TFC12`
- **Path:** `legacy/TFC12/` → `legacy/STG/TFC12/`
- **Frozen:** no edits under `legacy/**`; outputs under `analysis/TFC12/` and `modernized/`.

## Scope note

Import-locked **min + one-hop** closure per [`SCOPE.md`](SCOPE.md). External CALL/COPY are **ports** ([`BOUNDARY_CATALOG.md`](BOUNDARY_CATALOG.md)).

## Orchestration

```yaml
max_parallel_subagents: 5
```

> Cap parallel Task subagents at 5 for this project.

## Entry chain

`FTFCH120` (H) → `FTFCK120` (K) → `FTFCL120` (L)
