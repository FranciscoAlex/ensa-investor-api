-- Add columns for account activation and password reset links
ALTER TABLE users ADD (
    activation_token VARCHAR2(255),
    activation_token_expires_at TIMESTAMP,
    password_reset_token VARCHAR2(255),
    password_reset_token_expires_at TIMESTAMP
);

CREATE INDEX idx_users_activation_token ON users(activation_token);
CREATE INDEX idx_users_password_reset_token ON users(password_reset_token);
