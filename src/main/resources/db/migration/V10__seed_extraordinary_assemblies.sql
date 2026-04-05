-- V10: Seed Extraordinária assemblies
-- -----------------------------------------------------------------

-- AGE 2025: Aumento de Capital
INSERT INTO general_assemblies (slug_id, title, meeting_year, meeting_date, status, assembly_type, summary, display_order, is_active, created_at)
VALUES ('age-capital-2025', 'Assembleia Geral Extraordinária — Aumento de Capital', 2025,
    '15 de Setembro de 2025', 'Realizada', 'Extraordinária',
    'Reunião extraordinária para deliberar sobre o aumento de capital social da ENSA, aprovação de emissão de novas acções e alteração dos estatutos sociais em conformidade.',
    1, 1, CURRENT_TIMESTAMP);
SET @assembly_id = LAST_INSERT_ID();

INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES (@assembly_id, 'Aprovação do aumento de capital social em 15 mil milhões AOA.', 1);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES (@assembly_id, 'Modalidade da emissão de novas acções e preço de emissão.', 2);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES (@assembly_id, 'Alteração do artigo 5.º dos Estatutos (Capital Social).', 3);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES (@assembly_id, 'Autorização do Conselho de Administração para execução das deliberações.', 4);

INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (@assembly_id, 2025, 'Convocatória AGE — Setembro 2025', '#', 'PDF', '1.0 MB', '2025-09-15', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (@assembly_id, 2025, 'Proposta de Aumento de Capital', '#', 'PDF', '2.3 MB', '2025-09-15', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (@assembly_id, 2025, 'Projecto de Alteração dos Estatutos', '#', 'PDF', '1.8 MB', '2025-09-15', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (@assembly_id, 2025, 'Acta da AGE — Setembro 2025', '#', 'PDF', '1.4 MB', '2025-09-15', CURRENT_TIMESTAMP);

-- AGE 2024: Fusão e Aquisição
INSERT INTO general_assemblies (slug_id, title, meeting_year, meeting_date, status, assembly_type, summary, display_order, is_active, created_at)
VALUES ('age-fusao-2024', 'Assembleia Geral Extraordinária — Operação de Fusão', 2024,
    '20 de Junho de 2024', 'Realizada', 'Extraordinária',
    'Deliberação sobre a operação de fusão por incorporação de participada e aprovação das condições da operação, incluindo a relação de troca e avaliação independente.',
    2, 1, CURRENT_TIMESTAMP);
SET @assembly_id = LAST_INSERT_ID();

INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES (@assembly_id, 'Apresentação e aprovação do Projecto de Fusão.', 1);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES (@assembly_id, 'Aprovação da relação de troca de acções e paridade.', 2);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES (@assembly_id, 'Tomada de conhecimento da avaliação independente da sociedade incorporada.', 3);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES (@assembly_id, 'Autorização para registo e formalização da fusão.', 4);

INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (@assembly_id, 2024, 'Convocatória AGE — Junho 2024', '#', 'PDF', '0.9 MB', '2024-06-20', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (@assembly_id, 2024, 'Projecto de Fusão', '#', 'PDF', '4.2 MB', '2024-06-20', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (@assembly_id, 2024, 'Relatório de Avaliação Independente', '#', 'PDF', '3.1 MB', '2024-06-20', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (@assembly_id, 2024, 'Acta da AGE — Junho 2024', '#', 'PDF', '1.3 MB', '2024-06-20', CURRENT_TIMESTAMP);
