-- V8__add_assemblies_and_shareholders.sql (Oracle)

-- 1. Extend historical_milestones
ALTER TABLE historical_milestones ADD (
    image_url    VARCHAR2(1000),
    content_html CLOB,
    event_title  VARCHAR2(500)
);

-- 2. Extend communications
ALTER TABLE communications ADD (
    slug_id      VARCHAR2(100),
    category     VARCHAR2(100),
    content_html CLOB,
    image_url    VARCHAR2(1000),
    author       VARCHAR2(255)
);
CREATE INDEX idx_comm_slug     ON communications(slug_id);
CREATE INDEX idx_comm_category ON communications(category);

-- 3. General Assemblies
CREATE TABLE general_assemblies (
    id            NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    slug_id       VARCHAR2(100) NOT NULL,
    title         VARCHAR2(500) NOT NULL,
    meeting_year  NUMBER(5)     NOT NULL,
    meeting_date  VARCHAR2(100),
    status        VARCHAR2(50)  DEFAULT 'Realizada',
    assembly_type VARCHAR2(50)  DEFAULT 'Ordinária',
    summary       CLOB,
    display_order NUMBER(10)    DEFAULT 0,
    is_active     NUMBER(1)     DEFAULT 1,
    created_at    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_ga_slug UNIQUE (slug_id)
);
CREATE INDEX idx_ga_year  ON general_assemblies(meeting_year);
CREATE INDEX idx_ga_type  ON general_assemblies(assembly_type);
CREATE INDEX idx_ga_order ON general_assemblies(display_order);

-- 4. General Assembly agenda items
CREATE TABLE general_assembly_agenda_items (
    id            NUMBER(19) GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    assembly_id   NUMBER(19) NOT NULL,
    item_text     CLOB       NOT NULL,
    display_order NUMBER(10) DEFAULT 0,
    CONSTRAINT fk_gai_assembly FOREIGN KEY (assembly_id) REFERENCES general_assemblies(id) ON DELETE CASCADE
);
CREATE INDEX idx_gai_assembly ON general_assembly_agenda_items(assembly_id);

-- 5. Link general_assembly_documents to general_assemblies
ALTER TABLE general_assembly_documents ADD (
    assembly_id      NUMBER(19),
    file_size_label  VARCHAR2(50)
);
ALTER TABLE general_assembly_documents ADD CONSTRAINT fk_gad_assembly
    FOREIGN KEY (assembly_id) REFERENCES general_assemblies(id) ON DELETE SET NULL;
CREATE INDEX idx_gad_assembly ON general_assembly_documents(assembly_id);

-- 6. Shareholder structure
CREATE TABLE shareholder_structure (
    id               NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    shareholder_name VARCHAR2(500) NOT NULL,
    shares_label     VARCHAR2(100),
    percentage       NUMBER(6,4)   NOT NULL,
    display_color    VARCHAR2(20),
    display_order    NUMBER(10)    DEFAULT 0,
    is_active        NUMBER(1)     DEFAULT 1,
    created_at       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_sh_order ON shareholder_structure(display_order);
