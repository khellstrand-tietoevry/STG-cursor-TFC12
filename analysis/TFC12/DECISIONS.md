# Binding decisions — TFC12

| ID | Decision | Rationale |
|----|----------|-----------|
| D-01 | External CALL/COPY from [`BOUNDARY_CATALOG.md`](BOUNDARY_CATALOG.md) are **Java ports**, not silent stubs | Scope alternative B; no runtime reads from sibling repos |
| D-02 | **RULE-017 confirmed:** do **not** enforce F115L050 E000 update-timestamp on main path | [`RULE_REVIEWS.json`](RULE_REVIEWS.json) — match commented COBOL |
| D-03 | **H always calls L after K** even when K returns error (RULE-015) | Servo H-module sequence in `FTFCH120` |
| D-04 | **Decimal comma** preserved in numeric formatting tests (RULE-016) | `Special-Names Decimal-Point Is Comma` |
| D-05 | DB2 access via **repository/adapters** behind I-modules; no inlined SQL without schema import | EXEC SQL + unresolved SQL COPY members |
| D-06 | **`legacy/**` frozen**; expand only via preflight Legacy expansion log | Preflight Check 0 / AGENTS.md |
