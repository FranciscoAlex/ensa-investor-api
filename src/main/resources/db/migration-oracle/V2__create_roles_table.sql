-- V2__create_roles_table.sql (Oracle)
CREATE TABLE roles (
    id          NUMBER(19)   GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR2(30) NOT NULL,
    description VARCHAR2(255),
    CONSTRAINT uq_roles_name UNIQUE (name)
);

CREATE TABLE user_roles (
    user_id NUMBER(19) NOT NULL,
    role_id NUMBER(19) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

INSERT INTO roles (name, description) VALUES ('ADMIN',    'System Administrator');
INSERT INTO roles (name, description) VALUES ('INVESTOR', 'Standard Investor');
INSERT INTO roles (name, description) VALUES ('ANALYST',  'Investment Analyst');
INSERT INTO roles (name, description) VALUES ('MANAGER',  'Portfolio Manager');
