# External boundary catalog — TFC12

Generated from `legacy/STG/TFC12/corpus-manifest.json` at repo import.  
These artifacts are **outside** the modernization corpus unless added via the preflight **Legacy expansion log**.

**Manifest counts:** 178 external `CALL` sites → **45** distinct targets; **83** unresolved `COPY` sites → **35** distinct members.

## External programs (`CALL`, no `.src` in-tree)

| Target | Call sites | Typical role (inferred) |
|--------|------------|-------------------------|
| F5879010 | 15 | External utility / cross-module |
| F115I999 | 14 | I-module terminator / error path |
| F7918030 | 14 | R791 service module |
| F115L999 | 12 | L-module terminator |
| F7919100 | 8 | R791 service module |
| F115IEI0 | 6 | I-module helper |
| F115ICAR, F115ICHR, F115IMCR, F115IPAR, F115IPAU, F115IPCR, F115ISCR, F115ISRR | 4 each | I-module read/update variants |
| F115ICC0, F115IRP0, F115ISE0, F115ILM0, F115ILL0, F115IWM0, F115IWF0, F115ITP0 | 4 each | I-module helpers |
| F115ICAU, F115ICHU, F115IMCU, F115IPCU, F115ISCU, F115ISRU | 3 each | I-module update variants |
| F7919270, F115ICT0 | 3 each | Service / I-module |
| F1781910, F7919070, F115L040, F791I020, F116L999, F7919990 | 2 each | Mixed L/R/I helpers |
| F115IST0, F115L150, F7918110, F7918040, F203L130, F791I080, F116I020, F791IET0, F791TRAC | 1 each | Leaf boundaries |

**Transform rule:** Each target becomes a **port** with contract inferred from call sites and copybook usage in `map` / `extract-rules`. Do not assume source availability.

## Unresolved copybooks (`COPY`, no `.copy` in-tree)

| Copybook | Reference sites | Notes |
|----------|-----------------|-------|
| R7919999 | 17 | Shared R791 layout cluster |
| R7918030 | 14 | H/K common include |
| R5879010 | 8 | Cross-system layout |
| R7919100 | 5 | R791 layout |
| R400CH02 | 3 | Charge-related layout |
| R1781910, R7919270, R115IEI0, R115L040, R7919070, R791I020 | 2 each | Mixed |
| R115ICAS, R115ICHS, R115IMCS, R115IPAS, R115IPCS, R115ISCS, R115ISRS | 1 each | I-module shadow layouts |
| R115IST0, R115ICC0, R115IRP0, R115ITP0, R115ISE0, R115ILM0, R115ILL0, R115IWM0, R115IWF0 | 1 each | I-module layouts |
| R116I020, R203L130, R7918040, R7918110, R791I080, R791IET0, R791TRAC, R7919040 | 1 each | Service / trace layouts |

**Transform rule:** Treat as **external record layouts** or adapter DTOs until imported. SQL includes (e.g. `Exec SQL Include …`) remain mainframe-DB boundaries.

## Regenerating this catalog

After any approved import into `legacy/STG/TFC12/`, regenerate `corpus-manifest.json` and refresh this file from manifest `external_calls` and `unresolved_copybooks`.
