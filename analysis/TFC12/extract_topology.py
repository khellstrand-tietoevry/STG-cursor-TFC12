#!/usr/bin/env python3
"""Extract call/data topology for TFC12 from legacy/STG/TFC12 (corpus-manifest + sources)."""

from __future__ import annotations

import json
import re
from collections import defaultdict
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
LEGACY = REPO / "legacy" / "STG" / "TFC12"
OUT = Path(__file__).resolve().parent / "topology.json"

CALL_RE = re.compile(r"\bCall\s+'([A-Z0-9]+)'", re.I)
SQL_RE = re.compile(r"\bExec\s+SQL\b", re.I)

HKL = {"FTFCH120", "FTFCK120", "FTFCL120"}

DESCRIPTIONS: dict[str, str] = {
    "FTFCH120": "H-module for TFC12 create charge (omkostninger). Orchestrates FTFCK120 then FTFCL120 under STG header R400CH02 and function copy RTFCE120. Uses trace F791TRAC and error helper F7918030 (external). Decimal-Point Is Comma.",
    "FTFCK120": "K-module control for charge setup. Runs F115L050 initial check and F115ISC0 sys-code reads; validates paying party and charge acceptance rules before L-module work.",
    "FTFCL120": "L-module logic for main and part charges, percent/fixed calculation, shadow via F115L140, timestamps F115L030, compare helpers L060/L070/L080, and I-module persistence ICA0/ICH0/IMC0/IPA0/IPC0.",
    "F115L050": "Initial-check gate (R115L050-Initial-Check). Invoked from FTFCK120 before charge processing continues.",
    "F115L030": "Sets operational timestamps (R115L030-Set-Timestamp). Called from FTFCL120.",
    "F115L060": "Compare/compute charge tier helper (R115L060-Comp-Charge). Called from FTFCL120.",
    "F115L070": "Compare/compute charge tier helper (R115L070-Comp-Charge). Called from FTFCL120.",
    "F115L080": "Compare/compute charge tier helper (R115L080-Comp-Charge). Called from FTFCL120.",
    "F115L140": "Shadow and status orchestration (~2.9k LOC). Called from FTFCL120; calls external F115L040, F115ICC0, F115IRP0, F115IEI0 and in-repo F115IMC0, F115ICA0, F115IPA0.",
    "F115ICA0": "Contract amount I-module (EXEC SQL). Called from FTFCL120 and F115L140; external read/update variants F115ICAR, F115ICAU.",
    "F115ICH0": "Main charge I-module (EXEC SQL). Called from FTFCL120.",
    "F115IMC0": "Main contract I-module (EXEC SQL). Called from FTFCL120 and F115L140.",
    "F115IPA0": "Part amount I-module (EXEC SQL). Called from FTFCL120 and F115L140.",
    "F115IPC0": "Part charge I-module (EXEC SQL). Called from FTFCL120.",
    "F115ISC0": "System code read (R115ISC0-Sys-Code). Called from FTFCK120 and FTFCL120.",
    "F115ISR0": "Status relation sys-code (R115ISR0). Called from FTFCL120.",
    "F116L030": "Currency exchange helper (R116L030-Currency-Xchange). Called from FTFCL120.",
}


def loc_for(path: Path) -> int:
    return len(path.read_bytes().decode("latin-1", errors="replace").splitlines())


