package ao.co.ensa.investor.service;

import ao.co.ensa.investor.model.entity.DatabaseBackupRecord;
import ao.co.ensa.investor.repository.DatabaseBackupRecordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.metamodel.EntityType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileTime;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Database backup service — dumps and restores through JDBC (the app's own
 * datasource), so it works identically on Oracle (main or XE mirror) and MySQL.
 * No external binaries (mysqldump/mysql) are involved.
 *
 * Dump format: gzipped JSON-lines ("backup_*.dump.gz"):
 *   line 1:  {"ensadump":2,"dialect":"oracle","createdAt":"..."}
 *   then per table: {"table":"users","columns":[...],"types":[...]}
 *   then per row:   {"r":[...]}   (values encoded per JDBC type)
 *
 * A backup that produces no tables, or an unreadable file, is treated as a
 * FAILURE — it is never recorded as a successful backup.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseBackupService {

    private static final DateTimeFormatter FILE_TS = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final int MAX_RETAINED_BACKUPS = 30;
    private static final int BATCH_SIZE = 500;

    /** Metadata tables that must never be dumped or restored. */
    private static final Set<String> EXCLUDED_TABLES = Set.of("flyway_schema_history", "database_backup_records");
    /** Join/collection tables that have no JPA entity of their own. */
    private static final Set<String> EXTRA_TABLES = Set.of("user_roles");

    private final DatabaseBackupRecordRepository backupRecordRepository;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

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
            log.error("[Backup] Scheduled backup FAILED — no backup was recorded", e);
        }
    }

    // -----------------------------------------------------------------------
    // Public API (used by BackupController)
    // -----------------------------------------------------------------------

    public record BackupResult(String filename, long sizeBytes, String createdAt) {}

    public BackupResult createBackup() throws IOException, InterruptedException {
        return createBackup("MANUAL");
    }

    @Transactional
    public BackupResult createBackup(String triggeredBy) throws IOException {
        Path backupDir = resolveBackupDir();
        LocalDateTime now = LocalDateTime.now();
        String filename = "backup_" + now.format(FILE_TS) + ".dump.gz";
        Path outFile = backupDir.resolve(filename);
        Path tmpFile = backupDir.resolve(filename + ".tmp");

        int tableCount = 0;
        long rowCount = 0;

        try (Connection con = dataSource.getConnection()) {
            String dialect = detectDialect(con);
            List<String> tables = resolveAppTables(con, dialect);
            if (tables.isEmpty()) {
                throw new IOException("No application tables found in the database — refusing to write an empty backup.");
            }

            try (Writer w = new BufferedWriter(new OutputStreamWriter(
                    new GZIPOutputStream(Files.newOutputStream(tmpFile)), StandardCharsets.UTF_8))) {
                ObjectNode header = mapper.createObjectNode();
                header.put("ensadump", 2);
                header.put("dialect", dialect);
                header.put("createdAt", now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                w.write(mapper.writeValueAsString(header));
                w.write('\n');

                for (String table : tables) {
                    rowCount += dumpTable(con, table, w);
                    tableCount++;
                }
            }
        } catch (SQLException e) {
            try { Files.deleteIfExists(tmpFile); } catch (IOException ignored) { }
            throw new IOException("Backup failed: " + e.getMessage(), e);
        } catch (IOException | RuntimeException e) {
            try { Files.deleteIfExists(tmpFile); } catch (IOException ignored) { }
            throw e;
        }

        // Only a validated dump is moved into place and recorded.
        Files.move(tmpFile, outFile, StandardCopyOption.REPLACE_EXISTING);
        long size = Files.size(outFile);
        log.info("[Backup] Dumped {} tables / {} rows → {} ({} bytes)", tableCount, rowCount, filename, size);

        DatabaseBackupRecord record = DatabaseBackupRecord.builder()
                .filename(filename)
                .sizeBytes(size)
                .createdAt(now)
                .triggeredBy(triggeredBy)
                .build();
        backupRecordRepository.save(record);

        return new BackupResult(filename, size, now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Transactional
    public List<Map<String, Object>> listBackups() throws IOException {
        Path backupDir = resolveBackupDir();

        List<Path> diskFiles;
        try (Stream<Path> stream = Files.list(backupDir)) {
            diskFiles = stream
                    .filter(p -> {
                        String n = p.getFileName().toString();
                        return n.startsWith("backup_") && (n.endsWith(".sql.gz") || n.endsWith(".dump.gz"));
                    })
                    .toList();
        }

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

    public void restoreBackup(String filename) throws IOException {
        Path backupFile = resolveBackupDir().resolve(sanitizeFilename(filename));
        if (!Files.exists(backupFile)) {
            throw new IOException("Backup file not found: " + filename);
        }
        if (filename.endsWith(".sql.gz")) {
            throw new IOException("This is a legacy mysqldump backup — it cannot be restored through the app. " +
                    "Use a .dump.gz backup created by the current version.");
        }

        try (Connection con = dataSource.getConnection();
             BufferedReader reader = new BufferedReader(new InputStreamReader(
                     new GZIPInputStream(Files.newInputStream(backupFile)), StandardCharsets.UTF_8))) {

            String dialect = detectDialect(con);

            String headerLine = reader.readLine();
            if (headerLine == null) throw new IOException("Backup file is empty.");
            JsonNode header = mapper.readTree(headerLine);
            if (header.path("ensadump").asInt(0) != 2) {
                throw new IOException("Unrecognized backup format.");
            }
            String dumpDialect = header.path("dialect").asText("");
            if (!dumpDialect.equals(dialect)) {
                throw new IOException("Backup was created for '" + dumpDialect +
                        "' but the current database is '" + dialect + "'.");
            }

            List<String[]> fkConstraints = new ArrayList<>();
            List<String[]> identityCols = new ArrayList<>();
            List<String> restoredTables = new ArrayList<>();

            // DDL phase (auto-commit): relax constraints so tables can load in any order.
            con.setAutoCommit(true);
            if (dialect.equals("oracle")) {
                fkConstraints = listOracleFkConstraints(con);
                toggleOracleConstraints(con, fkConstraints, false);
                identityCols = listOracleIdentityCols(con);
                for (String[] ic : identityCols) {
                    execQuietly(con, "ALTER TABLE " + ic[0] + " MODIFY " + ic[1] + " GENERATED BY DEFAULT AS IDENTITY");
                }
            }

            try {
                con.setAutoCommit(false);
                if (dialect.equals("mysql")) {
                    try (Statement st = con.createStatement()) { st.execute("SET FOREIGN_KEY_CHECKS=0"); }
                }

                PreparedStatement insert = null;
                int[] colTypes = null;
                int pending = 0;
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.isBlank()) continue;
                    JsonNode node = mapper.readTree(line);
                    if (node.has("table")) {
                        if (insert != null) { if (pending > 0) insert.executeBatch(); insert.close(); }
                        String table = node.get("table").asText();
                        restoredTables.add(table);
                        ArrayNode cols = (ArrayNode) node.get("columns");
                        ArrayNode types = (ArrayNode) node.get("types");
                        colTypes = new int[types.size()];
                        StringBuilder colList = new StringBuilder();
                        StringBuilder marks = new StringBuilder();
                        for (int i = 0; i < cols.size(); i++) {
                            if (i > 0) { colList.append(','); marks.append(','); }
                            colList.append(cols.get(i).asText());
                            marks.append('?');
                            colTypes[i] = types.get(i).asInt();
                        }
                        try (Statement st = con.createStatement()) { st.executeUpdate("DELETE FROM " + table); }
                        insert = con.prepareStatement(
                                "INSERT INTO " + table + " (" + colList + ") VALUES (" + marks + ")");
                        pending = 0;
                    } else if (node.has("r")) {
                        if (insert == null) throw new IOException("Malformed backup: row before table header.");
                        ArrayNode row = (ArrayNode) node.get("r");
                        for (int i = 0; i < row.size(); i++) {
                            bindValue(insert, i + 1, row.get(i), colTypes[i]);
                        }
                        insert.addBatch();
                        if (++pending >= BATCH_SIZE) { insert.executeBatch(); pending = 0; }
                    }
                }
                if (insert != null) { if (pending > 0) insert.executeBatch(); insert.close(); }
                con.commit();
            } catch (SQLException | IOException e) {
                try { con.rollback(); } catch (SQLException ignored) { }
                throw new IOException("Restore failed (all data changes rolled back): " + e.getMessage(), e);
            } finally {
                // Always put constraints back, even after a failure.
                try {
                    con.setAutoCommit(true);
                    if (dialect.equals("mysql")) {
                        try (Statement st = con.createStatement()) { st.execute("SET FOREIGN_KEY_CHECKS=1"); }
                    } else if (dialect.equals("oracle")) {
                        for (String[] ic : identityCols) {
                            String generation = ic[2].equalsIgnoreCase("ALWAYS") ? "ALWAYS" : "BY DEFAULT";
                            // START WITH LIMIT VALUE bumps the identity past the restored max id.
                            execQuietly(con, "ALTER TABLE " + ic[0] + " MODIFY " + ic[1] +
                                    " GENERATED " + generation + " AS IDENTITY (START WITH LIMIT VALUE)");
                        }
                        toggleOracleConstraints(con, fkConstraints, true);
                    }
                } catch (SQLException e) {
                    log.warn("[Backup] Failed to fully restore constraints after restore: {}", e.getMessage());
                }
            }

            log.info("[Backup] Database restored from {} ({} tables)", filename, restoredTables.size());
        } catch (SQLException e) {
            throw new IOException("Restore failed: " + e.getMessage(), e);
        }
    }

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
    // Dump internals
    // -----------------------------------------------------------------------

    /** Dumps one table; returns the number of rows written. */
    private long dumpTable(Connection con, String table, Writer w) throws SQLException, IOException {
        long rows = 0;
        try (Statement st = con.createStatement()) {
            st.setFetchSize(500);
            try (ResultSet rs = st.executeQuery("SELECT * FROM " + table)) {
                ResultSetMetaData md = rs.getMetaData();
                int n = md.getColumnCount();

                ObjectNode tableHeader = mapper.createObjectNode();
                tableHeader.put("table", table);
                ArrayNode cols = tableHeader.putArray("columns");
                ArrayNode types = tableHeader.putArray("types");
                for (int i = 1; i <= n; i++) {
                    cols.add(md.getColumnName(i));
                    types.add(md.getColumnType(i));
                }
                w.write(mapper.writeValueAsString(tableHeader));
                w.write('\n');

                while (rs.next()) {
                    ObjectNode rowNode = mapper.createObjectNode();
                    ArrayNode vals = rowNode.putArray("r");
                    for (int i = 1; i <= n; i++) {
                        encodeValue(rs, i, md.getColumnType(i), vals);
                    }
                    w.write(mapper.writeValueAsString(rowNode));
                    w.write('\n');
                    rows++;
                }
            }
        }
        return rows;
    }

    private void encodeValue(ResultSet rs, int idx, int type, ArrayNode out) throws SQLException {
        switch (type) {
            case Types.BLOB, Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY -> {
                byte[] b = rs.getBytes(idx);
                if (b == null) out.addNull();
                else out.add("B64:" + Base64.getEncoder().encodeToString(b));
            }
            case Types.TIMESTAMP, Types.TIMESTAMP_WITH_TIMEZONE, Types.DATE, Types.TIME -> {
                Timestamp t = rs.getTimestamp(idx);
                if (t == null) out.addNull();
                else out.add("TS:" + t.toLocalDateTime().format(TS_FMT));
            }
            case Types.NUMERIC, Types.DECIMAL, Types.INTEGER, Types.BIGINT, Types.SMALLINT,
                 Types.TINYINT, Types.FLOAT, Types.DOUBLE, Types.REAL -> {
                BigDecimal bd = rs.getBigDecimal(idx);
                if (bd == null) out.addNull();
                else out.add(bd);
            }
            case Types.BOOLEAN, Types.BIT -> {
                boolean v = rs.getBoolean(idx);
                if (rs.wasNull()) out.addNull();
                else out.add(v);
            }
            default -> {
                String s = rs.getString(idx);
                if (s == null) out.addNull();
                else out.add(escapeStringMarkers(s));
            }
        }
    }

    private static String escapeStringMarkers(String s) {
        if (s.startsWith("TS:") || s.startsWith("B64:") || s.startsWith("S:")) return "S:" + s;
        return s;
    }

    // -----------------------------------------------------------------------
    // Restore internals
    // -----------------------------------------------------------------------

    private void bindValue(PreparedStatement ps, int idx, JsonNode value, int type) throws SQLException {
        if (value == null || value.isNull()) {
            ps.setNull(idx, normalizeNullType(type));
            return;
        }
        if (value.isBoolean()) {
            ps.setBoolean(idx, value.asBoolean());
            return;
        }
        if (value.isNumber()) {
            ps.setBigDecimal(idx, value.decimalValue());
            return;
        }
        String s = value.asText();
        if (s.startsWith("TS:")) {
            ps.setTimestamp(idx, Timestamp.valueOf(java.time.LocalDateTime.parse(s.substring(3), TS_FMT)));
        } else if (s.startsWith("B64:")) {
            ps.setBytes(idx, Base64.getDecoder().decode(s.substring(4)));
        } else if (s.startsWith("S:")) {
            ps.setString(idx, s.substring(2));
        } else {
            ps.setString(idx, s);
        }
    }

    private static int normalizeNullType(int type) {
        // Some drivers reject exotic type codes in setNull; map them to safe ones.
        return switch (type) {
            case Types.TIMESTAMP_WITH_TIMEZONE -> Types.TIMESTAMP;
            case Types.NCLOB -> Types.CLOB;
            default -> type;
        };
    }

    private List<String[]> listOracleFkConstraints(Connection con) throws SQLException {
        List<String[]> result = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT table_name, constraint_name FROM user_constraints " +
                     "WHERE constraint_type='R' AND status='ENABLED'")) {
            while (rs.next()) result.add(new String[]{rs.getString(1), rs.getString(2)});
        }
        return result;
    }

    private void toggleOracleConstraints(Connection con, List<String[]> constraints, boolean enable) {
        for (String[] c : constraints) {
            execQuietly(con, "ALTER TABLE " + c[0] + (enable ? " ENABLE" : " DISABLE") + " CONSTRAINT " + c[1]);
        }
    }

    /** [table, column, generationType] for identity columns of the current schema. */
    private List<String[]> listOracleIdentityCols(Connection con) throws SQLException {
        List<String[]> result = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT table_name, column_name, generation_type FROM user_tab_identity_cols")) {
            while (rs.next()) result.add(new String[]{rs.getString(1), rs.getString(2), rs.getString(3)});
        }
        return result;
    }

    private void execQuietly(Connection con, String sql) {
        try (Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            log.warn("[Backup] DDL failed (continuing): {} — {}", sql, e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Table discovery
    // -----------------------------------------------------------------------

    private static String detectDialect(Connection con) throws SQLException {
        String product = con.getMetaData().getDatabaseProductName().toLowerCase(Locale.ROOT);
        if (product.contains("oracle")) return "oracle";
        if (product.contains("mysql") || product.contains("mariadb")) return "mysql";
        return "other";
    }

    /**
     * The app's tables = JPA entity tables + known join tables, intersected with
     * what actually exists in the connected schema (exact case preserved).
     * This deliberately ignores Oracle SYSTEM's own built-in tables.
     */
    private List<String> resolveAppTables(Connection con, String dialect) throws SQLException {
        Set<String> wanted = new LinkedHashSet<>(EXTRA_TABLES);
        for (EntityType<?> et : entityManagerFactory.getMetamodel().getEntities()) {
            jakarta.persistence.Table t = et.getJavaType().getAnnotation(jakarta.persistence.Table.class);
            String name = (t != null && !t.name().isBlank()) ? t.name() : camelToSnake(et.getName());
            wanted.add(name.toLowerCase(Locale.ROOT));
        }
        wanted.removeAll(EXCLUDED_TABLES);

        String schema = dialect.equals("oracle") ? con.getSchema() : null;
        List<String> present = new ArrayList<>();
        Set<String> presentLower = new LinkedHashSet<>();
        try (ResultSet rs = con.getMetaData().getTables(con.getCatalog(), schema, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                String tn = rs.getString("TABLE_NAME");
                if (tn != null && wanted.contains(tn.toLowerCase(Locale.ROOT))) {
                    present.add(tn);
                    presentLower.add(tn.toLowerCase(Locale.ROOT));
                }
            }
        }
        for (String w : wanted) {
            if (!presentLower.contains(w)) {
                log.warn("[Backup] App table '{}' not found in the database — skipping", w);
            }
        }
        return present;
    }

    private static String camelToSnake(String name) {
        return name.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase(Locale.ROOT);
    }

    // -----------------------------------------------------------------------
    // Misc helpers
    // -----------------------------------------------------------------------

    private Path resolveBackupDir() throws IOException {
        Path dir = Paths.get(uploadDir).toAbsolutePath().normalize().resolve("backups");
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        return dir;
    }

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

    private static String sanitizeFilename(String filename) {
        return Paths.get(filename).getFileName().toString();
    }
}
