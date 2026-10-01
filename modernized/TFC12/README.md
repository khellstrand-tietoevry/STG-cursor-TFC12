# TFC12 — Java 21 modernization

Maven reactor for **Create charge** (TFC12/01). Phases 1–6 delivered as library modules.

| Module | Legacy program |
|--------|----------------|
| `tfc12-envelope` | R400CH01 / RTFCE120 DTO subset |
| `tfc12-persistence` | F115I* repository ports + in-memory adapters |
| `ftfc-l050` | F115L050 initial check |
| `ftfc-k120` | FTFCK120 |
| `ftfc-h120` | FTFCH120 (+ F791TRAC / F7918030 ports) |
| `ftfc-l120-api` | FTFCL120 port interface |
| `ftfc-l120` | FTFCL120 orchestration slice |
| `ftfc-l140` | F115L140 create-shadow slice |
| `ftfc-fx130` | F116L030 FX subset |
| `tfc12-boot-smoke` | Spring Boot local smoke (hardening) |

```bash
cd modernized/TFC12 && mvn test
./scripts/modernize-verify-tfc12.sh   # /modernize-verify TFC12
./scripts/modernize-harden-tfc12.sh   # /modernize-harden TFC12
./scripts/run-boot-smoke.sh           # Spring smoke app (needs -am; do not use -pl alone)
```

See [`analysis/TFC12/MODERNIZATION_BRIEF.md`](../../analysis/TFC12/MODERNIZATION_BRIEF.md), [`analysis/TFC12/VERIFICATION.md`](../../analysis/TFC12/VERIFICATION.md), and [`analysis/TFC12/TRANSFORMATION_NOTES.md`](../../analysis/TFC12/TRANSFORMATION_NOTES.md).
