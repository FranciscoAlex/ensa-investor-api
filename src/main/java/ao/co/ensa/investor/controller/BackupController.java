package ao.co.ensa.investor.controller;

import ao.co.ensa.investor.service.DatabaseBackupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/backups")
@RequiredArgsConstructor
@Tag(name = "Database Backups", description = "Daily database backup management (list, trigger, restore, delete)")
public class BackupController {

    private final DatabaseBackupService databaseBackupService;

    @GetMapping
    @Operation(summary = "List all database backups")
    public ResponseEntity<List<Map<String, Object>>> listBackups() {
        try {
            return ResponseEntity.ok(databaseBackupService.listBackups());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/trigger")
    @Operation(summary = "Trigger an immediate database backup")
    public ResponseEntity<Map<String, Object>> triggerBackup() {
        try {
            DatabaseBackupService.BackupResult result = databaseBackupService.createBackup();
            return ResponseEntity.status(201).body(Map.of(
                "filename", result.filename(),
                "sizeBytes", result.sizeBytes(),
                "createdAt", result.createdAt()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage() != null ? e.getMessage() : "Backup failed"));
        }
    }

    @PostMapping("/{filename}/restore")
    @Operation(summary = "Restore the database from a backup file")
    public ResponseEntity<Map<String, String>> restoreBackup(@PathVariable String filename) {
        try {
            databaseBackupService.restoreBackup(filename);
            return ResponseEntity.ok(Map.of("message", "Database restored from " + filename));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("error", e.getMessage() != null ? e.getMessage() : "Restore failed"));
        }
    }

    @DeleteMapping("/{filename}")
    @Operation(summary = "Delete a backup file")
    public ResponseEntity<Void> deleteBackup(@PathVariable String filename) {
        try {
            databaseBackupService.deleteBackup(filename);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
