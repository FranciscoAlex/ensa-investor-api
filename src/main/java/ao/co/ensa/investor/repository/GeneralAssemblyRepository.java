package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.GeneralAssembly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GeneralAssemblyRepository extends JpaRepository<GeneralAssembly, Long> {

    List<GeneralAssembly> findAllByActiveTrueOrderByMeetingYearDesc();

    List<GeneralAssembly> findAllByActiveTrueAndAssemblyTypeOrderByMeetingYearDesc(String assemblyType);

    Optional<GeneralAssembly> findBySlugId(String slugId);

    @Query("SELECT ga FROM GeneralAssembly ga LEFT JOIN FETCH ga.agendaItems WHERE ga.id = :id")
    Optional<GeneralAssembly> findByIdWithAgendaItems(@Param("id") Long id);
}
