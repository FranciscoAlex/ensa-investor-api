-- V1__create_users_table.sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    phone_number VARCHAR(20),
    nif VARCHAR(20),
    is_enabled TINYINT(1) DEFAULT 0,
    is_locked TINYINT(1) DEFAULT 0,
    email_verified TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    last_login_at TIMESTAMP NULL,
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email UNIQUE (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE investor_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    investor_code VARCHAR(30),
    document_type VARCHAR(50),
    document_number VARCHAR(50),
    date_of_birth DATE,
    nationality VARCHAR(100),
    province VARCHAR(100),
    address VARCHAR(255),
    risk_profile VARCHAR(30),
    total_invested DECIMAL(18,2) DEFAULT 0,
    account_status VARCHAR(30) DEFAULT 'PENDING_VERIFICATION',
    kyc_verified TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    CONSTRAINT uq_ip_user UNIQUE (user_id),
    CONSTRAINT uq_ip_investor_code UNIQUE (investor_code),
    CONSTRAINT fk_investor_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
