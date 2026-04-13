-- V11__add_communications_display_sections.sql (Oracle)
ALTER TABLE communications ADD (display_sections VARCHAR2(255) DEFAULT 'HOME,COMUNICADOS');
UPDATE communications SET display_sections = 'HOME,COMUNICADOS' WHERE display_sections IS NULL;
