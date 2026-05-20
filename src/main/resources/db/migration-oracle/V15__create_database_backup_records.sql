-- V15 (Oracle): Persist database backup metadata so the admin dashboard reads
--               from the DB rather than relying solely on filesystem scans.
CREATE TABLE database_backup_records (
    id           NUMBER(19)   GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    filename     VARCHAR2(200) NOT NULL,
    size_bytes   NUMBER(19)   DEFAULT 0 NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    triggered_by VARCHAR2(20) DEFAULT 'MANUAL' NOT NULL,
    CONSTRAINT uq_dbr_filename UNIQUE (filename)
);
