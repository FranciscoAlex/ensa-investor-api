-- V3__create_static_data_table.sql
CREATE TABLE static_data_definitions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL,
    label_pt VARCHAR(255) NOT NULL,
    label_en VARCHAR(255),
    value VARCHAR(500),
    parent_id BIGINT,
    sort_order SMALLINT DEFAULT 0,
    is_active TINYINT(1) DEFAULT 1,
    metadata JSON,
    description VARCHAR(500),
    icon VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    CONSTRAINT uq_category_code UNIQUE (category, code),
    CONSTRAINT fk_static_parent FOREIGN KEY (parent_id) REFERENCES static_data_definitions(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_static_category ON static_data_definitions(category);
CREATE INDEX idx_static_active ON static_data_definitions(is_active);
CREATE INDEX idx_static_parent ON static_data_definitions(parent_id);
