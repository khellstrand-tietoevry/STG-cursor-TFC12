# TFC12 — Legacy COBOL (Charges and Pricing)

Standalone copy of the legacy COBOL source for mainframe **TFC12** (charges and
pricing), extracted for modernization analysis with the Cursor
**code-modernization** plugin.

## Layout

```
legacy/STG/TFC12/
├── src/                 17 COBOL programs (.src) — H/K/L entry and one-hop helpers
├── copybooks/           19 copybooks (.copy) — record layouts and includes
└── corpus-manifest.json Call-graph and COPY metadata for the imported closure
analysis/                Plugin discovery output (assess, map, rules, brief, …)
modernized/              Transformed or uplifted code (empty until a build track runs)
```

This mirrors the `legacy/$system` convention expected by **code-modernization**
commands (`/modernize-preflight`, `/modernize-assess`, `/modernize-map`, …):
the system name is **`STG/TFC12`**, so its code lives at `legacy/STG/TFC12/`.

Nothing under `legacy/` should be edited — it is a frozen copy of the original
mainframe source, not a working copy.

## Workspace boundary

After the initial import, **all further work stays inside this repo.** Agents and
humans should not use other checkouts (parity testing, cob2jav, tfi-workspace
siblings, etc.) as inspiration or as a source of missing files. If something is
needed, copy it in explicitly and record it here. See `AGENTS.md`.

Scope and external boundaries are closed in `analysis/TFC12/SCOPE.md` (min+one-hop slice; outbound CALL/COPY = ports).

## Provenance

Copied from
`tfi-java-cobol-parity-testing/reference/original-cobol-code/STG_TRADEFINANCE/TFC12_CHARGES_PRICING/`
(commit `feb8fbb049f3a44a4a7a7b58a4004f4b83b2b509`) in the **tfi-workspace**
container:

| Source | Contents |
|--------|----------|
| `min/src`, `min/copybook` | Canonical H/K/L entry: `FTFCH120`, `FTFCK120`, `FTFCL120` + `RTFCE120` |
| `onehop/src`, `onehop/copybook` | One-hop helper programs and copybooks (byte-locked in `provenance.json`) |

That folder is the **import-locked min + one-hop closure** used for TFC12 parity
and Java translation lanes. It is not the full mainframe inventory; transitive
COPY books referenced but not shipped in that closure are listed under
`corpus-manifest.json` → `unresolved_copybooks`.

The sibling repo `tfi-java-cobol-parity-testing` holds a wider executable lane
(mocks, DB adapters, and parity scenarios). That material is intentionally **not**
copied here; this repo is legacy COBOL only.

## Programs in this snapshot

**Entry (H/K/L):** `FTFCH120`, `FTFCK120`, `FTFCL120`

**One-hop helpers:** `F115ICA0`, `F115ICH0`, `F115IMC0`, `F115IPA0`, `F115IPC0`,
`F115ISC0`, `F115ISR0`, `F115L030`, `F115L050`, `F115L060`, `F115L070`, `F115L080`,
`F115L140`, `F116L030`

## Suggested next commands

```
/modernize-preflight TFC12
/modernize-assess TFC12
/modernize-map TFC12
```

**System label:** `TFC12` · **Legacy:** `legacy/TFC12/` → `legacy/STG/TFC12/`
