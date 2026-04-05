#!/usr/bin/env bash
# Stops and removes Docker Compose services for the ENSA Investor API.
# Pass -v to remove named volumes as well (e.g. ./scripts/docker-down.sh -v).

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

cd "$PROJECT_ROOT/docker"
exec docker compose down "$@"
