-- V13__fix_business_indicators_period_quarter_type.sql (Oracle)
-- Conditionally make period_quarter nullable; in Oracle this column may already be nullable (ORA-01451 guard).
DECLARE
  v_nullable VARCHAR2(1);
BEGIN
  SELECT nullable INTO v_nullable
  FROM user_tab_columns
  WHERE table_name = 'BUSINESS_INDICATORS' AND column_name = 'PERIOD_QUARTER';

  IF v_nullable = 'N' THEN
    EXECUTE IMMEDIATE 'ALTER TABLE business_indicators MODIFY (period_quarter NUMBER(10) NULL)';
  END IF;
EXCEPTION
  WHEN NO_DATA_FOUND THEN NULL;
END;
