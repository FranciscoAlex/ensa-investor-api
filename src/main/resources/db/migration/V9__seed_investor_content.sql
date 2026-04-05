-- ============================================================
-- V9: Seed all hardcoded frontend data into the backend tables
--     Sources: src/data/mockData.ts, newsData.ts, reportsData.ts
-- ============================================================

-- -----------------------------------------------------------------
-- 1. Historical milestones (historyData)
-- -----------------------------------------------------------------
INSERT INTO historical_milestones (id, title, event_title, description, image_url, content_html, milestone_year, display_order, is_active, created_at)
VALUES (historical_milestone_seq.NEXTVAL,
    'Fundação e Início',
    'Fundação e Início',
    'A ENSA nasce em 18 de fevereiro de 1978 como a seguradora líder de Angola. Inicialmente como U.E.E., estabeleceu os alicerces do mercado nacional com início de atividade em abril do mesmo ano.',
    'https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?ixlib=rb-4.0.3&auto=format&fit=crop&w=1200&q=80',
    '<p>A ENSA – Seguros de Angola, S.A. foi fundada pelo Decreto n.º 18/78 de 18 de Fevereiro, com a missão de dinamizar o sector segurador no país. Desde o seu início, assumiu um papel preponderante na protecção de activos nacionais e no apoio ao desenvolvimento económico.</p><p>Nesta fase inicial, a empresa operava como Unidade Económica Estatal (U.E.E.), focada em estabelecer uma rede de cobertura básica para as principais indústrias que começavam a reerguer-se no período pós-independência.</p>',
    1978, 1, 1, CURRENT_TIMESTAMP);

INSERT INTO historical_milestones (id, title, event_title, description, image_url, content_html, milestone_year, display_order, is_active, created_at)
VALUES (historical_milestone_seq.NEXTVAL,
    'Sociedade Anónima',
    'Sociedade Anónima',
    'Um marco de modernização: a transformação em S.A. permitiu à ENSA elevar os seus padrões de governação e consolidar a sua posição estratégica.',
    null,
    '<p>A transformação da ENSA em Sociedade Anónima em 2002 marcou o início de uma nova era de gestão corporativa. Esta mudança não foi apenas jurídica, mas sim cultural e operacional.</p><p>Com maior autonomia e novos órgãos sociais, a companhia pôde investir em tecnologia e na especialização das suas equipas Técnicas, permitindo a entrada em mercados complexos como o de Petróleos e Aeronáutica com maior propriedade.</p>',
    2002, 2, 1, CURRENT_TIMESTAMP);

INSERT INTO historical_milestones (id, title, event_title, description, image_url, content_html, milestone_year, display_order, is_active, created_at)
VALUES (historical_milestone_seq.NEXTVAL,
    'Liderança de Mercado',
    'Liderança de Mercado',
    'Consolidação como líder absoluta no mercado segurador e na gestão de fundos de pensões, alcançando a maior cobertura nacional.',
    null,
    '<p>Em 2015, a ENSA atingiu lucros recorde e consolidou a sua posição de liderança com uma quota de mercado superior a 30%. O foco na excelência de serviço e na inovação de produtos como o ''ENSA Saúde'' permitiu um crescimento sustentado.</p><p>A rede de balcões estendeu-se a todas as dezoito províncias de Angola, tornando a ENSA a seguradora com maior penetração territorial do país.</p>',
    2015, 3, 1, CURRENT_TIMESTAMP);

