#!/bin/sh
# Wait a moment for Oracle to accept connections after healthcheck, then create app user.
set -e
sleep 5
sqlplus -S "sys/ensa_dev_password@//oracle-xe:1521/XEPDB1 as sysdba" @/init/create_user.sql
