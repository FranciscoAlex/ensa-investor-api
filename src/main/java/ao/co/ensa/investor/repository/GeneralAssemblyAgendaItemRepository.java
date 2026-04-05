package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.GeneralAssemblyAgendaItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneralAssemblyAgendaItemRepository extends JpaRepository<GeneralAssemblyAgendaItem, Long> {

    List<GeneralAssemblyAgendaItem> findByAssemblyIdOrderByDisplayOrderAsc(Long assemblyId);
}
