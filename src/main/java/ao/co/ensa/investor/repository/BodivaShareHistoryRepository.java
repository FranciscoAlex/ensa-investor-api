package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.BodivaShareHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BodivaShareHistoryRepository extends JpaRepository<BodivaShareHistory, Long> {

    List<BodivaShareHistory> findAllByOrderByRecordDateDesc();
    List<BodivaShareHistory> findByRecordDateBetweenOrderByRecordDateDesc(LocalDate from, LocalDate to);
}
