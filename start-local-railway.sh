#!/usr/bin/env bash
# Start backend locally using local Docker MySQL/Redis
set -e

JAVA_HOME=/opt/homebrew/opt/openjdk@21
JAR=/Users/macuser/SAAS/nefrontend/ensa-investor-api/target/ensa-investor-api-1.0.0-SNAPSHOT.jar
LOG=/Users/macuser/SAAS/nefrontend/logs/backend.log

exec "$JAVA_HOME/bin/java" \
  -DMYSQL_HOST=localhost \
  -DMYSQL_PORT=3306 \
  -DMYSQL_DATABASE=ensa_investor \
  -DMYSQL_USER=ensa_dev \
  -DMYSQL_PASSWORD=ensa_dev_password \
  -DMYSQL_USE_SSL=false \
  -DFLYWAY_ENABLED=true \
  -DREDIS_HOST=localhost \
  -DREDIS_PORT=6379 \
  -jar "$JAR" \
  --spring.profiles.active=dev
