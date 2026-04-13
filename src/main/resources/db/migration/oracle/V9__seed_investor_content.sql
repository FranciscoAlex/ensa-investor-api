-- V9__seed_investor_content.sql (Oracle)
-- NOTE: replaces MySQL's SET @var = LAST_INSERT_ID() with subqueries on slug_id

-- 1. Historical milestones
INSERT INTO historical_milestones (title, event_title, description, image_url, content_html, milestone_year, display_order, is_active, created_at)
VALUES (
    'Fundação e Início', 'Fundação e Início',
    'A ENSA nasce em 18 de fevereiro de 1978 como a seguradora líder de Angola.',
    'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80',
    '<p>A ENSA – Seguros de Angola, S.A. foi fundada pelo Decreto n.º 18/78 de 18 de Fevereiro.</p>',
    1978, 1, 1, CURRENT_TIMESTAMP);

INSERT INTO historical_milestones (title, event_title, description, image_url, content_html, milestone_year, display_order, is_active, created_at)
VALUES (
    'Sociedade Anónima', 'Sociedade Anónima',
    'Um marco de modernização: a transformação em S.A. permitiu à ENSA elevar os seus padrões de governação.',
    NULL,
    '<p>A transformação da ENSA em Sociedade Anónima em 2002 marcou o início de uma nova era de gestão corporativa.</p>',
    2002, 2, 1, CURRENT_TIMESTAMP);

INSERT INTO historical_milestones (title, event_title, description, image_url, content_html, milestone_year, display_order, is_active, created_at)
VALUES (
    'Liderança de Mercado', 'Liderança de Mercado',
    'Consolidação como líder absoluta no mercado segurador e na gestão de fundos de pensões.',
    NULL,
    '<p>Em 2015, a ENSA atingiu lucros recorde e consolidou a sua posição de liderança com uma quota de mercado superior a 30%.</p>',
    2015, 3, 1, CURRENT_TIMESTAMP);

INSERT INTO historical_milestones (title, event_title, description, image_url, content_html, milestone_year, display_order, is_active, created_at)
VALUES (
    'IPO e Sociedade Aberta', 'IPO e Sociedade Aberta',
    'A conclusão bem-sucedida da Oferta Pública Inicial (IPO) marca uma nova era. A ENSA torna-se uma Sociedade Aberta na BODIVA.',
    NULL,
    '<p>O Outubro de 2024 ficará gravado na história financeira de Angola. A ENSA concluiu o seu processo de privatização parcial.</p>',
    2024, 4, 1, CURRENT_TIMESTAMP);

-- 2. Events
INSERT INTO events (title, description, event_date, location, event_type, is_active, created_at)
VALUES ('Apresentação de Resultados Q1',
    'Análise detalhada do desempenho financeiro do primeiro trimestre de 2025.',
    TIMESTAMP '2025-05-15 09:00:00', 'Sede da ENSA / Online (Webcast)', 'Resultados', 1, CURRENT_TIMESTAMP);

INSERT INTO events (title, description, event_date, location, event_type, is_active, created_at)
VALUES ('Assembleia Geral Ordinária',
    'Reunião anual de accionistas para discussão de contas e estratégia.',
    TIMESTAMP '2025-08-20 10:00:00', 'Auditório da ENSA, Luanda', 'Reunião', 1, CURRENT_TIMESTAMP);

INSERT INTO events (title, description, event_date, location, event_type, is_active, created_at)
VALUES ('Publicação do Relatório Intercalar',
    'Divulgação dos resultados do terceiro trimestre e perspetivas para o fecho do ano.',
    TIMESTAMP '2025-11-10 00:00:00', 'Portal do Investidor', 'Relatório', 1, CURRENT_TIMESTAMP);

-- 3. Communications
INSERT INTO communications (slug_id, title, category, communication_type, summary, content_html, published_at, is_active, created_at)
VALUES ('PR-2025-001', 'ENSA aprova distribuição de dividendos históricos de 4.3 mil milhões AOA',
    'Finanças', 'PressRelease',
    'Primeira distribuição de dividendos como sociedade aberta cotada na BODIVA.',
    '<p>A ENSA anunciou a aprovação da distribuição de dividendos históricos relativos ao exercício económico de 2024.</p>',
    TIMESTAMP '2025-04-15 00:00:00', 1, CURRENT_TIMESTAMP);

