-- V4__seed_static_data.sql

-- Provinces of Angola
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('PROVINCES', 'LAD', 'Luanda', 'Luanda', 1);
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('PROVINCES', 'BGO', 'Bengo', 'Bengo', 2);
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('PROVINCES', 'BGU', 'Benguela', 'Benguela', 3);
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('PROVINCES', 'HUI', 'Huíla', 'Huila', 4);
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('PROVINCES', 'HUA', 'Huambo', 'Huambo', 5);
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('PROVINCES', 'CBI', 'Bié', 'Bie', 6);
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('PROVINCES', 'CAB', 'Cabinda', 'Cabinda', 7);
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('PROVINCES', 'CUN', 'Cunene', 'Cunene', 8);

-- Document Types
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('DOCUMENT_TYPES', 'BI', 'Bilhete de Identidade', 'Identity Card', 1);
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('DOCUMENT_TYPES', 'PASS', 'Passaporte', 'Passport', 2);

-- Currencies
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order, metadata) VALUES ('CURRENCIES', 'AOA', 'Kwanza', 'Angolan Kwanza', 1, '{"symbol": "Kz"}');
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order, metadata) VALUES ('CURRENCIES', 'USD', 'Dólar Americano', 'US Dollar', 2, '{"symbol": "$"}');
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order, metadata) VALUES ('CURRENCIES', 'EUR', 'Euro', 'Euro', 3, '{"symbol": "€"}');

-- Risk Profiles
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('RISK_PROFILES', 'CONSERVATIVE', 'Conservador', 'Conservative', 1);
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('RISK_PROFILES', 'MODERATE', 'Moderado', 'Moderate', 2);
INSERT INTO static_data_definitions (category, code, label_pt, label_en, sort_order) VALUES ('RISK_PROFILES', 'AGGRESSIVE', 'Agressivo', 'Aggressive', 3);
