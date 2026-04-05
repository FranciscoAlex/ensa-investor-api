package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.GeneralAssembly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GeneralAssemblyRepository extends JpaRepository<GeneralAssembly, Long> {

    List<GeneralAssembly> findAllByActiveTrueOrderByMeetingYearDesc();

    List<GeneralAssembly> findAllByActiveTrueAndAssemblyTypeOrderByMeetingYearDesc(String assemblyType);

    Optional<GeneralAssembly> findBySlugId(String slugId);
}
