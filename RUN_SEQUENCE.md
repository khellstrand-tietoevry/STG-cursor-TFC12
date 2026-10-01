# RUN_SEQUENCE — TFC12 modernization log

Chronological record of **commands run**, **decisions**, **prompts**, and **outcomes** for **STG-cursor-TFC12**. Plugin artifacts live under `analysis/TFC12/`; this file is the human-oriented journal.

### Specialist decisions (review settled)

**Review handoff:** [`CONFIRMED.md`](analysis/TFC12/CONFIRMED.md) (RULE-017 confirmed) · [`DECISIONS.md`](analysis/TFC12/DECISIONS.md) · [`RULE_REVIEWS.json`](analysis/TFC12/RULE_REVIEWS.json) · [`BUSINESS_RULES.md`](analysis/TFC12/BUSINESS_RULES.md).

**Readable chat export:** [`analysis/TFC12/CURSOR_SESSION_EXPORT.md`](analysis/TFC12/CURSOR_SESSION_EXPORT.md) (conversation `91dbb263-3c58-4c3f-b5dd-5d7feed10687`).

**Policy:** Cap parallel Task subagents at **5** (see [`analysis/TFC12/INTENT.md`](analysis/TFC12/INTENT.md)). Repeat in chat when starting each step:

```text
Cap parallel Task subagents at 5 for this project.
```

---

## Session index

| # | Date (UTC+2) | Step | Status | Artifacts |
|---|--------------|------|--------|-----------|
| 0 | 2026-09-30 | Repo scaffold + `legacy/STG/TFC12` | Done | Root `README.md`, `AGENTS.md`, `.cursor/rules/` |
| 1 | 2026-09-30 | Intent / transform Java 21 | Done | [`analysis/TFC12/INTENT.md`](analysis/TFC12/INTENT.md) |
| 2 | 2026-09-30 | `modernize-preflight TFC12` | Done | [`PREFLIGHT.md`](analysis/TFC12/PREFLIGHT.md), [`SCOPE.md`](analysis/TFC12/SCOPE.md) |
| 3 | 2026-09-30 | `modernize-assess TFC12` | Done | [`ASSESSMENT.md`](analysis/TFC12/ASSESSMENT.md) |
| 4 | 2026-09-30 | `modernize-map TFC12` | Done | [`topology.json`](analysis/TFC12/topology.json), [`TOPOLOGY.html`](analysis/TFC12/TOPOLOGY.html) |
| 5 | 2026-09-30 | `modernize-extract-rules TFC12` | Done | [`rules_result.json`](analysis/TFC12/rules_result.json), [`BUSINESS_RULES.md`](analysis/TFC12/BUSINESS_RULES.md) |
| 6 | 2026-09-30 | `modernize-review TFC12` | Done | [`RULE_REVIEWS.json`](analysis/TFC12/RULE_REVIEWS.json), [`CONFIRMED.md`](analysis/TFC12/CONFIRMED.md) |
| 7 | 2026-09-30 | `modernize-brief TFC12` | Approved (Phases 1–6) | [`MODERNIZATION_BRIEF.md`](analysis/TFC12/MODERNIZATION_BRIEF.md) |
| 8 | 2026-09-30 | `modernize-transform TFC12` Phase 1 | Done | `ftfc-k120`, `ftfc-h120`, `tfc12-envelope` |
| 9 | 2026-09-30 | `modernize-transform TFC12` Phase 2 | Done | `ftfc-l050`, K E100 / RULE-012 |
| 10 | 2026-09-30 | `modernize-transform TFC12` Phase 3 | Done | `ftfc-l120`, [`TRACE_G060_SHADOW.md`](analysis/TFC12/TRACE_G060_SHADOW.md) |
| 11 | 2026-09-30 | `modernize-transform TFC12` Phase 4 | Done | `tfc12-persistence`, [`BOUNDARY_PORTS.md`](modernized/TFC12/BOUNDARY_PORTS.md) |
| 12 | 2026-09-30 | `modernize-transform TFC12` Phase 5 | Done | `ftfc-l140`, `ftfc-fx130`, K C520 LC/ILC |
| 13 | 2026-09-30 | `modernize-transform TFC12` Phase 6 | Done | F791TRAC/F7918030 ports, [`VERIFICATION.md`](analysis/TFC12/VERIFICATION.md) |
| 14 | 2026-09-30 | `modernize-verify TFC12` | Done | [`scripts/modernize-verify-tfc12.sh`](modernized/TFC12/scripts/modernize-verify-tfc12.sh), [`equivalence/cases.json`](analysis/TFC12/equivalence/cases.json) |
| 15 | 2026-09-30 | `modernize-harden TFC12` | Done | [`HARDENING.md`](analysis/TFC12/HARDENING.md), `tfc12-boot-smoke` |
| 16 | 2026-09-30 | GitHub publish | Done | `main` @ `khellstrand-tietoevry/STG-cursor-TFC12` (public) |
| 17 | 2026-10-01 | Session export + RUN_SEQUENCE (TFC10-style) | Done | This file + [`CURSOR_SESSION_EXPORT.md`](analysis/TFC12/CURSOR_SESSION_EXPORT.md) |

