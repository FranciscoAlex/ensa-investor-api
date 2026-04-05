-- V2__create_roles_table.sql
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT uq_roles_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Default Roles
INSERT INTO roles (name, description) VALUES ('ADMIN', 'System Administrator');
INSERT INTO roles (name, description) VALUES ('INVESTOR', 'Standard Investor');
INSERT INTO roles (name, description) VALUES ('ANALYST', 'Investment Analyst');
INSERT INTO roles (name, description) VALUES ('MANAGER', 'Portfolio Manager');