def main() -> None:
    manifest = json.loads((LEGACY / "corpus-manifest.json").read_text(encoding="utf-8"))
    in_repo: dict[str, dict] = {}
    for path in sorted((LEGACY / "src").glob("*.src")):
        member = path.stem.upper()
        rel = f"src/{path.name}"
        in_repo[member] = {
            "id": member,
            "name": member,
            "kind": "module",
            "language": "cobol",
            "file": rel,
            "loc": loc_for(path),
            "description": DESCRIPTIONS.get(member, f"In-repo TFC12 program {member}."),
        }

    callers: dict[str, set[str]] = defaultdict(set)
    edges: list[dict] = []
    for ce in manifest.get("call_edges", []):
        caller = ce["caller"]
        target = ce["target"]
        if ce.get("target_kind") != "literal" or not target:
            continue
        kind = "call"
        if target not in in_repo:
            callers[target].add(caller)
        edges.append({"source": caller, "target": target, "kind": kind})

    external_nodes = []
    for target in sorted({e["target"] for e in edges if e["target"] not in in_repo}):
        clist = ", ".join(sorted(callers[target])[:8])
        extra = "…" if len(callers[target]) > 8 else ""
        external_nodes.append(
            {
                "id": target,
                "name": target,
                "kind": "module",
                "language": "cobol",
                "loc": 0,
                "description": f"{target} is an external boundary (no .src in legacy/STG/TFC12). Callers include: {clist}{extra}.",
            }
        )

    sql_modules = [m for m in in_repo if SQL_RE.search((LEGACY / "src" / f"{m}.src").read_text(encoding="latin-1", errors="replace"))]
    data_children = [
        {
            "id": "ds:DB2-TFC12",
            "name": "DB2 (Trade Finance)",
            "kind": "datastore",
            "description": "Mainframe DB2 accessed via EXEC SQL in F115I* modules; table layouts partly in unresolved COPY members.",
        },
        {
            "id": "ds:RTFCE120",
            "name": "RTFCE120 function I/O",
            "kind": "datastore",
            "description": "STG function envelope copybook RTFCE120 — charge/pricing request and response fields for TFC12.",
        },
    ]

    for mod in sql_modules:
        edges.append({"source": mod, "target": "ds:DB2-TFC12", "kind": "read"})
        edges.append({"source": mod, "target": "ds:DB2-TFC12", "kind": "write"})

    edges.append({"source": "FTFCH120", "target": "ds:RTFCE120", "kind": "read"})
    edges.append({"source": "FTFCL120", "target": "ds:RTFCE120", "kind": "read"})
    edges.append({"source": "FTFCL120", "target": "ds:RTFCE120", "kind": "write"})

    stg_children = [in_repo[m] for m in ["FTFCH120", "FTFCK120", "FTFCL120"] if m in in_repo]
    helper_children = [in_repo[m] for m in sorted(in_repo) if m not in HKL]

    root = {
        "id": "sys",
        "name": "TFC12",
        "kind": "system",
        "children": [
            {"id": "dom:stg", "name": "STG H/K/L", "kind": "domain", "children": stg_children},
            {"id": "dom:helpers", "name": "In-repo helpers", "kind": "domain", "children": helper_children},
            {"id": "dom:boundary", "name": "External CALL targets", "kind": "domain", "children": external_nodes},
            {"id": "dom:data", "name": "Data stores", "kind": "domain", "children": data_children},
        ],
    }

    inbound: dict[str, int] = defaultdict(int)
    for e in edges:
        if e["kind"] == "call":
            inbound[e["target"]] += 1
    all_module_ids = set(in_repo) | {n["id"] for n in external_nodes}
    dead = sorted(mid for mid in all_module_ids if inbound[mid] == 0 and mid != "FTFCH120")

    observations = [
        "F115L140 is the largest in-repo hub (~2.9k LOC) and fans out to both in-repo I-modules and external ICC0/IRP0/IEI0/L040 ports.",
        "All seven F115I* helpers use EXEC SQL; DB2 access must be modeled as adapters, not inlined without schema imports.",
        "45 external CALL targets are boundary modules; map edges are complete but sources live outside this repo per SCOPE.md.",
        "H-module always invokes K then L (Servo pattern); Java transform should preserve ordering even when K rejects.",
        "Charge compare helpers L060/L070/L080 form a parallel tier called from FTFCL120 — candidate for shared Java strategy.",
        "No JCL/CICS entry config in-repo; FTFCH120 is the documented STG entry for this closure.",
    ]

    flows = [
        {
            "name": "Register a trade finance charge",
            "persona": "Back-office operator",
            "description": "Operator submits a create-charge STG request for a contract or part.",
            "steps": [
                {"label": "STG invokes H-module", "nodes": ["FTFCH120", "ds:RTFCE120"]},
                {"label": "Control validation (K)", "nodes": ["FTFCK120", "F115L050", "F115ISC0"]},
                {"label": "Charge logic (L)", "nodes": ["FTFCL120", "F115L060", "F115ICH0", "F115IPC0"]},
                {"label": "Persist amounts", "nodes": ["F115ICA0", "F115IPA0", "F115IMC0", "ds:DB2-TFC12"]},
            ],
        },
        {
            "name": "Percent-based charge with shadow update",
            "persona": "Pricing specialist",
            "description": "Specialist sets percent charges that trigger shadow processing.",
            "steps": [
                {"label": "K initial check passes", "nodes": ["FTFCK120", "F115L050"]},
                {"label": "L selects percent path", "nodes": ["FTFCL120"]},
                {"label": "Shadow orchestration", "nodes": ["F115L140", "F115IMC0"]},
                {"label": "External status/event ports", "nodes": ["F115ICC0", "F115IEI0", "F115IRP0"]},
            ],
        },
        {
            "name": "Cross-currency charge",
            "persona": "Treasury user",
            "description": "Charge requires currency conversion before amounts are stored.",
            "steps": [
                {"label": "L-module charge path", "nodes": ["FTFCL120"]},
                {"label": "Currency exchange", "nodes": ["F116L030"]},
                {"label": "Amount helpers", "nodes": ["F115ICA0", "F115IPA0", "ds:DB2-TFC12"]},
            ],
        },
    ]

    topology = {
        "system": "TFC12",
        "generated": "2026-09-30",
        "legacy_path": "legacy/STG/TFC12",
        "stats": {
            "programs_in_repo": len(in_repo),
            "external_call_targets": len(external_nodes),
            "call_edges": len([e for e in edges if e["kind"] == "call"]),
            "unresolved_copybooks": manifest.get("counts", {}).get("unresolved_copybooks", 0),
        },
        "root": root,
        "edges": edges,
        "entryPoints": ["FTFCH120"],
        "deadEnds": dead[:20],
        "observations": observations,
        "flows": flows,
    }

    OUT.write_text(json.dumps(topology, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")
    print(f"Wrote {OUT}")
    print(f"  in-repo modules: {len(in_repo)}")
    print(f"  external targets: {len(external_nodes)}")
    print(f"  call edges: {topology['stats']['call_edges']}")
    print(f"  total edges (incl data): {len(edges)}")
    print(f"  dead-end candidates: {len(dead)}")


if __name__ == "__main__":
    main()
