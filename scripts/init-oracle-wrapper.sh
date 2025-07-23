#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
LOG_FILE="$PROJECT_DIR/init-oracle.log"

exec > >(tee -i "$LOG_FILE")
exec 2>&1

echo "▶ Oracle init wrapper started..."
bash "$SCRIPT_DIR/init-oracle.sh"
echo "Oracle init wrapper finished."
