package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubsidiaryRepository extends JpaRepository<Subsidiary, Long> {

    List<Subsidiary> findAllByActiveTrueOrderByEntityNameAsc();
}
