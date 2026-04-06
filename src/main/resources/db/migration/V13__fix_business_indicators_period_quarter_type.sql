-- Ensure compatibility with JPA Integer mapping for period_quarter.
ALTER TABLE business_indicators
    MODIFY COLUMN period_quarter INT NULL;
