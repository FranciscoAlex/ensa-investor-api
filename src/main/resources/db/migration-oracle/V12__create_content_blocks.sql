-- V12__create_content_blocks.sql (Oracle)
-- ON UPDATE CURRENT_TIMESTAMP is MySQL-specific; Spring JPA manages updated_at via @UpdateTimestamp
CREATE TABLE content_blocks (
    id        NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    block_key VARCHAR2(100) NOT NULL,
    data      CLOB          NOT NULL,
    updated_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT uq_content_blocks_key UNIQUE (block_key)
);
