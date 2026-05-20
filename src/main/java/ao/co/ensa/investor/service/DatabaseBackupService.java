package ao.co.ensa.investor.service;

import ao.co.ensa.investor.model.entity.DatabaseBackupRecord;
import ao.co.ensa.investor.repository.DatabaseBackupRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Performs daily MySQL database backups using mysqldump and stores the resulting
 * .sql.gz files under {uploadDir}/backups/.  Up to 30 backups are retained
 * automatically.
 *
 * Backup metadata (filename, size, timestamp, trigger source) is persisted to the
 * {@code database_backup_records} table so the admin dashboard is backed by the
 * database rather than purely filesystem directory scans.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseBackupService {

    private static final DateTimeFormatter FILE_TS = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final int MAX_RETAINED_BACKUPS = 30;

    private final DatabaseBackupRecordRepository backupRecordRepository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    // MySQL datasource coordinates – injected from Spring's datasource config.
    @Value("${spring.datasource.url:}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:}")
    private String dbUser;

    @Value("${spring.datasource.password:}")
    private String dbPassword;

    // Allow overriding the path to mysqldump (useful in containers).
    @Value("${app.backup.mysqldump-path:mysqldump}")
    private String mysqldumpPath;

    // -----------------------------------------------------------------------
    // Scheduling – runs every day at 02:00 (server local time).
    // -----------------------------------------------------------------------

    @Scheduled(cron = "0 0 2 * * *")
    public void scheduledBackup() {
        log.info("[Backup] Starting scheduled daily database backup");
        try {
            BackupResult result = createBackup("SCHEDULED");
            log.info("[Backup] Backup completed: {}", result.filename());
            pruneOldBackups();
        } catch (Exception e) {
            log.error("[Backup] Scheduled backup failed", e);
        }
    }

    // -----------------------------------------------------------------------
    // Public API (used by BackupController)
    // -----------------------------------------------------------------------

    public record BackupResult(String filename, long sizeBytes, String createdAt) {}

    /**
     * Creates a gzipped SQL dump immediately (manual trigger) and persists
     * metadata to the database.
     */
    public BackupResult createBackup() throws IOException, InterruptedException {
        return createBackup("MANUAL");
    }

    /**
     * Creates a gzipped SQL dump with the given trigger source ("MANUAL" or
     * "SCHEDULED") and persists metadata to the database.
     */
    @Transactional
    public BackupResult createBackup(String triggeredBy) throws IOException, InterruptedException {
        Path backupDir = resolveBackupDir();
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(FILE_TS);
        String filename = "backup_" + timestamp + ".sql.gz";
        Path outFile = backupDir.resolve(filename);

        String[] jdbcParts = parseJdbcUrl(datasourceUrl);
        String host = jdbcParts[0];
        String port = jdbcParts[1];
        String database = jdbcParts[2];

        // Build: mysqldump --host=H --port=P --user=U --password=PW --single-transaction
        //        --routines --triggers DB | gzip > file.sql.gz
        ProcessBuilder pb = new ProcessBuilder(
                "/bin/sh", "-c",
                String.format(
                        "%s --host=%s --port=%s --user=%s --password=%s " +
                        "--single-transaction --routines --triggers %s | gzip > %s",
                        shellEscape(mysqldumpPath),
                        shellEscape(host),
                        shellEscape(port),
                        shellEscape(dbUser),
                        shellEscape(dbPassword),
                        shellEscape(database),
                        outFile.toAbsolutePath()
                )
        );
        pb.redirectErrorStream(true);

        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            Files.deleteIfExists(outFile);
            throw new IOException("mysqldump exited with code " + exitCode);
        }

        long size = Files.exists(outFile) ? Files.size(outFile) : 0L;

        // Persist metadata to DB.
        DatabaseBackupRecord record = DatabaseBackupRecord.builder()
                .filename(filename)
                .sizeBytes(size)
                .createdAt(now)
                .triggeredBy(triggeredBy)
                .build();
        backupRecordRepository.save(record);

        return new BackupResult(filename, size, now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    /**
     * Returns all backup records from the database (newest first).
     *
     * On each call, the backup directory is scanned to reconcile:
     * - Files on disk but not in the DB are imported as "IMPORTED" records so that
     *   backups created before DB tracking was introduced are still visible.
     * - Each record is annotated with {@code fileExists} reflecting whether the
     *   physical file is still present.
     */
    @Transactional
    public List<Map<String, Object>> listBackups() throws IOException {
        Path backupDir = resolveBackupDir();

        // Collect filenames currently on disk.
        List<Path> diskFiles;
        try (Stream<Path> stream = Files.list(backupDir)) {
            diskFiles = stream
                    .filter(p -> p.getFileName().toString().startsWith("backup_") &&
                                 p.getFileName().toString().endsWith(".sql.gz"))
                    .toList();
        }

        // Import any on-disk files that have no DB record yet.
        for (Path p : diskFiles) {
            String name = p.getFileName().toString();
            if (!backupRecordRepository.existsByFilename(name)) {
                try {
                    FileTime ft = Files.getLastModifiedTime(p);
                    LocalDateTime ts = ft.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    DatabaseBackupRecord imported = DatabaseBackupRecord.builder()
                            .filename(name)
                            .sizeBytes(Files.size(p))
                            .createdAt(ts)
                            .triggeredBy("IMPORTED")
                            .build();
                    backupRecordRepository.save(imported);
                    log.info("[Backup] Imported pre-existing backup into DB: {}", name);
                } catch (IOException e) {
                    log.warn("[Backup] Could not import backup file {}: {}", name, e.getMessage());
                }
            }
        }

        // Build response from DB, annotated with live file-existence flag.
        List<DatabaseBackupRecord> records = backupRecordRepository.findAllByOrderByCreatedAtDesc();
        List<Map<String, Object>> result = new ArrayList<>(records.size());
        for (DatabaseBackupRecord rec : records) {
            boolean fileExists = Files.exists(backupDir.resolve(rec.getFilename()));
            Map<String, Object> entry = new java.util.LinkedHashMap<>();
            entry.put("filename",    rec.getFilename());
            entry.put("sizeBytes",   rec.getSizeBytes());
            entry.put("createdAt",   rec.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            entry.put("triggeredBy", rec.getTriggeredBy());
            entry.put("fileExists",  fileExists);
            result.add(entry);
        }
        return result;
    }

    /**
     * Restores the database from a previously created backup file.
     */
    public void restoreBackup(String filename) throws IOException, InterruptedException {
        Path backupFile = resolveBackupDir().resolve(sanitizeFilename(filename));
        if (!Files.exists(backupFile)) {
            throw new IOException("Backup file not found: " + filename);
        }

        String[] jdbcParts = parseJdbcUrl(datasourceUrl);
        String host = jdbcParts[0];
        String port = jdbcParts[1];
        String database = jdbcParts[2];

        // gunzip | mysql
        ProcessBuilder pb = new ProcessBuilder(
                "/bin/sh", "-c",
                String.format(
                        "gunzip -c %s | mysql --host=%s --port=%s --user=%s --password=%s %s",
                        backupFile.toAbsolutePath(),
                        shellEscape(host),
                        shellEscape(port),
                        shellEscape(dbUser),
                        shellEscape(dbPassword),
                        shellEscape(database)
                )
        );
        pb.redirectErrorStream(true);

        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new IOException("mysql restore exited with code " + exitCode);
        }
        log.info("[Backup] Database restored from: {}", filename);
    }

    /**
     * Deletes a backup file and its database record.
     */
    @Transactional
    public void deleteBackup(String filename) throws IOException {
        String safe = sanitizeFilename(filename);
        Path file = resolveBackupDir().resolve(safe);
        if (!Files.exists(file)) {
            throw new IOException("Backup file not found: " + filename);
        }
        Files.delete(file);
        backupRecordRepository.findByFilename(safe).ifPresent(backupRecordRepository::delete);
        log.info("[Backup] Deleted backup: {}", safe);
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private Path resolveBackupDir() throws IOException {
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize().resolve("backups");
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        return dir;
    }

    /**
     * Removes the oldest backups keeping only MAX_RETAINED_BACKUPS.
     * Both the filesystem files and their DB records are removed.
     */
    @Transactional
    void pruneOldBackups() {
        try {
            Path backupDir = resolveBackupDir();
            List<Path> files;
            try (Stream<Path> stream = Files.list(backupDir)) {
                files = stream
                    .filter(p -> p.getFileName().toString().startsWith("backup_"))
                    .sorted()
                    .toList();
            }
            int toDelete = files.size() - MAX_RETAINED_BACKUPS;
            for (int i = 0; i < toDelete; i++) {
                Path f = files.get(i);
                String name = f.getFileName().toString();
                Files.deleteIfExists(f);
                backupRecordRepository.findByFilename(name).ifPresent(backupRecordRepository::delete);
                log.info("[Backup] Pruned old backup: {}", name);
            }
        } catch (IOException e) {
            log.warn("[Backup] Failed to prune old backups", e);
        }
    }

    /**
     * Parses jdbc:mysql://host:port/database and returns [host, port, database].
     * Falls back to safe defaults if parsing fails.
     */
    private static String[] parseJdbcUrl(String url) {
        try {
            // Strip jdbc:mysql:// prefix and query params
            String stripped = url.replaceFirst("(?i)^jdbc:mysql://", "");
            int qMark = stripped.indexOf('?');
            if (qMark >= 0) stripped = stripped.substring(0, qMark);
            int slash = stripped.indexOf('/');
            String hostPort = slash >= 0 ? stripped.substring(0, slash) : stripped;
            String database = slash >= 0 ? stripped.substring(slash + 1) : "railway";
            int colon = hostPort.lastIndexOf(':');
            String host = colon >= 0 ? hostPort.substring(0, colon) : hostPort;
            String port = colon >= 0 ? hostPort.substring(colon + 1) : "3306";
            return new String[]{host, port, database};
        } catch (Exception e) {
            return new String[]{"localhost", "3306", "railway"};
        }
    }

    /**
     * Prevents path traversal by stripping any directory separators from the filename.
     */
    private static String sanitizeFilename(String filename) {
        return Paths.get(filename).getFileName().toString();
    }

    /**
     * Wraps a value in single quotes and escapes embedded single quotes for shell use.
     */
    private static String shellEscape(String value) {
        return "'" + value.replace("'", "'\\''") + "'";
    }
}
