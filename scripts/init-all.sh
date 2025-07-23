#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
LOG_FILE="$PROJECT_DIR/init-all.log"

exec > >(tee -i "$LOG_FILE")
exec 2>&1

echo "Initializing all databases"
echo "$(date)"
echo "------------------------------"

echo "Waiting for PostgreSQL: users-db..."
until docker exec users-db pg_isready -U user1 > /dev/null 2>&1; do
  sleep 2
done
echo "PostgreSQL: users-db"
docker exec -i users-db psql -U user1 -d users < "$PROJECT_DIR/init/users-init.sql"

echo "Waiting for PostgreSQL: auth-db..."
until docker exec auth-db pg_isready -U user2 > /dev/null 2>&1; do
  sleep 2
done
echo "PostgreSQL: auth-db"
docker exec -i auth-db psql -U user2 -d auth < "$PROJECT_DIR/init/auth-init.sql"

echo "Waiting for MySQL: mysql-db..."
until docker exec mysql-db mysqladmin ping -uuser3 -ppass3 --silent; do
  sleep 2
done
echo "MySQL: mysql-db"
docker exec -i mysql-db mysql -uuser3 -ppass3 employees < "$PROJECT_DIR/init/mysql-init.sql"

echo "Initialization completed!"