INSERT INTO historical_milestones (id, title, event_title, description, image_url, content_html, milestone_year, display_order, is_active, created_at)
VALUES (historical_milestone_seq.NEXTVAL,
    'IPO e Sociedade Aberta',
    'IPO e Sociedade Aberta',
    'A conclusão bem-sucedida da Oferta Pública Inicial (IPO) marca uma nova era. A ENSA torna-se uma Sociedade Aberta na BODIVA.',
    null,
    '<p>O Outubro de 2024 ficará gravado na história financeira de Angola. A ENSA concluiu o seu processo de privatização parcial através de uma Oferta Pública Inicial (IPO) de 30% do seu capital social na BODIVA.</p><p>A elevada procura dos investidores, que superou a oferta em 1,74x, demonstrou a confiança absoluta no valor e na transparência da ENSA. Com a entrada de mais de 1.100 novos accionistas, a empresa arrecadou cerca de 9 mil milhões de Kz para o Estado e reforçou o seu compromisso com as melhores práticas de governação global.</p>',
    2024, 4, 1, CURRENT_TIMESTAMP);

-- -----------------------------------------------------------------
-- 2. Events (eventos)
-- -----------------------------------------------------------------
INSERT INTO events (id, title, description, event_date, location, event_type, is_active, created_at)
VALUES (event_seq.NEXTVAL,
    'Apresentação de Resultados Q1',
    'Análise detalhada do desempenho financeiro do primeiro trimestre de 2025. O evento contará com a presença do Conselho de Administração para apresentar os números principais e responder a perguntas de analistas e investidores.',
    TIMESTAMP '2025-05-15 09:00:00',
    'Sede da ENSA / Online (Webcast)',
    'Resultados', 1, CURRENT_TIMESTAMP);

INSERT INTO events (id, title, description, event_date, location, event_type, is_active, created_at)
VALUES (event_seq.NEXTVAL,
    'Assembleia Geral Ordinária',
    'Reunião anual de accionistas para discussão de contas e estratégia. A pauta inclui a aprovação do relatório de gestão, contas do exercício anterior e a proposta de aplicação de resultados.',
    TIMESTAMP '2025-08-20 10:00:00',
    'Auditório da ENSA, Luanda',
    'Reunião', 1, CURRENT_TIMESTAMP);

INSERT INTO events (id, title, description, event_date, location, event_type, is_active, created_at)
VALUES (event_seq.NEXTVAL,
    'Publicação do Relatório Intercalar',
    'Divulgação dos resultados do terceiro trimestre e perspetivas para o fecho do ano. O relatório estará disponível para download e incluirá comentários da gestão sobre a performance operacional.',
    TIMESTAMP '2025-11-10 00:00:00',
    'Portal do Investidor',
    'Relatório', 1, CURRENT_TIMESTAMP);

-- -----------------------------------------------------------------
-- 3. Press releases / Communications (releases in newsData.ts)
-- -----------------------------------------------------------------
INSERT INTO communications (id, slug_id, title, category, communication_type, summary, content_html, published_at, is_active, created_at)
VALUES (communication_seq.NEXTVAL,
    'PR-2025-001',
    'ENSA aprova distribuição de dividendos históricos de 4.3 mil milhões AOA',
    'Finanças',
    'PressRelease',
    'Primeira distribuição de dividendos como sociedade aberta cotada na BODIVA, correspondendo a 54% dos lucros de 2024.',
    '<p>A ENSA – Seguros de Angola, S.A., maior seguradora do mercado angolano, anunciou hoje em Assembleia Geral Ordinária a aprovação da distribuição de dividendos históricos relativos ao exercício económico de 2024.</p><p>Pela primeira vez na sua história como sociedade aberta cotada na BODIVA, a companhia irá distribuir um montante total de <strong>4.323.000.000 AOA</strong> pelos seus accionistas, o que representa um "payout" de 54% sobre os lucros líquidos auditados.</p><h2>Marcos Relevantes de 2024</h2><ul><li>Lucro Líquido Recorde de 8 mil milhões AOA.</li><li>Entrada na Bolsa (IPO) com procura superando a oferta em 174%.</li><li>Manutenção da liderança de mercado com 27% de quota.</li></ul><p>O PCA da ENSA destacou que este momento reflecte a solidez financeira da instituição e o compromisso assumido com os novos investidores durante o processo de privatização.</p>',
    TIMESTAMP '2025-04-15 00:00:00',
    1, CURRENT_TIMESTAMP);

