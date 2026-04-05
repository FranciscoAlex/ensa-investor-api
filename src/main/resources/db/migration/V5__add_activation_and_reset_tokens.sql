-- Add columns for account activation and password reset links
ALTER TABLE users
    ADD COLUMN activation_token VARCHAR(255),
    ADD COLUMN activation_token_expires_at TIMESTAMP NULL,
    ADD COLUMN password_reset_token VARCHAR(255),
    ADD COLUMN password_reset_token_expires_at TIMESTAMP NULL;

CREATE INDEX idx_users_activation_token ON users(activation_token);
CREATE INDEX idx_users_password_reset_token ON users(password_reset_token);
