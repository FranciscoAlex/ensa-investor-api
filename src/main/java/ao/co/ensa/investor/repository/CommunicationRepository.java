package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.Communication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommunicationRepository extends JpaRepository<Communication, Long> {

    List<Communication> findAllByActiveTrueOrderByPublishedAtDesc();
    List<Communication> findByCommunicationTypeAndActiveTrueOrderByPublishedAtDesc(String type);

    @Query("SELECT c FROM Communication c WHERE c.active = true AND LOWER(c.displaySections) LIKE LOWER(CONCAT('%', :section, '%')) ORDER BY c.publishedAt DESC")
    List<Communication> findBySectionAndActiveTrueOrderByPublishedAtDesc(@Param("section") String section);
}
