#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
INIT_SQL="$PROJECT_DIR/init/oracle-init.sql"
CONTAINER_NAME=oracle-db

echo "Waiting for Oracle DB to be ready..."
for i in {1..30}; do
  if docker exec "$CONTAINER_NAME" bash -c "echo 'exit' | sqlplus -s user4/pass4@//localhost:1521/XEPDB1" | grep -q "Connected"; then
    echo "Oracle is ready."
    break
  fi
  sleep 5
done

echo "▶ Oracle: loading initialization SQL..."
docker exec -i "$CONTAINER_NAME" bash -c "
echo 'ALTER SESSION SET CURRENT_SCHEMA = user4;' > /tmp/full-init.sql && \
cat > /tmp/temp-init.sql && \
cat /tmp/temp-init.sql >> /tmp/full-init.sql && \
sqlplus -s user4/pass4@//localhost:1521/XEPDB1 @/tmp/full-init.sql
" < "$INIT_SQL"

echo "Oracle data successfully uploaded."
