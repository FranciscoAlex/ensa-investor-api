-- Create application user for ENSA Investor API (idempotent).
-- Run as SYS. Safe to run multiple times.
WHENEVER SQLERROR EXIT SQL.SQLCODE

BEGIN
  EXECUTE IMMEDIATE 'CREATE USER ensa_dev IDENTIFIED BY ensa_dev_password DEFAULT TABLESPACE users QUOTA UNLIMITED ON users';
EXCEPTION
  WHEN OTHERS THEN
    IF SQLCODE != -1920 THEN RAISE; END IF;  -- -1920 = user already exists
END;
/

GRANT CONNECT, RESOURCE TO ensa_dev;
GRANT CREATE VIEW TO ensa_dev;
EXIT;