INSERT INTO communications (slug_id, title, category, communication_type, summary, content_html, published_at, is_active, created_at)
VALUES ('PR-2025-002', 'Relatório e Contas 2024: ENSA consolida liderança com lucros de 8 mil milhões AOA',
    'Resultados', 'PressRelease',
    'Resultados anuais confirmam quota de mercado de 27%.',
    '<p>O exercício de 2024 foi marcado por um desempenho operacional excepcional.</p>',
    TIMESTAMP '2025-02-28 00:00:00', 1, CURRENT_TIMESTAMP);

INSERT INTO communications (slug_id, title, category, communication_type, summary, published_at, is_active, created_at)
VALUES ('PR-2024-005', 'Sucesso absoluto na OPV: Procura pelas acções da ENSA supera oferta em 174%',
    'Corporativo', 'PressRelease',
    'Conclusão da privatização marca a entrada de 1.115 novos investidores.',
    TIMESTAMP '2024-10-28 00:00:00', 1, CURRENT_TIMESTAMP);

INSERT INTO communications (slug_id, title, category, communication_type, summary, published_at, is_active, created_at)
VALUES ('PR-2024-004', 'ENSA lança "Uma Seguradora Para Todas as Vidas" - Nova visão estratégica',
    'Estratégia', 'PressRelease',
    'A nova narrativa foca na humanização do seguro.',
    TIMESTAMP '2024-09-15 00:00:00', 1, CURRENT_TIMESTAMP);

INSERT INTO communications (slug_id, title, category, communication_type, summary, published_at, is_active, created_at)
VALUES ('PR-2024-003', 'ENSA atinge marco histórico de 100 mil milhões AOA em prémios emitidos',
    'Operacional', 'PressRelease',
    'Performance recorde nos primeiros oito meses do ano.',
    TIMESTAMP '2024-08-02 00:00:00', 1, CURRENT_TIMESTAMP);

