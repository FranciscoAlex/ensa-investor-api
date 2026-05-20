package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.InvestorDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestorDocumentRepository extends JpaRepository<InvestorDocument, Long> {
    List<InvestorDocument> findAllByOrderByYearDescCreatedAtDesc();
}
