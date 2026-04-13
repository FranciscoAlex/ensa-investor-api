-- V14__seed_investor_faq_content_block.sql (Oracle)
-- FROM DUAL WHERE NOT EXISTS is supported natively in Oracle
INSERT INTO content_blocks (block_key, data, updated_at)
SELECT
    'investor_faq.json',
    '{"updatedAt":"2026-04-06","items":[{"id":"faq-1","question":"Como posso adquirir accoes da ENSA?","answer":"As accoes da ENSA sao negociadas na BODIVA. Para investir, contacte um intermediario financeiro autorizado (Banco ou Corretora) para abrir uma conta de custodia e emitir ordens de compra.","sortOrder":1},{"id":"faq-2","question":"Onde encontro os resultados financeiros anuais?","answer":"Os Relatorios e Contas anuais estao disponiveis na secao de reporte financeiro do Portal do Investidor para consulta e download.","sortOrder":2},{"id":"faq-3","question":"Qual e a politica de dividendos da ENSA?","answer":"A distribuicao de dividendos e deliberada em Assembleia Geral, com base nos resultados liquidos auditados e na estrategia de reinvestimento.","sortOrder":3},{"id":"faq-4","question":"Quem e o responsavel pela auditoria externa da ENSA?","answer":"A entidade de auditoria externa e divulgada nos relatorios e comunicacoes oficiais de governacao corporativa da ENSA.","sortOrder":4},{"id":"faq-5","question":"Como posso contactar o Gabinete de Apoio ao Investidor?","answer":"Pode contactar o Gabinete de Apoio ao Investidor pelos canais oficiais publicados no portal institucional da ENSA.","sortOrder":5}]}',
    CURRENT_TIMESTAMP
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM content_blocks WHERE block_key = 'investor_faq.json'
);
