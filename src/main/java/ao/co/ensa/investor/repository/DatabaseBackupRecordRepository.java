package ao.co.ensa.investor.repository;

import ao.co.ensa.investor.model.entity.DatabaseBackupRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatabaseBackupRecordRepository extends JpaRepository<DatabaseBackupRecord, Long> {

    /** All records newest first. */
    List<DatabaseBackupRecord> findAllByOrderByCreatedAtDesc();

    Optional<DatabaseBackupRecord> findByFilename(String filename);

    boolean existsByFilename(String filename);

    void deleteByFilename(String filename);
}
