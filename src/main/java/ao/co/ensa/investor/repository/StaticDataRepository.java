package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.StaticDataDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaticDataRepository extends JpaRepository<StaticDataDefinition, Long> {

    List<StaticDataDefinition> findByCategoryAndActiveTrueOrderBySortOrderAsc(String category);

    Optional<StaticDataDefinition> findByCategoryAndCode(String category, String code);

    @Query("SELECT DISTINCT s.category FROM StaticDataDefinition s WHERE s.active = true ORDER BY s.category")
    List<String> findAllActiveCategories();

    List<StaticDataDefinition> findByParentIdAndActiveTrueOrderBySortOrderAsc(Long parentId);

    @Query("SELECT s FROM StaticDataDefinition s WHERE s.active = true " +
           "AND (LOWER(s.labelPt) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(s.labelEn) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<StaticDataDefinition> searchByLabel(@Param("query") String query);
}
