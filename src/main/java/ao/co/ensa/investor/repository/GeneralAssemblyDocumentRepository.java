package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.GeneralAssemblyDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneralAssemblyDocumentRepository extends JpaRepository<GeneralAssemblyDocument, Long> {

    List<GeneralAssemblyDocument> findAllByOrderByAssemblyYearDesc();
}
