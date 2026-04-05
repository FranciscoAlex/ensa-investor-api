-- ============================================================
-- ENSA Investor Portal: Public content tables (English API)
-- ============================================================

-- 1. Historical milestones of ENSA
CREATE SEQUENCE historical_milestone_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE historical_milestones (
    id NUMBER(19) PRIMARY KEY,
    title VARCHAR2(500) NOT NULL,
    description CLOB,
    milestone_year NUMBER(4),
    display_order NUMBER(5) DEFAULT 0,
    is_active NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);
CREATE INDEX idx_hm_year ON historical_milestones(milestone_year);
CREATE INDEX idx_hm_order ON historical_milestones(display_order);

-- 2. BODIVA share / stock indicators history
CREATE SEQUENCE bodiva_share_history_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE bodiva_share_history (
    id NUMBER(19) PRIMARY KEY,
    record_date DATE NOT NULL,
    share_price NUMBER(18,4),
    volume NUMBER(19),
    opening_price NUMBER(18,4),
    closing_price NUMBER(18,4),
    high_price NUMBER(18,4),
    low_price NUMBER(18,4),
    notes VARCHAR2(1000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_bodiva_date ON bodiva_share_history(record_date);

-- 3. Board of Directors (CA) members with CV
CREATE SEQUENCE board_member_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE board_members (
    id NUMBER(19) PRIMARY KEY,
    full_name VARCHAR2(255) NOT NULL,
    role VARCHAR2(255),
    bio CLOB,
    cv_document_url VARCHAR2(1000),
    photo_url VARCHAR2(1000),
    display_order NUMBER(5) DEFAULT 0,
    is_active NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);
CREATE INDEX idx_bm_order ON board_members(display_order);

-- 4. Corporate governance reports
CREATE SEQUENCE corp_governance_report_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE corporate_governance_reports (
    id NUMBER(19) PRIMARY KEY,
    title VARCHAR2(500) NOT NULL,
    document_url VARCHAR2(1000) NOT NULL,
    report_year NUMBER(4),
    language VARCHAR2(10) DEFAULT 'pt',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_cgr_year ON corporate_governance_reports(report_year);

-- 5. Financial statements (reports and accounts, last 10 years)
CREATE SEQUENCE financial_statement_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE financial_statements (
    id NUMBER(19) PRIMARY KEY,
    year NUMBER(4) NOT NULL,
    title VARCHAR2(500) NOT NULL,
    document_url VARCHAR2(1000) NOT NULL,
    statement_type VARCHAR2(100),
    language VARCHAR2(10) DEFAULT 'pt',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_fs_year ON financial_statements(year);

-- 6. Main business / financial indicators
CREATE SEQUENCE business_indicator_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE business_indicators (
    id NUMBER(19) PRIMARY KEY,
    title VARCHAR2(500) NOT NULL,
    indicator_value VARCHAR2(500),
    numeric_value NUMBER(18,4),
    period_year NUMBER(4),
    period_quarter NUMBER(1),
    category VARCHAR2(100),
    unit VARCHAR2(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_bi_period ON business_indicators(period_year, period_quarter);
CREATE INDEX idx_bi_category ON business_indicators(category);

-- 7. Communications / press releases
CREATE SEQUENCE communication_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE communications (
    id NUMBER(19) PRIMARY KEY,
    title VARCHAR2(500) NOT NULL,
    communication_type VARCHAR2(100),
    summary CLOB,
    document_url VARCHAR2(1000),
    published_at TIMESTAMP,
    is_active NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_comm_type ON communications(communication_type);
CREATE INDEX idx_comm_published ON communications(published_at);

-- 8. Events and meetings calendar
CREATE SEQUENCE event_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE events (
    id NUMBER(19) PRIMARY KEY,
    title VARCHAR2(500) NOT NULL,
    description CLOB,
    event_date TIMESTAMP,
    end_date TIMESTAMP,
    location VARCHAR2(500),
    event_type VARCHAR2(100),
    is_active NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_evt_date ON events(event_date);
CREATE INDEX idx_evt_type ON events(event_type);

-- 9. Subsidiaries / participated entities portfolio
CREATE SEQUENCE subsidiary_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE subsidiaries (
    id NUMBER(19) PRIMARY KEY,
    entity_name VARCHAR2(500) NOT NULL,
    description CLOB,
    participation_percentage NUMBER(5,2),
    country VARCHAR2(100),
    website_url VARCHAR2(1000),
    is_active NUMBER(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_subs_country ON subsidiaries(country);

-- 10. General Assembly documents
CREATE SEQUENCE general_assembly_doc_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE general_assembly_documents (
    id NUMBER(19) PRIMARY KEY,
    assembly_year NUMBER(4) NOT NULL,
    title VARCHAR2(500) NOT NULL,
    document_url VARCHAR2(1000) NOT NULL,
    assembly_date DATE,
    document_type VARCHAR2(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_gad_year ON general_assembly_documents(assembly_year);

-- 11. Investor Relations (contact info - typically one row)
CREATE SEQUENCE investor_relations_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE investor_relations (
    id NUMBER(19) PRIMARY KEY,
    email VARCHAR2(255),
    phone VARCHAR2(100),
    address CLOB,
    other_contacts CLOB,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 12. External auditor information
CREATE SEQUENCE external_auditor_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE external_auditors (
    id NUMBER(19) PRIMARY KEY,
    auditor_name VARCHAR2(500) NOT NULL,
    contact_info CLOB,
    period_from DATE,
    period_to DATE,
    is_current NUMBER(1) DEFAULT 1,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_ea_current ON external_auditors(is_current);
