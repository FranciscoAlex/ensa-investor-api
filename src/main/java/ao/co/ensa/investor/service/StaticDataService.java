package ao.co.ensa.investor.service;

import ao.co.ensa.investor.model.dto.StaticDataDTO;
import ao.co.ensa.investor.model.entity.StaticDataDefinition;
import ao.co.ensa.investor.repository.StaticDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for managing static/reference data used by the frontend.
 * All reads are cached in Redis (cache name: "staticData", TTL: 24h).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StaticDataService {

    private final StaticDataRepository staticDataRepository;

    /**
     * Get all items in a category (cached in Redis)
     */
    @Cacheable(value = "staticData", key = "#category")
    @Transactional(readOnly = true)
    public List<StaticDataDTO> getByCategory(String category) {
        log.debug("Cache MISS for category: {} — loading from DB", category);
        return staticDataRepository.findByCategoryAndActiveTrueOrderBySortOrderAsc(category)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get all available categories
     */
    @Cacheable(value = "staticData", key = "'ALL_CATEGORIES'")
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return staticDataRepository.findAllActiveCategories();
    }

    /**
     * Get children of a parent item (for hierarchical data)
     */
    @Cacheable(value = "staticData", key = "'children_' + #parentId")
    @Transactional(readOnly = true)
    public List<StaticDataDTO> getChildren(Long parentId) {
        return staticDataRepository.findByParentIdAndActiveTrueOrderBySortOrderAsc(parentId)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Search static data by label (PT or EN)
     */
    @Transactional(readOnly = true)
    public List<StaticDataDTO> search(String query) {
        return staticDataRepository.searchByLabel(query)
            .stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    /**
     * Evict all static data cache (use after admin updates)
     */
    @CacheEvict(value = "staticData", allEntries = true)
    public void evictCache() {
        log.info("Static data cache evicted");
    }

    private StaticDataDTO mapToDTO(StaticDataDefinition entity) {
        return StaticDataDTO.builder()
            .id(entity.getId())
            .category(entity.getCategory())
            .code(entity.getCode())
            .labelPt(entity.getLabelPt())
            .labelEn(entity.getLabelEn())
            .value(entity.getValue())
            .parentId(entity.getParent() != null ? entity.getParent().getId() : null)
            .sortOrder(entity.getSortOrder())
            .active(entity.isActive())
            .metadata(entity.getMetadata())
            .description(entity.getDescription())
            .icon(entity.getIcon())
            .build();
    }
}
