#!/usr/bin/env bash
# Removes macOS resource-fork and metadata files that break Docker build (xattr errors),
# then runs docker compose up --build.

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/.." && pwd)"

cd "$PROJECT_ROOT"

echo "Removing macOS metadata files (._* and .DS_Store)..."
REMOVED=0
while IFS= read -r -d '' f; do
  rm -f "$f"
  echo "  removed: $f"
  ((REMOVED++)) || true
done < <(find . -name '._*' -type f -print0 2>/dev/null)
while IFS= read -r -d '' f; do
  rm -f "$f"
  echo "  removed: $f"
  ((REMOVED++)) || true
done < <(find . -name '.DS_Store' -type f -print0 2>/dev/null)

if [[ "$REMOVED" -eq 0 ]]; then
  echo "  (none found)"
else
  echo "Removed $REMOVED file(s)."
fi

echo ""
echo "Starting Docker Compose..."
cd docker
exec docker compose up --build "$@"
