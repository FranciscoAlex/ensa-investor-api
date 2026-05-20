-- V15: Persist database backup metadata so the admin dashboard reads from
--      the DB rather than relying solely on filesystem directory scans.
CREATE TABLE database_backup_records (
    id           BIGINT       AUTO_INCREMENT PRIMARY KEY,
    filename     VARCHAR(200) NOT NULL,
    size_bytes   BIGINT       NOT NULL DEFAULT 0,
    created_at   DATETIME     NOT NULL,
    triggered_by VARCHAR(20)  NOT NULL DEFAULT 'MANUAL',
    CONSTRAINT uq_dbr_filename UNIQUE (filename)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
