-- ============================================================
-- ENSA Investor Portal: Public content tables (MySQL)
-- ============================================================

-- 1. Historical milestones of ENSA
CREATE TABLE historical_milestones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    description LONGTEXT,
    milestone_year SMALLINT,
    display_order INT DEFAULT 0,
    is_active TINYINT(1) DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_hm_year ON historical_milestones(milestone_year);
CREATE INDEX idx_hm_order ON historical_milestones(display_order);

-- 2. BODIVA share / stock indicators history
CREATE TABLE bodiva_share_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    record_date DATE NOT NULL,
    share_price DECIMAL(18,4),
    volume BIGINT,
    opening_price DECIMAL(18,4),
    closing_price DECIMAL(18,4),
    high_price DECIMAL(18,4),
    low_price DECIMAL(18,4),
    notes VARCHAR(1000),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_bodiva_date ON bodiva_share_history(record_date);

-- 3. Board of Directors (CA) members with CV
CREATE TABLE board_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    role VARCHAR(255),
    bio LONGTEXT,
    cv_document_url VARCHAR(1000),
    photo_url VARCHAR(1000),
    display_order INT DEFAULT 0,
    is_active TINYINT(1) DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_bm_order ON board_members(display_order);

-- 4. Corporate governance reports
CREATE TABLE corporate_governance_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    document_url VARCHAR(1000) NOT NULL,
    report_year SMALLINT,
    language VARCHAR(10) DEFAULT 'pt',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_cgr_year ON corporate_governance_reports(report_year);

-- 5. Financial statements (reports and accounts, last 10 years)
CREATE TABLE financial_statements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    year SMALLINT NOT NULL,
    title VARCHAR(500) NOT NULL,
    document_url VARCHAR(1000) NOT NULL,
    statement_type VARCHAR(100),
    language VARCHAR(10) DEFAULT 'pt',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_fs_year ON financial_statements(year);

-- 6. Main business / financial indicators
CREATE TABLE business_indicators (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    indicator_value VARCHAR(500),
    numeric_value DECIMAL(18,4),
    period_year SMALLINT,
    period_quarter TINYINT(1),
    category VARCHAR(100),
    unit VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_bi_period ON business_indicators(period_year, period_quarter);
CREATE INDEX idx_bi_category ON business_indicators(category);

-- 7. Communications / press releases
CREATE TABLE communications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    communication_type VARCHAR(100),
    summary LONGTEXT,
    document_url VARCHAR(1000),
    published_at DATETIME NULL,
    is_active TINYINT(1) DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_comm_type ON communications(communication_type);
CREATE INDEX idx_comm_published ON communications(published_at);

-- 8. Events and meetings calendar
CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    description LONGTEXT,
    event_date DATETIME NULL,
    end_date DATETIME NULL,
    location VARCHAR(500),
    event_type VARCHAR(100),
    is_active TINYINT(1) DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_evt_date ON events(event_date);
CREATE INDEX idx_evt_type ON events(event_type);

-- 9. Subsidiaries / participated entities portfolio
CREATE TABLE subsidiaries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_name VARCHAR(500) NOT NULL,
    description LONGTEXT,
    participation_percentage DECIMAL(5,2),
    country VARCHAR(100),
    website_url VARCHAR(1000),
    is_active TINYINT(1) DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_subs_country ON subsidiaries(country);

-- 10. General Assembly documents
CREATE TABLE general_assembly_documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    assembly_year SMALLINT NOT NULL,
    title VARCHAR(500) NOT NULL,
    document_url VARCHAR(1000) NOT NULL,
    assembly_date DATE NULL,
    document_type VARCHAR(100),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_gad_year ON general_assembly_documents(assembly_year);

-- 11. Investor Relations (contact info - typically one row)
CREATE TABLE investor_relations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255),
    phone VARCHAR(100),
    address LONGTEXT,
    other_contacts LONGTEXT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 12. External auditor information
CREATE TABLE external_auditors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    auditor_name VARCHAR(500) NOT NULL,
    contact_info LONGTEXT,
    period_from DATE NULL,
    period_to DATE NULL,
    is_current TINYINT(1) DEFAULT 1,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE INDEX idx_ea_current ON external_auditors(is_current);