-- 4. Financial statements
INSERT INTO financial_statements (year, title, document_url, statement_type, language, created_at) VALUES (2025, 'Resultados 1º Trimestre 2025',       '#', 'Relatório',      'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (year, title, document_url, statement_type, language, created_at) VALUES (2025, 'Apresentação Institucional T1',       '#', 'Apresentação',   'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (year, title, document_url, statement_type, language, created_at) VALUES (2025, 'Boletim Estatístico T1',             '#', 'Dados',          'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (year, title, document_url, statement_type, language, created_at) VALUES (2025, 'Comunicado de Imprensa - Março',      '#', 'Comunicado',     'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (year, title, document_url, statement_type, language, created_at) VALUES (2024, 'Relatório e Contas 2024',             '#', 'Relatório Anual','pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (year, title, document_url, statement_type, language, created_at) VALUES (2024, 'Relatório de Sustentabilidade',       '#', 'ESG',            'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (year, title, document_url, statement_type, language, created_at) VALUES (2024, 'Resultados 4º Trimestre 2024',        '#', 'Relatório',      'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (year, title, document_url, statement_type, language, created_at) VALUES (2024, 'Resultados 3º Trimestre 2024',        '#', 'Relatório',      'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (year, title, document_url, statement_type, language, created_at) VALUES (2024, 'Governo Societário 2024',             '#', 'Governação',     'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (year, title, document_url, statement_type, language, created_at) VALUES (2023, 'Relatório e Contas 2023',             '#', 'Relatório Anual','pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (year, title, document_url, statement_type, language, created_at) VALUES (2023, 'Resultados 4º Trimestre 2023',        '#', 'Relatório',      'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (year, title, document_url, statement_type, language, created_at) VALUES (2023, 'Apresentação de Resultados 2023',     '#', 'Apresentação',   'pt', CURRENT_TIMESTAMP);

-- 5. Business indicators (quarterly results)
INSERT INTO business_indicators (title, indicator_value, period_year, period_quarter, category, unit, created_at) VALUES ('Receita T2 2025',       '97.3B', 2025, 2, 'QUARTERLY_RESULTS', 'AOA', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (title, indicator_value, period_year, period_quarter, category, unit, created_at) VALUES ('EPS T2 2025',           '1.56',  2025, 2, 'QUARTERLY_RESULTS', 'AOA', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (title, indicator_value, period_year, period_quarter, category, unit, created_at) VALUES ('Margem Bruta T2 2025',  '45.2%', 2025, 2, 'QUARTERLY_RESULTS', '%',   CURRENT_TIMESTAMP);
INSERT INTO business_indicators (title, indicator_value, period_year, period_quarter, category, unit, created_at) VALUES ('YoY T2 2025',           '+5%',   2025, 2, 'QUARTERLY_RESULTS', '%',   CURRENT_TIMESTAMP);
INSERT INTO business_indicators (title, indicator_value, period_year, period_quarter, category, unit, created_at) VALUES ('Receita T1 2025',       '120.1B',2025, 1, 'QUARTERLY_RESULTS', 'AOA', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (title, indicator_value, period_year, period_quarter, category, unit, created_at) VALUES ('EPS T1 2025',           '2.15',  2025, 1, 'QUARTERLY_RESULTS', 'AOA', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (title, indicator_value, period_year, period_quarter, category, unit, created_at) VALUES ('Margem Bruta T1 2025',  '46.0%', 2025, 1, 'QUARTERLY_RESULTS', '%',   CURRENT_TIMESTAMP);
INSERT INTO business_indicators (title, indicator_value, period_year, period_quarter, category, unit, created_at) VALUES ('YoY T1 2025',           '+3%',   2025, 1, 'QUARTERLY_RESULTS', '%',   CURRENT_TIMESTAMP);
INSERT INTO business_indicators (title, indicator_value, period_year, period_quarter, category, unit, created_at) VALUES ('Receita T4 2024',       '89.5B', 2024, 4, 'QUARTERLY_RESULTS', 'AOA', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (title, indicator_value, period_year, period_quarter, category, unit, created_at) VALUES ('EPS T4 2024',           '1.44',  2024, 4, 'QUARTERLY_RESULTS', 'AOA', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (title, indicator_value, period_year, period_quarter, category, unit, created_at) VALUES ('Margem Bruta T4 2024',  '44.7%', 2024, 4, 'QUARTERLY_RESULTS', '%',   CURRENT_TIMESTAMP);
INSERT INTO business_indicators (title, indicator_value, period_year, period_quarter, category, unit, created_at) VALUES ('YoY T4 2024',           '+2%',   2024, 4, 'QUARTERLY_RESULTS', '%',   CURRENT_TIMESTAMP);

-- 6. Shareholder structure
INSERT INTO shareholder_structure (shareholder_name, shares_label, percentage, display_color, display_order, is_active, created_at)
VALUES ('IGAPE - INSTITUTO DE GESTÃO DE ACTIVOS E PARTICIPAÇÕES DO ESTADO', '1.680.000', 70.0000, '#164993', 1, 1, CURRENT_TIMESTAMP);
INSERT INTO shareholder_structure (shareholder_name, shares_label, percentage, display_color, display_order, is_active, created_at)
VALUES ('FUNDO DE PENSÕES DA SONANGOL', '164.121', 6.8400, '#e63c2e', 2, 1, CURRENT_TIMESTAMP);
INSERT INTO shareholder_structure (shareholder_name, shares_label, percentage, display_color, display_order, is_active, created_at)
VALUES ('NOBLE GROUP S.A', '162.784', 6.7800, '#10b981', 3, 1, CURRENT_TIMESTAMP);
INSERT INTO shareholder_structure (shareholder_name, shares_label, percentage, display_color, display_order, is_active, created_at)
VALUES ('OUTROS', '393.095', 16.3800, '#f59e0b', 4, 1, CURRENT_TIMESTAMP);

-- 7. General Assemblies (Ordinary)
-- AG-2024
INSERT INTO general_assemblies (slug_id, title, meeting_year, meeting_date, status, assembly_type, summary, display_order, is_active, created_at)
VALUES ('ag-2024', 'Assembleia Geral Ordinária de 2024', 2024,
    '10 de Abril de 2025', 'Realizada', 'Ordinária',
    'A Assembleia Geral Ordinária de 2024 decorreu com quórum total, onde foram aprovadas as contas por unanimidade.',
    1, 1, CURRENT_TIMESTAMP);

INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2024'), 'Apreciação do Relatório de Gestão e Contas do exercício de 2024.', 1);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2024'), 'Aprovação da proposta de aplicação dos resultados do exercício de 2024.', 2);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2024'), 'Avaliação geral do desempenho do Conselho de Administração e do Conselho Fiscal.', 3);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2024'), 'Apreciação do Relatório de Governo Societário referente ao ano de 2024.', 4);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2024'), 'Deliberação sobre o Relatório da Comissão de Remunerações.', 5);

INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2024'), 2024, 'Convocatória AGO 2024',               '#', 'PDF', '1.2 MB', DATE '2025-04-10', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2024'), 2024, 'Relatório e Contas 2024',             '#', 'PDF', '8.4 MB', DATE '2025-04-10', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2024'), 2024, 'Relatório de Governo Societário 2024','#', 'PDF', '2.1 MB', DATE '2025-04-10', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2024'), 2024, 'Acta da Assembleia Geral 2024',       '#', 'PDF', '1.2 MB', DATE '2025-04-10', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2024'), 2024, 'Parecer do Conselho Fiscal 2024',     '#', 'PDF', '0.9 MB', DATE '2025-04-10', CURRENT_TIMESTAMP);

-- AG-2023
INSERT INTO general_assemblies (slug_id, title, meeting_year, meeting_date, status, assembly_type, summary, display_order, is_active, created_at)
VALUES ('ag-2023', 'Assembleia Geral Ordinária de 2023', 2023,
    '18 de Abril de 2024', 'Realizada', 'Ordinária',
    'Em 2023, a assembleia focou-se na expansão estratégica e na renovação dos órgãos de fiscalização.',
    2, 1, CURRENT_TIMESTAMP);

INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2023'), 'Relatório de Gestão e Contas do exercício de 2023.', 1);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2023'), 'Aplicação de Resultados e Dividendos.', 2);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2023'), 'Eleição de novos membros para o Conselho Fiscal.', 3);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2023'), 'Aprovação do Plano Estratégico 2024-2026.', 4);

INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2023'), 2023, 'Relatório e Contas 2023',  '#', 'PDF', '7.8 MB', DATE '2024-04-18', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2023'), 2023, 'Convocatória AGO 2023',    '#', 'PDF', '1.1 MB', DATE '2024-04-18', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'ag-2023'), 2023, 'Acta da Assembleia Geral 2023', '#', 'PDF', '1.5 MB', DATE '2024-04-18', CURRENT_TIMESTAMP);

-- AGE-2025 Eleitoral
INSERT INTO general_assemblies (slug_id, title, meeting_year, meeting_date, status, assembly_type, summary, display_order, is_active, created_at)
VALUES ('age-2025', 'Eleição dos Órgãos Sociais (2026-2029)', 2025,
    '16 de Dezembro de 2025', 'Convocada', 'Eleitoral',
    'Eleição e constituição dos novos órgãos sociais para o mandato 2026-2029.',
    1, 1, CURRENT_TIMESTAMP);

INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'age-2025'), 'Eleição dos membros da Mesa da Assembleia Geral.', 1);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'age-2025'), 'Eleição dos membros do Conselho de Administração.', 2);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'age-2025'), 'Eleição dos membros do Conselho Fiscal.', 3);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'age-2025'), 'Fixação da caução a prestar pelos administradores e fiscais.', 4);
INSERT INTO general_assembly_agenda_items (assembly_id, item_text, display_order)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'age-2025'), 'Deliberação sobre a Política de Remuneração dos Órgãos Sociais.', 5);

INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'age-2025'), 2025, 'Convocatória AGE Dez 2025',          '#', 'PDF', '1.2 MB', DATE '2025-12-16', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'age-2025'), 2025, 'Proposta Lista A - Administração',   '#', 'PDF', '3.4 MB', DATE '2025-12-16', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'age-2025'), 2025, 'Proposta Lista B - Administração',   '#', 'PDF', '2.8 MB', DATE '2025-12-16', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'age-2025'), 2025, 'Curriculum Vitae dos Candidatos',    '#', 'PDF', '5.1 MB', DATE '2025-12-16', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES ((SELECT id FROM general_assemblies WHERE slug_id = 'age-2025'), 2025, 'Guia de Procedimento de Votação',    '#', 'PDF', '0.9 MB', DATE '2025-12-16', CURRENT_TIMESTAMP);
