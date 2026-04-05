package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.ContentBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentBlockRepository extends JpaRepository<ContentBlock, Long> {
    Optional<ContentBlock> findByBlockKey(String blockKey);
}
