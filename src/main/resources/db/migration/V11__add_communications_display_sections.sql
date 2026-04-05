-- V11: Add display_sections to communications
-- Comma-separated flags controlling which pages render each item.
-- Valid tokens: HOME, COMUNICADOS, RELATORIOS
-- Default: 'HOME,COMUNICADOS' preserves current behaviour for all existing records.

ALTER TABLE communications ADD COLUMN display_sections VARCHAR(255) DEFAULT 'HOME,COMUNICADOS';
UPDATE communications SET display_sections = 'HOME,COMUNICADOS' WHERE display_sections IS NULL;