---

## 0 — Repo and boundary (2026-09-30)

**Trigger:** Standalone **STG-cursor-TFC12** with frozen legacy under `legacy/STG/TFC12/`; no out-of-repo authority during modernization.

**Outcome:** Repository-boundary rules in `AGENTS.md` / `.cursor/rules/repository-boundary.mdc`; system label **TFC12** (Create charge / omkostninger).

---

## 1–7 — Discovery pipeline (2026-09-30)

| Command | Outcome |
|---------|---------|
| Intent | Transform → Java 21 / Spring Boot; exact legacy behavior |
| Preflight | Ready-with-gaps; legacy compile N/A ([`TOOLCHAIN.md`](analysis/TFC12/TOOLCHAIN.md)) |
| Assess | ~58 complexity index; H→K→L flow |
| Map | 62 modules in topology; external CALL = ports |
| Extract rules | **18** rules, **14** P0 |
| Review | RULE-017 confirmed; no `wrong`/`discuss` on P0 |
| Brief | **Full plan Phases 1–6** approved 2026-09-30 |

Report: [`analysis/TFC12/REPORT.html`](analysis/TFC12/REPORT.html)

---

## 8–13 — Transform Phases 1–6 (2026-09-30)

**Chat prompt (repeated):**

```text
/modernize-transform TFC12
Cap parallel Task subagents at 5 for this project.
```

| Phase | Legacy focus | Java modules (high level) |
|-------|--------------|---------------------------|
| 1 | FTFCK120, FTFCH120 | `ftfc-k120`, `ftfc-h120`, RULE-004–007, RULE-015, RULE-016 |
| 2 | F115L050 + K gate | `ftfc-l050`, RULE-001–003, RULE-012, RULE-017 |
| 3 | FTFCL120 core | `ftfc-l120`, compare-helper ports, RULE-014 |
| 4 | F115I* | `tfc12-persistence`, copybook contract tests |
| 5 | F115L140, F116L030, K C520 | `ftfc-l140`, `ftfc-fx130`, RULE-008–011 |
| 6 | H boundaries + verify | F791TRAC/F7918030, S000 subset |

Journal detail: [`TRANSFORMATION_NOTES.md`](analysis/TFC12/TRANSFORMATION_NOTES.md)

**Build proof (final reactor):**

```bash
cd modernized/TFC12 && mvn test
```

**48** tests, **0** failures (includes `tfc12-boot-smoke` MockMvc tests).

---

## 14 — Verify (2026-09-30)

**Chat prompt:**

```text
/modernize-verify TFC12
```

**Outcome:** All **14 P0** rules **PROVEN** via JUnit characterization ([`VERIFICATION.md`](analysis/TFC12/VERIFICATION.md)); trace-based equivalence inventory in [`equivalence/cases.json`](analysis/TFC12/equivalence/cases.json); no local COBOL dual-run.

```bash
./scripts/modernize-verify-tfc12.sh
# ==> /modernize-verify TFC12: OK
```

---

## 15 — Harden (2026-09-30)

**Chat prompt:**

```text
/modernize-harden TFC12
```

**Outcome:** S000 **AW** / K warning restore; `CentralServoErrorMessagePort`; `tfc12-boot-smoke` Spring app; [`HARDENING.md`](analysis/TFC12/HARDENING.md).

```bash
./scripts/modernize-harden-tfc12.sh
```

**Note:** `mvn -pl tfc12-boot-smoke spring-boot:run` fails without `-am` (local `ftfc-h120` not in Artifactory). Use [`scripts/run-boot-smoke.sh`](modernized/TFC12/scripts/run-boot-smoke.sh).

---

## 16 — GitHub (2026-09-30)

**Chat prompt:**

```text
Commit to github and set to public
```

**Outcome:** Merge to `main`; remote **https://github.com/khellstrand-tietoevry/STG-cursor-TFC12** — already **PUBLIC**.

---

## 17 — Session export (2026-10-01)

**Chat prompt:**

```text
Create them using the files created in TFC10 as template
```

**Outcome:** [`analysis/TFC12/CURSOR_SESSION_EXPORT.md`](analysis/TFC12/CURSOR_SESSION_EXPORT.md) + this journal (TFC10 / TFR11 pattern).

---

## Next commands (optional)

1. Resolve brief §7 open questions (R400CH02 header, Oracle schema, parity scenario import).
2. Expand [`equivalence/cases.json`](analysis/TFC12/equivalence/cases.json) when legacy fixtures exist.
3. Wire `modernized/TFC12` into a production STG adapter (separate initiative).

Report: [`analysis/TFC12/REPORT.html`](analysis/TFC12/REPORT.html)
