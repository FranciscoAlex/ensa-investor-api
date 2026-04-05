-- ============================================================
-- V8: Add missing columns + new tables for General Assemblies
--     and Shareholder Structure
-- ============================================================

-- -----------------------------------------------------------------
-- 1. Extend historical_milestones with image and rich-HTML content
-- -----------------------------------------------------------------
ALTER TABLE historical_milestones ADD (
    image_url   VARCHAR2(1000),
    content_html CLOB,
    event_title VARCHAR2(500)
);

-- -----------------------------------------------------------------
-- 2. Extend communications (press releases) with category, slug,
--    rich-HTML body, author and image
-- -----------------------------------------------------------------
ALTER TABLE communications ADD (
    slug_id      VARCHAR2(100),
    category     VARCHAR2(100),
    content_html CLOB,
    image_url    VARCHAR2(1000),
    author       VARCHAR2(255)
);
CREATE INDEX idx_comm_slug ON communications(slug_id);
CREATE INDEX idx_comm_category ON communications(category);

-- -----------------------------------------------------------------
-- 3. General Assembly meetings (ordinary + extraordinary)
-- -----------------------------------------------------------------
CREATE SEQUENCE general_assembly_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE general_assemblies (
    id            NUMBER(19) PRIMARY KEY,
    slug_id       VARCHAR2(100) NOT NULL,       -- e.g. "ag-2024"
    title         VARCHAR2(500) NOT NULL,        -- full meeting title / pauta
    meeting_year  NUMBER(4)     NOT NULL,
    meeting_date  VARCHAR2(100),                 -- stored as formatted label, e.g. "10 de Abril de 2025"
    status        VARCHAR2(50)  DEFAULT 'Realizada', -- Realizada | Convocada
    assembly_type VARCHAR2(50)  DEFAULT 'Ordinária', -- Ordinária | Extraordinária | Eleitoral
    summary       CLOB,                           -- full summary / content paragraph
    display_order NUMBER(5)     DEFAULT 0,
    is_active     NUMBER(1)     DEFAULT 1,
    created_at    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_ga_slug UNIQUE (slug_id)
);
CREATE INDEX idx_ga_year   ON general_assemblies(meeting_year);
CREATE INDEX idx_ga_type   ON general_assemblies(assembly_type);
CREATE INDEX idx_ga_order  ON general_assemblies(display_order);

-- -----------------------------------------------------------------
-- 4. Agenda items for each General Assembly
-- -----------------------------------------------------------------
CREATE SEQUENCE general_assembly_agenda_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE general_assembly_agenda_items (
    id            NUMBER(19) PRIMARY KEY,
    assembly_id   NUMBER(19) NOT NULL,
    item_text     CLOB       NOT NULL,
    display_order NUMBER(5)  DEFAULT 0,
    CONSTRAINT fk_gai_assembly FOREIGN KEY (assembly_id)
        REFERENCES general_assemblies(id) ON DELETE CASCADE
);
CREATE INDEX idx_gai_assembly ON general_assembly_agenda_items(assembly_id);

-- -----------------------------------------------------------------
-- 5. Link existing general_assembly_documents to the new meetings
-- -----------------------------------------------------------------
ALTER TABLE general_assembly_documents ADD (
    assembly_id    NUMBER(19),
    file_size_label VARCHAR2(50),
    CONSTRAINT fk_gad_assembly FOREIGN KEY (assembly_id)
        REFERENCES general_assemblies(id) ON DELETE SET NULL
);
CREATE INDEX idx_gad_assembly ON general_assembly_documents(assembly_id);

-- -----------------------------------------------------------------
-- 6. Shareholder structure (for the Accionistas / ownership chart)
-- -----------------------------------------------------------------
CREATE SEQUENCE shareholder_structure_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE shareholder_structure (
    id                NUMBER(19)     PRIMARY KEY,
    shareholder_name  VARCHAR2(500)  NOT NULL,
    shares_label      VARCHAR2(100),             -- formatted string e.g. "1.680.000"
    percentage        NUMBER(6,4)    NOT NULL,   -- 70.0000
    display_color     VARCHAR2(20),              -- hex color e.g. "#164993"
    display_order     NUMBER(5)      DEFAULT 0,
    is_active         NUMBER(1)      DEFAULT 1,
    created_at        TIMESTAMP      DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_sh_order ON shareholder_structure(display_order);
