#!/usr/bin/env bash
# /modernize-verify TFC12 — aggregate test gate + verification doc presence
set -euo pipefail

REACTOR_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
REPO_ROOT="$(cd "${REACTOR_ROOT}/../.." && pwd)"
VERIFICATION="${REPO_ROOT}/analysis/TFC12/VERIFICATION.md"
EQUIVALENCE="${REPO_ROOT}/analysis/TFC12/equivalence/cases.json"

cd "${REACTOR_ROOT}"
echo "==> mvn test (TFC12 reactor)"
mvn -q test

for f in "${VERIFICATION}" "${EQUIVALENCE}"; do
  if [[ ! -f "${f}" ]]; then
    echo "MISSING: ${f}" >&2
    exit 1
  fi
done

echo "==> /modernize-verify TFC12: OK"
