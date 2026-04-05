package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.FinancialStatement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialStatementRepository extends JpaRepository<FinancialStatement, Long> {

    List<FinancialStatement> findAllByOrderByYearDesc();
    List<FinancialStatement> findByYearBetweenOrderByYearDesc(int fromYear, int toYear);
}
