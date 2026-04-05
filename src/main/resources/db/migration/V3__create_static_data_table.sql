-- V3__create_static_data_table.sql
CREATE SEQUENCE static_data_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE static_data_definitions (
    id NUMBER(19) PRIMARY KEY,
    category VARCHAR2(100) NOT NULL,
    code VARCHAR2(50) NOT NULL,
    label_pt VARCHAR2(255) NOT NULL,
    label_en VARCHAR2(255),
    value VARCHAR2(500),
    parent_id NUMBER(19),
    sort_order NUMBER(5) DEFAULT 0,
    is_active NUMBER(1) DEFAULT 1,
    metadata CLOB,
    description VARCHAR2(500),
    icon VARCHAR2(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT uq_category_code UNIQUE (category, code),
    CONSTRAINT fk_static_parent FOREIGN KEY (parent_id) REFERENCES static_data_definitions(id) ON DELETE SET NULL
);

CREATE INDEX idx_static_category ON static_data_definitions(category);
CREATE INDEX idx_static_active ON static_data_definitions(is_active);
CREATE INDEX idx_static_parent ON static_data_definitions(parent_id);
