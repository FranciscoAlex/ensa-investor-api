package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.BusinessIndicator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusinessIndicatorRepository extends JpaRepository<BusinessIndicator, Long> {

    List<BusinessIndicator> findAllByOrderByPeriodYearDescPeriodQuarterDesc();
    List<BusinessIndicator> findByCategoryOrderByPeriodYearDesc(String category);
}
