-- V6__create_investor_content_tables.sql (Oracle)

-- 1. Historical milestones
CREATE TABLE historical_milestones (
    id            NUMBER(19)   GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title         VARCHAR2(500) NOT NULL,
    description   CLOB,
    milestone_year NUMBER(5),
    display_order NUMBER(10)   DEFAULT 0,
    is_active     NUMBER(1)    DEFAULT 1,
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP
);
CREATE INDEX idx_hm_year  ON historical_milestones(milestone_year);
CREATE INDEX idx_hm_order ON historical_milestones(display_order);

-- 2. BODIVA share / stock indicators history
CREATE TABLE bodiva_share_history (
    id            NUMBER(19)   GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    record_date   DATE         NOT NULL,
    share_price   NUMBER(18,4),
    volume        NUMBER(19),
    opening_price NUMBER(18,4),
    closing_price NUMBER(18,4),
    high_price    NUMBER(18,4),
    low_price     NUMBER(18,4),
    notes         VARCHAR2(1000),
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_bodiva_date ON bodiva_share_history(record_date);

-- 3. Board of Directors members
CREATE TABLE board_members (
    id               NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name        VARCHAR2(255) NOT NULL,
    role             VARCHAR2(255),
    bio              CLOB,
    cv_document_url  VARCHAR2(1000),
    photo_url        VARCHAR2(1000),
    display_order    NUMBER(10)    DEFAULT 0,
    is_active        NUMBER(1)     DEFAULT 1,
    created_at       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP
);
CREATE INDEX idx_bm_order ON board_members(display_order);

-- 4. Corporate governance reports
CREATE TABLE corporate_governance_reports (
    id           NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title        VARCHAR2(500) NOT NULL,
    document_url VARCHAR2(1000) NOT NULL,
    report_year  NUMBER(5),
    language     VARCHAR2(10)  DEFAULT 'pt',
    created_at   TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_cgr_year ON corporate_governance_reports(report_year);

-- 5. Financial statements
CREATE TABLE financial_statements (
    id             NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    year           NUMBER(5)     NOT NULL,
    title          VARCHAR2(500) NOT NULL,
    document_url   VARCHAR2(1000) NOT NULL,
    statement_type VARCHAR2(100),
    language       VARCHAR2(10)  DEFAULT 'pt',
    created_at     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_fs_year ON financial_statements(year);

-- 6. Business / financial indicators
CREATE TABLE business_indicators (
    id              NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title           VARCHAR2(500) NOT NULL,
    indicator_value VARCHAR2(500),
    numeric_value   NUMBER(18,4),
    period_year     NUMBER(5),
    period_quarter  NUMBER(1),
    category        VARCHAR2(100),
    unit            VARCHAR2(50),
    created_at      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_bi_period   ON business_indicators(period_year, period_quarter);
CREATE INDEX idx_bi_category ON business_indicators(category);

-- 7. Communications / press releases
CREATE TABLE communications (
    id                 NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title              VARCHAR2(500) NOT NULL,
    communication_type VARCHAR2(100),
    summary            CLOB,
    document_url       VARCHAR2(1000),
    published_at       TIMESTAMP,
    is_active          NUMBER(1)     DEFAULT 1,
    created_at         TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_comm_type      ON communications(communication_type);
CREATE INDEX idx_comm_published ON communications(published_at);

-- 8. Events and meetings calendar
CREATE TABLE events (
    id          NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title       VARCHAR2(500) NOT NULL,
    description CLOB,
    event_date  TIMESTAMP,
    end_date    TIMESTAMP,
    location    VARCHAR2(500),
    event_type  VARCHAR2(100),
    is_active   NUMBER(1)     DEFAULT 1,
    created_at  TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_evt_date ON events(event_date);
CREATE INDEX idx_evt_type ON events(event_type);

-- 9. Subsidiaries / participated entities
CREATE TABLE subsidiaries (
    id                       NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    entity_name              VARCHAR2(500) NOT NULL,
    description              CLOB,
    participation_percentage NUMBER(5,2),
    country                  VARCHAR2(100),
    website_url              VARCHAR2(1000),
    is_active                NUMBER(1)     DEFAULT 1,
    created_at               TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_subs_country ON subsidiaries(country);

-- 10. General Assembly documents
CREATE TABLE general_assembly_documents (
    id            NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    assembly_year NUMBER(5)     NOT NULL,
    title         VARCHAR2(500) NOT NULL,
    document_url  VARCHAR2(1000) NOT NULL,
    assembly_date DATE,
    document_type VARCHAR2(100),
    created_at    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_gad_year ON general_assembly_documents(assembly_year);

-- 11. Investor Relations
CREATE TABLE investor_relations (
    id             NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email          VARCHAR2(255),
    phone          VARCHAR2(100),
    address        CLOB,
    other_contacts CLOB,
    updated_at     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);

-- 12. External auditors
CREATE TABLE external_auditors (
    id           NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    auditor_name VARCHAR2(500) NOT NULL,
    contact_info CLOB,
    period_from  DATE,
    period_to    DATE,
    is_current   NUMBER(1)     DEFAULT 1,
    updated_at   TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_ea_current ON external_auditors(is_current);
