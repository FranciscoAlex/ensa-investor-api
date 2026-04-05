package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.CorporateGovernanceReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorporateGovernanceReportRepository extends JpaRepository<CorporateGovernanceReport, Long> {

    List<CorporateGovernanceReport> findAllByOrderByReportYearDesc();
}
