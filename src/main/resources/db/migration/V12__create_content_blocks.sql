-- Content blocks table: stores all JSON-based investor content (organogram, carousel, etc.)
-- Data is auto-seeded from classpath JSON on first GET request.
CREATE TABLE content_blocks (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    block_key   VARCHAR(100) NOT NULL,
    data        LONGTEXT     NOT NULL,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_content_blocks_key UNIQUE (block_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