INSERT INTO communications (id, slug_id, title, category, communication_type, summary, content_html, published_at, is_active, created_at)
VALUES (communication_seq.NEXTVAL,
    'PR-2025-002',
    'Relatório e Contas 2024: ENSA consolida liderança com lucros de 8 mil milhões AOA',
    'Resultados',
    'PressRelease',
    'Resultados anuais confirmam quota de mercado de 27% e crescimento sólido em todos os segmentos de seguros.',
    '<p>O exercício de 2024 foi marcado por um desempenho operacional excepcional, permitindo à ENSA consolidar a sua posição como o pilar da estabilidade seguradora em Angola.</p><p>Com um Resultado Líquido de 8 mil milhões AOA, a companhia demonstrou resiliência face aos desafios macroeconómicos, mantendo uma estratégia de diversificação de portefólio e rigor na aceitação de riscos.</p>',
    TIMESTAMP '2025-02-28 00:00:00',
    1, CURRENT_TIMESTAMP);

INSERT INTO communications (id, slug_id, title, category, communication_type, summary, published_at, is_active, created_at)
VALUES (communication_seq.NEXTVAL,
    'PR-2024-005',
    'Sucesso absoluto na OPV: Procura pelas acções da ENSA supera oferta em 174%',
    'Corporativo',
    'PressRelease',
    'Conclusão da privatização marca a entrada de 1.115 novos investidores na estrutura accionista da companhia.',
    TIMESTAMP '2024-10-28 00:00:00',
    1, CURRENT_TIMESTAMP);

INSERT INTO communications (id, slug_id, title, category, communication_type, summary, published_at, is_active, created_at)
VALUES (communication_seq.NEXTVAL,
    'PR-2024-004',
    'ENSA lança "Uma Seguradora Para Todas as Vidas" - Nova visão estratégica',
    'Estratégia',
    'PressRelease',
    'A nova narrativa foca na humanização do seguro e na simplificação do acesso através de canais digitais.',
    TIMESTAMP '2024-09-15 00:00:00',
    1, CURRENT_TIMESTAMP);

INSERT INTO communications (id, slug_id, title, category, communication_type, summary, published_at, is_active, created_at)
VALUES (communication_seq.NEXTVAL,
    'PR-2024-003',
    'ENSA atinge marco histórico de 100 mil milhões AOA em prémios emitidos',
    'Operacional',
    'PressRelease',
    'Performance recorde nos primeiros oito meses do ano demonstra a confiança do mercado angolano.',
    TIMESTAMP '2024-08-02 00:00:00',
    1, CURRENT_TIMESTAMP);

