package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.ExternalAuditor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExternalAuditorRepository extends JpaRepository<ExternalAuditor, Long> {

    List<ExternalAuditor> findAllByOrderByPeriodFromDesc();
    Optional<ExternalAuditor> findFirstByCurrentTrue();
}
