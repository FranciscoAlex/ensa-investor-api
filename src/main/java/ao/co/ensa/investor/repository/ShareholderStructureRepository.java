package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.ShareholderStructure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShareholderStructureRepository extends JpaRepository<ShareholderStructure, Long> {

    List<ShareholderStructure> findAllByActiveTrueOrderByDisplayOrderAsc();
}
