-- ============================================================
-- V8: Add missing columns + new tables for General Assemblies
--     and Shareholder Structure (MySQL)
-- ============================================================

-- -----------------------------------------------------------------
-- 1. Extend historical_milestones with image and rich-HTML content
-- -----------------------------------------------------------------
ALTER TABLE historical_milestones
    ADD COLUMN image_url VARCHAR(1000),
    ADD COLUMN content_html LONGTEXT,
    ADD COLUMN event_title VARCHAR(500);

-- -----------------------------------------------------------------
-- 2. Extend communications (press releases) with category, slug,
--    rich-HTML body, author and image
-- -----------------------------------------------------------------
ALTER TABLE communications
    ADD COLUMN slug_id VARCHAR(100),
    ADD COLUMN category VARCHAR(100),
    ADD COLUMN content_html LONGTEXT,
    ADD COLUMN image_url VARCHAR(1000),
    ADD COLUMN author VARCHAR(255);
CREATE INDEX idx_comm_slug ON communications(slug_id);
CREATE INDEX idx_comm_category ON communications(category);

-- -----------------------------------------------------------------
-- 3. General Assembly meetings (ordinary + extraordinary)
-- -----------------------------------------------------------------
CREATE TABLE general_assemblies (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    slug_id       VARCHAR(100) NOT NULL,
    title         VARCHAR(500) NOT NULL,
    meeting_year  SMALLINT     NOT NULL,
    meeting_date  VARCHAR(100),
    status        VARCHAR(50)  DEFAULT 'Realizada',
    assembly_type VARCHAR(50)  DEFAULT 'Ordin\u00e1ria',
    summary       LONGTEXT,
    display_order INT          DEFAULT 0,
    is_active     TINYINT(1)   DEFAULT 1,
    created_at    DATETIME     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_ga_slug UNIQUE (slug_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_ga_year   ON general_assemblies(meeting_year);
CREATE INDEX idx_ga_type   ON general_assemblies(assembly_type);
CREATE INDEX idx_ga_order  ON general_assemblies(display_order);

-- -----------------------------------------------------------------
-- 4. Agenda items for each General Assembly
-- -----------------------------------------------------------------
CREATE TABLE general_assembly_agenda_items (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    assembly_id   BIGINT NOT NULL,
    item_text     LONGTEXT NOT NULL,
    display_order INT  DEFAULT 0,
    CONSTRAINT fk_gai_assembly FOREIGN KEY (assembly_id)
        REFERENCES general_assemblies(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_gai_assembly ON general_assembly_agenda_items(assembly_id);

-- -----------------------------------------------------------------
-- 5. Link existing general_assembly_documents to the new meetings
-- -----------------------------------------------------------------
ALTER TABLE general_assembly_documents
    ADD COLUMN assembly_id BIGINT,
    ADD COLUMN file_size_label VARCHAR(50),
    ADD CONSTRAINT fk_gad_assembly FOREIGN KEY (assembly_id)
        REFERENCES general_assemblies(id) ON DELETE SET NULL;
CREATE INDEX idx_gad_assembly ON general_assembly_documents(assembly_id);

-- -----------------------------------------------------------------
-- 6. Shareholder structure (for the Accionistas / ownership chart)
-- -----------------------------------------------------------------
CREATE TABLE shareholder_structure (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    shareholder_name  VARCHAR(500)  NOT NULL,
    shares_label      VARCHAR(100),
    percentage        DECIMAL(6,4)  NOT NULL,
    display_color     VARCHAR(20),
    display_order     INT           DEFAULT 0,
    is_active         TINYINT(1)    DEFAULT 1,
    created_at        DATETIME      DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_sh_order ON shareholder_structure(display_order);
