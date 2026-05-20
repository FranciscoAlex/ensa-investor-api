-- V1__create_users_table.sql (Oracle)
CREATE TABLE users (
    id              NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username        VARCHAR2(50)  NOT NULL,
    email           VARCHAR2(100) NOT NULL,
    password        VARCHAR2(255) NOT NULL,
    full_name       VARCHAR2(150) NOT NULL,
    phone_number    VARCHAR2(20),
    nif             VARCHAR2(20),
    is_enabled      NUMBER(1)     DEFAULT 0,
    is_locked       NUMBER(1)     DEFAULT 0,
    email_verified  NUMBER(1)     DEFAULT 0,
    created_at      TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP,
    last_login_at   TIMESTAMP,
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email    UNIQUE (email)
);

CREATE TABLE investor_profiles (
    id                    NUMBER(19)    GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id               NUMBER(19)    NOT NULL,
    investor_code         VARCHAR2(30),
    document_type         VARCHAR2(50),
    document_number       VARCHAR2(50),
    date_of_birth         DATE,
    nationality           VARCHAR2(100),
    province              VARCHAR2(100),
    address               VARCHAR2(255),
    risk_profile          VARCHAR2(30),
    total_invested        NUMBER(18,2)  DEFAULT 0,
    account_status        VARCHAR2(30)  DEFAULT 'PENDING_VERIFICATION',
    kyc_verified          NUMBER(1)     DEFAULT 0,
    created_at            TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP,
    CONSTRAINT uq_ip_user          UNIQUE (user_id),
    CONSTRAINT uq_ip_investor_code UNIQUE (investor_code),
    CONSTRAINT fk_investor_user    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
