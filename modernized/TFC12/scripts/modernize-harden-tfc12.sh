#!/usr/bin/env bash
# /modernize-harden TFC12 — verify gate + hardening report
set -euo pipefail

REACTOR_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
REPO_ROOT="$(cd "${REACTOR_ROOT}/../.." && pwd)"
HARDENING="${REPO_ROOT}/analysis/TFC12/HARDENING.md"

"${REACTOR_ROOT}/scripts/modernize-verify-tfc12.sh"

if [[ ! -f "${HARDENING}" ]]; then
  echo "MISSING: ${HARDENING}" >&2
  exit 1
fi

echo "==> /modernize-harden TFC12: OK"
