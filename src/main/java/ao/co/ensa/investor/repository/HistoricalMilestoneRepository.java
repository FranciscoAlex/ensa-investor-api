package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.HistoricalMilestone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricalMilestoneRepository extends JpaRepository<HistoricalMilestone, Long> {

    List<HistoricalMilestone> findAllByActiveTrueOrderByDisplayOrderAscMilestoneYearAsc();
}
