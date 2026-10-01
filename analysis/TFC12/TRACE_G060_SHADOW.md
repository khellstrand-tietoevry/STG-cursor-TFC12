# G060-Create-Shadow trace notes (RULE-014)

**Source:** `legacy/STG/TFC12/src/FTFCL120.src`

| Step | COBOL | Behavior |
|------|--------|----------|
| 1 | `D000-Preparations` → `G030-Read-Main-Contract` | Read main contract via F115IMC0 (ACTUAL) |
| 2 | `If R115IMC0-In-Progress-No` | Main contract **not** in progress |
| 3 | `Perform G060-Create-Shadow` | Initialize R115L140, `Set R115L140-Create-Shadow`, CALL `F115L140` |
| 4 | In progress | G060 **skipped** |

Java mapping: `FtfcL120Module.preparations` — `MainContractPort` + `ShadowCreationPort`.
