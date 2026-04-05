package ao.co.ensa.investor.controller;

import ao.co.ensa.investor.model.dto.StaticDataDTO;
import ao.co.ensa.investor.service.StaticDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/static-data")
@RequiredArgsConstructor
@Tag(name = "Static Data", description = "Reference/lookup data for frontend (public, cached in Redis)")
public class StaticDataController {

    private final StaticDataService staticDataService;

    @GetMapping("/categories")
    @Operation(summary = "List all categories", description = "Returns all available static data categories")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(staticDataService.getAllCategories());
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get items by category", description = "Returns all active items for a given category (e.g. PROVINCES, CURRENCIES)")
    public ResponseEntity<List<StaticDataDTO>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(staticDataService.getByCategory(category.toUpperCase()));
    }

    @GetMapping("/children/{parentId}")
    @Operation(summary = "Get children", description = "Returns child items of a parent (for hierarchical data)")
    public ResponseEntity<List<StaticDataDTO>> getChildren(@PathVariable Long parentId) {
        return ResponseEntity.ok(staticDataService.getChildren(parentId));
    }

    @GetMapping("/search")
    @Operation(summary = "Search static data", description = "Search by label in Portuguese or English")
    public ResponseEntity<List<StaticDataDTO>> search(@RequestParam String q) {
        return ResponseEntity.ok(staticDataService.search(q));
    }

    @PostMapping("/cache/evict")
    @Operation(summary = "Evict cache", description = "Admin-only: Clear all static data from Redis cache")
    public ResponseEntity<String> evictCache() {
        staticDataService.evictCache();
        return ResponseEntity.ok("{\"message\": \"Static data cache evicted\"}");
    }
}
