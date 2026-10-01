#!/usr/bin/env bash
# Run tfc12-boot-smoke with reactor dependencies built locally (-am).
set -euo pipefail
cd "$(dirname "$0")/.."
exec mvn -q -pl tfc12-boot-smoke -am spring-boot:run "$@"
