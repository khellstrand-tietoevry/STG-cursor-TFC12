# Agent instructions — STG-cursor-TFC12

## Repository boundary (mandatory)

This repository is **self-contained** for TFC12 modernization work.

- **Use only material inside this repo** as authority for behavior, structure,
  tests, and next steps: `legacy/`, `analysis/`, `modernized/`, and files at
  the repo root.
- **Do not** read, search, or cite paths outside this workspace (for example
  `tfi-workspace`, `tfi-java-cobol-parity-testing`, other STG repos, or sibling
  cob2jav trees) for inspiration, missing source, scenario fixtures, or
  “how we did it elsewhere.”
- If required evidence is missing here, **stop and say what to import into this
  repo** under `legacy/` or `analysis/` — do not substitute external lookups.

Historical provenance in `README.md` describes where the legacy snapshot came
from; it is not a license to pull live data from those locations during work.

## Frozen legacy

- **`legacy/` is read-only.** Commands and agents write under `analysis/` and
  `modernized/` only.
- System id for the code-modernization plugin: **`STG/TFC12`** at
  `legacy/STG/TFC12/`.

## Paths

| Area | Role |
|------|------|
| `legacy/STG/TFC12/` | Frozen COBOL source and copybooks |
| `analysis/` | Assessments, maps, rules, briefs, reports |
| `modernized/` | Uplift, transform, or reimagine outputs |
