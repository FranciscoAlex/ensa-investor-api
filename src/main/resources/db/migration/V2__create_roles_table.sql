-- V2__create_roles_table.sql
CREATE SEQUENCE role_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE roles (
    id NUMBER(19) PRIMARY KEY,
    name VARCHAR2(30) NOT NULL UNIQUE,
    description VARCHAR2(255)
);

CREATE TABLE user_roles (
    user_id NUMBER(19) NOT NULL,
    role_id NUMBER(19) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Default Roles
INSERT INTO roles (id, name, description) VALUES (role_seq.nextval, 'ADMIN', 'System Administrator');
INSERT INTO roles (id, name, description) VALUES (role_seq.nextval, 'INVESTOR', 'Standard Investor');
INSERT INTO roles (id, name, description) VALUES (role_seq.nextval, 'ANALYST', 'Investment Analyst');
INSERT INTO roles (id, name, description) VALUES (role_seq.nextval, 'MANAGER', 'Portfolio Manager');