-- -----------------------------------------------------------------
-- 4. Financial statements / document downloads (downloadsByYear)
-- -----------------------------------------------------------------
INSERT INTO financial_statements (id, year, title, document_url, statement_type, language, created_at) VALUES
    (financial_statement_seq.NEXTVAL, 2025, 'Resultados 1º Trimestre 2025', '#', 'Relatório', 'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (id, year, title, document_url, statement_type, language, created_at) VALUES
    (financial_statement_seq.NEXTVAL, 2025, 'Apresentação Institucional T1', '#', 'Apresentação', 'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (id, year, title, document_url, statement_type, language, created_at) VALUES
    (financial_statement_seq.NEXTVAL, 2025, 'Boletim Estatístico T1', '#', 'Dados', 'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (id, year, title, document_url, statement_type, language, created_at) VALUES
    (financial_statement_seq.NEXTVAL, 2025, 'Comunicado de Imprensa - Março', '#', 'Comunicado', 'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (id, year, title, document_url, statement_type, language, created_at) VALUES
    (financial_statement_seq.NEXTVAL, 2024, 'Relatório e Contas 2024', '#', 'Relatório Anual', 'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (id, year, title, document_url, statement_type, language, created_at) VALUES
    (financial_statement_seq.NEXTVAL, 2024, 'Relatório de Sustentabilidade', '#', 'ESG', 'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (id, year, title, document_url, statement_type, language, created_at) VALUES
    (financial_statement_seq.NEXTVAL, 2024, 'Resultados 4º Trimestre 2024', '#', 'Relatório', 'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (id, year, title, document_url, statement_type, language, created_at) VALUES
    (financial_statement_seq.NEXTVAL, 2024, 'Resultados 3º Trimestre 2024', '#', 'Relatório', 'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (id, year, title, document_url, statement_type, language, created_at) VALUES
    (financial_statement_seq.NEXTVAL, 2024, 'Governo Societário 2024', '#', 'Governação', 'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (id, year, title, document_url, statement_type, language, created_at) VALUES
    (financial_statement_seq.NEXTVAL, 2023, 'Relatório e Contas 2023', '#', 'Relatório Anual', 'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (id, year, title, document_url, statement_type, language, created_at) VALUES
    (financial_statement_seq.NEXTVAL, 2023, 'Resultados 4º Trimestre 2023', '#', 'Relatório', 'pt', CURRENT_TIMESTAMP);
INSERT INTO financial_statements (id, year, title, document_url, statement_type, language, created_at) VALUES
    (financial_statement_seq.NEXTVAL, 2023, 'Apresentação de Resultados 2023', '#', 'Apresentação', 'pt', CURRENT_TIMESTAMP);

-- -----------------------------------------------------------------
-- 5. Quarterly financial results table (financialRows in reportsData.ts)
--    → stored as business_indicators with category = 'QUARTERLY_RESULTS'
-- -----------------------------------------------------------------
INSERT INTO business_indicators (id, title, indicator_value, period_year, period_quarter, category, unit, created_at)
VALUES (business_indicator_seq.NEXTVAL, 'Receita T2 2025', '97.3B', 2025, 2, 'QUARTERLY_RESULTS', 'AOA', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (id, title, indicator_value, period_year, period_quarter, category, unit, created_at)
VALUES (business_indicator_seq.NEXTVAL, 'EPS T2 2025', '1.56', 2025, 2, 'QUARTERLY_RESULTS', 'AOA', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (id, title, indicator_value, period_year, period_quarter, category, unit, created_at)
VALUES (business_indicator_seq.NEXTVAL, 'Margem Bruta T2 2025', '45.2%', 2025, 2, 'QUARTERLY_RESULTS', '%', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (id, title, indicator_value, period_year, period_quarter, category, unit, created_at)
VALUES (business_indicator_seq.NEXTVAL, 'YoY T2 2025', '+5%', 2025, 2, 'QUARTERLY_RESULTS', '%', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (id, title, indicator_value, period_year, period_quarter, category, unit, created_at)
VALUES (business_indicator_seq.NEXTVAL, 'Receita T1 2025', '120.1B', 2025, 1, 'QUARTERLY_RESULTS', 'AOA', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (id, title, indicator_value, period_year, period_quarter, category, unit, created_at)
VALUES (business_indicator_seq.NEXTVAL, 'EPS T1 2025', '2.15', 2025, 1, 'QUARTERLY_RESULTS', 'AOA', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (id, title, indicator_value, period_year, period_quarter, category, unit, created_at)
VALUES (business_indicator_seq.NEXTVAL, 'Margem Bruta T1 2025', '46.0%', 2025, 1, 'QUARTERLY_RESULTS', '%', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (id, title, indicator_value, period_year, period_quarter, category, unit, created_at)
VALUES (business_indicator_seq.NEXTVAL, 'YoY T1 2025', '+3%', 2025, 1, 'QUARTERLY_RESULTS', '%', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (id, title, indicator_value, period_year, period_quarter, category, unit, created_at)
VALUES (business_indicator_seq.NEXTVAL, 'Receita T4 2024', '89.5B', 2024, 4, 'QUARTERLY_RESULTS', 'AOA', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (id, title, indicator_value, period_year, period_quarter, category, unit, created_at)
VALUES (business_indicator_seq.NEXTVAL, 'EPS T4 2024', '1.44', 2024, 4, 'QUARTERLY_RESULTS', 'AOA', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (id, title, indicator_value, period_year, period_quarter, category, unit, created_at)
VALUES (business_indicator_seq.NEXTVAL, 'Margem Bruta T4 2024', '44.7%', 2024, 4, 'QUARTERLY_RESULTS', '%', CURRENT_TIMESTAMP);
INSERT INTO business_indicators (id, title, indicator_value, period_year, period_quarter, category, unit, created_at)
VALUES (business_indicator_seq.NEXTVAL, 'YoY T4 2024', '+2%', 2024, 4, 'QUARTERLY_RESULTS', '%', CURRENT_TIMESTAMP);

-- -----------------------------------------------------------------
-- 6. Shareholder structure (participantes)
-- -----------------------------------------------------------------
INSERT INTO shareholder_structure (id, shareholder_name, shares_label, percentage, display_color, display_order, is_active, created_at)
VALUES (shareholder_structure_seq.NEXTVAL, 'IGAPE - INSTITUTO DE GESTÃO DE ACTIVOS E PARTICIPAÇÕES DO ESTADO', '1.680.000', 70.0000, '#164993', 1, 1, CURRENT_TIMESTAMP);
INSERT INTO shareholder_structure (id, shareholder_name, shares_label, percentage, display_color, display_order, is_active, created_at)
VALUES (shareholder_structure_seq.NEXTVAL, 'FUNDO DE PENSÕES DA SONANGOL', '164.121', 6.8400, '#e63c2e', 2, 1, CURRENT_TIMESTAMP);
INSERT INTO shareholder_structure (id, shareholder_name, shares_label, percentage, display_color, display_order, is_active, created_at)
VALUES (shareholder_structure_seq.NEXTVAL, 'NOBLE GROUP S.A', '162.784', 6.7800, '#10b981', 3, 1, CURRENT_TIMESTAMP);
INSERT INTO shareholder_structure (id, shareholder_name, shares_label, percentage, display_color, display_order, is_active, created_at)
VALUES (shareholder_structure_seq.NEXTVAL, 'OUTROS', '393.095', 16.3800, '#f59e0b', 4, 1, CURRENT_TIMESTAMP);

-- -----------------------------------------------------------------
-- 7. General Assemblies — Ordinary (assembleias)
-- -----------------------------------------------------------------
INSERT INTO general_assemblies (id, slug_id, title, meeting_year, meeting_date, status, assembly_type, summary, display_order, is_active, created_at)
VALUES (general_assembly_seq.NEXTVAL, 'ag-2024', 'Assembleia Geral Ordinária de 2024', 2024,
    '10 de Abril de 2025', 'Realizada', 'Ordinária',
    'A Assembleia Geral Ordinária de 2024 decorreu com quórum total, onde foram aprovadas as contas por unanimidade, destacando-se a solidez dos indicadores de solvabilidade.',
    1, 1, CURRENT_TIMESTAMP);

-- Agenda items for AG-2024
INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Apreciação do Relatório de Gestão e Contas do exercício de 2024.', 1);
INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Aprovação da proposta de aplicação dos resultados do exercício de 2024.', 2);
INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Avaliação geral do desempenho do Conselho de Administração e do Conselho Fiscal.', 3);
INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Apreciação do Relatório de Governo Societário referente ao ano de 2024.', 4);
INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Deliberação sobre o Relatório da Comissão de Remunerações.', 5);

-- Documents for AG-2024
INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2024, 'Convocatória AGO 2024', '#', 'PDF', '1.2 MB', DATE '2025-04-10', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2024, 'Relatório e Contas 2024', '#', 'PDF', '8.4 MB', DATE '2025-04-10', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2024, 'Relatório de Governo Societário 2024', '#', 'PDF', '2.1 MB', DATE '2025-04-10', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2024, 'Acta da Assembleia Geral 2024', '#', 'PDF', '1.2 MB', DATE '2025-04-10', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2024, 'Parecer do Conselho Fiscal 2024', '#', 'PDF', '0.9 MB', DATE '2025-04-10', CURRENT_TIMESTAMP);

INSERT INTO general_assemblies (id, slug_id, title, meeting_year, meeting_date, status, assembly_type, summary, display_order, is_active, created_at)
VALUES (general_assembly_seq.NEXTVAL, 'ag-2023', 'Assembleia Geral Ordinária de 2023', 2023,
    '18 de Abril de 2024', 'Realizada', 'Ordinária',
    'Em 2023, a assembleia focou-se na expansão estratégica e na renovação dos órgãos de fiscalização.',
    2, 1, CURRENT_TIMESTAMP);

INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Relatório de Gestão e Contas do exercício de 2023.', 1);
INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Aplicação de Resultados e Dividendos.', 2);
INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Eleição de novos membros para o Conselho Fiscal.', 3);
INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Aprovação do Plano Estratégico 2024-2026.', 4);

INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2023, 'Relatório e Contas 2023', '#', 'PDF', '7.8 MB', DATE '2024-04-18', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2023, 'Convocatória AGO 2023', '#', 'PDF', '1.1 MB', DATE '2024-04-18', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2023, 'Acta da Assembleia Geral 2023', '#', 'PDF', '1.5 MB', DATE '2024-04-18', CURRENT_TIMESTAMP);

-- -----------------------------------------------------------------
-- 8. General Assemblies — Extraordinary (assembleiaExtraordinaria)
-- -----------------------------------------------------------------
INSERT INTO general_assemblies (id, slug_id, title, meeting_year, meeting_date, status, assembly_type, summary, display_order, is_active, created_at)
VALUES (general_assembly_seq.NEXTVAL, 'age-2025', 'Eleição dos Órgãos Sociais (2026-2029)', 2025,
    '16 de Dezembro de 2025', 'Convocada', 'Eleitoral',
    'Eleição e constituição dos novos órgãos sociais para o mandato 2026-2029.',
    1, 1, CURRENT_TIMESTAMP);

INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Eleição dos membros da Mesa da Assembleia Geral.', 1);
INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Eleição dos membros do Conselho de Administração.', 2);
INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Eleição dos membros do Conselho Fiscal.', 3);
INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Fixação da caução a prestar pelos administradores e fiscais.', 4);
INSERT INTO general_assembly_agenda_items (id, assembly_id, item_text, display_order)
VALUES (general_assembly_agenda_seq.NEXTVAL, general_assembly_seq.CURRVAL, 'Deliberação sobre a Política de Remuneração dos Órgãos Sociais.', 5);

INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2025, 'Convocatória AGE Dez 2025', '#', 'PDF', '1.2 MB', DATE '2025-12-16', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2025, 'Proposta Lista A - Administração', '#', 'PDF', '3.4 MB', DATE '2025-12-16', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2025, 'Proposta Lista B - Administração', '#', 'PDF', '2.8 MB', DATE '2025-12-16', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2025, 'Curriculum Vitae dos Candidatos', '#', 'PDF', '5.1 MB', DATE '2025-12-16', CURRENT_TIMESTAMP);
INSERT INTO general_assembly_documents (id, assembly_id, assembly_year, title, document_url, document_type, file_size_label, assembly_date, created_at)
VALUES (general_assembly_doc_seq.NEXTVAL, general_assembly_seq.CURRVAL, 2025, 'Guia de Procedimento de Votação', '#', 'PDF', '0.9 MB', DATE '2025-12-16', CURRENT_TIMESTAMP);
