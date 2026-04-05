-- V1__create_users_table.sql
CREATE SEQUENCE user_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE investor_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
    id NUMBER(19) PRIMARY KEY,
    username VARCHAR2(50) NOT NULL UNIQUE,
    email VARCHAR2(100) NOT NULL UNIQUE,
    password VARCHAR2(255) NOT NULL,
    full_name VARCHAR2(150) NOT NULL,
    phone_number VARCHAR2(20),
    nif VARCHAR2(20),
    is_enabled NUMBER(1) DEFAULT 0,
    is_locked NUMBER(1) DEFAULT 0,
    email_verified NUMBER(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    last_login_at TIMESTAMP
);

CREATE TABLE investor_profiles (
    id NUMBER(19) PRIMARY KEY,
    user_id NUMBER(19) NOT NULL UNIQUE,
    investor_code VARCHAR2(30) UNIQUE,
    document_type VARCHAR2(50),
    document_number VARCHAR2(50),
    date_of_birth DATE,
    nationality VARCHAR2(100),
    province VARCHAR2(100),
    address VARCHAR2(255),
    risk_profile VARCHAR2(30),
    total_invested NUMBER(18,2) DEFAULT 0,
    account_status VARCHAR2(30) DEFAULT 'PENDING_VERIFICATION',
    kyc_verified NUMBER(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_investor_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
