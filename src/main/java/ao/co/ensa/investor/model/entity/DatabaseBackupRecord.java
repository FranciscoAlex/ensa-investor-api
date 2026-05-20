package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Persists backup metadata in the database so the dashboard is not
 * purely reliant on filesystem scanning.  One row per backup file.
 */
@Entity
@Table(name = "database_backup_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatabaseBackupRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Filename only (no path) — uniquely identifies a backup. */
    @Column(nullable = false, unique = true, length = 200)
    private String filename;

    /** Compressed file size in bytes at creation time. */
    @Column(name = "size_bytes", nullable = false)
    private long sizeBytes;

    /** When the backup was created (server local time). */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * What triggered this backup: "SCHEDULED", "MANUAL", or "IMPORTED"
     * (for backups discovered on disk that pre-date the DB tracking feature).
     */
    @Column(name = "triggered_by", length = 20, nullable = false)
    @Builder.Default
    private String triggeredBy = "MANUAL";
}
