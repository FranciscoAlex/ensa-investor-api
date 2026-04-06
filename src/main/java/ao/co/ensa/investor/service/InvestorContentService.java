package ao.co.ensa.investor.service;

import ao.co.ensa.investor.exception.ResourceNotFoundException;
import ao.co.ensa.investor.model.dto.*;
import ao.co.ensa.investor.model.entity.*;
import ao.co.ensa.investor.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import ao.co.ensa.investor.model.entity.ContentBlock;
import ao.co.ensa.investor.repository.ContentBlockRepository;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Investor relations static content. All reads are cached in Redis (cache name: "investorContent", TTL: 24h).
 */
@Service
@RequiredArgsConstructor
public class InvestorContentService {

    private final HistoricalMilestoneRepository historicalMilestoneRepository;
    private final BoardMemberRepository boardMemberRepository;
    private final CorporateGovernanceReportRepository corporateGovernanceReportRepository;
    private final FinancialStatementRepository financialStatementRepository;
    private final BusinessIndicatorRepository businessIndicatorRepository;
    private final CommunicationRepository communicationRepository;
    private final EventRepository eventRepository;
    private final SubsidiaryRepository subsidiaryRepository;
    private final GeneralAssemblyDocumentRepository generalAssemblyDocumentRepository;
    private final ContentBlockRepository contentBlockRepository;
    private final GeneralAssemblyRepository generalAssemblyRepository;
    private final ShareholderStructureRepository shareholderStructureRepository;
    private final InvestorRelationsRepository investorRelationsRepository;
    private final ExternalAuditorRepository externalAuditorRepository;

    @Value("${app.upload.dir:./uploads}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    private static final List<String> IMAGE_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".webp", ".gif", ".bmp", ".svg");

    private <T> T readJson(String filename, Class<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        // 1. Try DB first
        return contentBlockRepository.findByBlockKey(filename)
            .map(block -> {
                try {
                    return mapper.readValue(block.getData(), type);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to parse content block: " + filename, e);
                }
            })
            .orElseGet(() -> {
                // 2. Not in DB yet — load from classpath and persist (auto-seed)
                try {
                    ClassPathResource resource = new ClassPathResource(filename);
                    try (InputStream is = resource.getInputStream()) {
                        String json = new String(is.readAllBytes());
                        T dto = mapper.readValue(json, type);
                        ContentBlock block = new ContentBlock();
                        block.setBlockKey(filename);
                        block.setData(json);
                        block.setUpdatedAt(java.time.LocalDateTime.now());
                        contentBlockRepository.save(block);
                        return dto;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Failed to load " + filename, e);
                }
            });
    }

    private void writeJson(String filename, Object dto) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
            String json = mapper.writeValueAsString(dto);
            ContentBlock block = contentBlockRepository.findByBlockKey(filename)
                .orElseGet(ContentBlock::new);
            block.setBlockKey(filename);
            block.setData(json);
            block.setUpdatedAt(java.time.LocalDateTime.now());
            contentBlockRepository.save(block);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save " + filename, e);
        }
    }

    public List<Map<String, String>> listImageAssets() {
        try {
            Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (!Files.exists(root)) {
                return List.of();
            }

            List<Map<String, String>> assets = new ArrayList<>();
            try (var stream = Files.walk(root)) {
                stream.filter(Files::isRegularFile)
                    .forEach(path -> {
                        String filename = path.getFileName().toString();
                        String lower = filename.toLowerCase(Locale.ROOT);
                        boolean isImage = IMAGE_EXTENSIONS.stream().anyMatch(lower::endsWith);
                        if (!isImage) {
                            return;
                        }

                        Path relative = root.relativize(path);
                        String relativeUrl = relative.toString().replace('\\', '/');
                        String url = baseUrl + "/uploads/" + relativeUrl;

                        assets.add(Map.of(
                            "name", filename,
                            "url", url,
                            "path", relativeUrl
                        ));
                    });
            }

            assets.sort(Comparator.comparing((Map<String, String> item) -> item.getOrDefault("path", "")).reversed());
            return assets;
        } catch (IOException e) {
            throw new RuntimeException("Failed to list image assets", e);
        }
    }

    public String uploadSharedImage(MultipartFile file) {
        try {
            String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image";
            String filename = UUID.randomUUID() + "_" + original;
            Path uploadPath = Paths.get(uploadDir, "shared-images");
            Files.createDirectories(uploadPath);
            Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return baseUrl + "/uploads/shared-images/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload shared image", e);
        }
    }

    // ---- Historical milestones ----
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'historicalMilestones'")
    public List<HistoricalMilestoneDTO> getHistoricalMilestones() {
        return historicalMilestoneRepository.findAllByActiveTrueOrderByDisplayOrderAscMilestoneYearAsc()
            .stream().map(this::toMilestoneDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'historicalMilestone:' + #id")
    public HistoricalMilestoneDTO getHistoricalMilestoneById(Long id) {
        return historicalMilestoneRepository.findById(id)
            .map(this::toMilestoneDTO)
            .orElseThrow(() -> new ResourceNotFoundException("HistoricalMilestone", "id", id));
    }

    // ---- Board members ----
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'boardMembers'")
    public List<BoardMemberDTO> getBoardMembers() {
        return boardMemberRepository.findAllByActiveTrueOrderByDisplayOrderAsc()
            .stream().map(this::toBoardMemberDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'boardMember:' + #id")
    public BoardMemberDTO getBoardMemberById(Long id) {
        return boardMemberRepository.findById(id)
            .map(this::toBoardMemberDTO)
            .orElseThrow(() -> new ResourceNotFoundException("BoardMember", "id", id));
    }

    @CacheEvict(value = "investorContent", allEntries = true)
    public BoardMemberDTO createBoardMember(BoardMemberDTO dto) {
        BoardMember e = BoardMember.builder()
            .fullName(dto.getFullName()).role(dto.getRole()).bio(dto.getBio())
            .cvDocumentUrl(dto.getCvDocumentUrl() != null ? dto.getCvDocumentUrl() : "")
            .photoUrl(dto.getPhotoUrl() != null ? dto.getPhotoUrl() : "")
            .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
            .active(true).build();
        return toBoardMemberDTO(boardMemberRepository.save(e));
    }

    @CacheEvict(value = "investorContent", allEntries = true)
    public BoardMemberDTO updateBoardMember(Long id, BoardMemberDTO dto) {
        BoardMember e = boardMemberRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("BoardMember", "id", id));
        if (dto.getFullName() != null) e.setFullName(dto.getFullName());
        if (dto.getRole() != null) e.setRole(dto.getRole());
        if (dto.getBio() != null) e.setBio(dto.getBio());
        if (dto.getCvDocumentUrl() != null) e.setCvDocumentUrl(dto.getCvDocumentUrl());
        if (dto.getPhotoUrl() != null) e.setPhotoUrl(dto.getPhotoUrl());
        if (dto.getDisplayOrder() != null) e.setDisplayOrder(dto.getDisplayOrder());
        return toBoardMemberDTO(boardMemberRepository.save(e));
    }

    @CacheEvict(value = "investorContent", allEntries = true)
    public void deleteBoardMember(Long id) {
        BoardMember e = boardMemberRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("BoardMember", "id", id));
        e.setActive(false);
        boardMemberRepository.save(e);
    }

    @CacheEvict(value = "investorContent", allEntries = true)
    public BoardMemberDTO uploadBoardMemberFile(Long id, MultipartFile file, String field) {
        try {
            String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "file";
            String filename = UUID.randomUUID() + "_" + original;
            Path uploadPath = Paths.get(uploadDir, "board-members", String.valueOf(id));
            Files.createDirectories(uploadPath);
            Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            String url = baseUrl + "/uploads/board-members/" + id + "/" + filename;
            BoardMemberDTO dto = new BoardMemberDTO();
            if ("photo".equals(field)) dto.setPhotoUrl(url);
            else dto.setCvDocumentUrl(url);
            return updateBoardMember(id, dto);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to upload board member file", ex);
        }
    }

    // ---- Corporate governance reports ----
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'corporateGovernanceReports'")
    public List<CorporateGovernanceReportDTO> getCorporateGovernanceReports() {
        return corporateGovernanceReportRepository.findAllByOrderByReportYearDesc()
            .stream().map(this::toGovernanceReportDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'corporateGovernanceReport:' + #id")
    public CorporateGovernanceReportDTO getCorporateGovernanceReportById(Long id) {
        return corporateGovernanceReportRepository.findById(id)
            .map(this::toGovernanceReportDTO)
            .orElseThrow(() -> new ResourceNotFoundException("CorporateGovernanceReport", "id", id));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public CorporateGovernanceReportDTO createCorporateGovernanceReport(CorporateGovernanceReportDTO dto) {
        CorporateGovernanceReport entity = CorporateGovernanceReport.builder()
            .title(dto.getTitle())
            .documentUrl(dto.getDocumentUrl() != null ? dto.getDocumentUrl() : "#")
            .reportYear(dto.getReportYear())
            .language(dto.getLanguage() != null ? dto.getLanguage() : "pt")
            .build();
        return toGovernanceReportDTO(corporateGovernanceReportRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public CorporateGovernanceReportDTO updateCorporateGovernanceReport(Long id, CorporateGovernanceReportDTO dto) {
        CorporateGovernanceReport entity = corporateGovernanceReportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("CorporateGovernanceReport", "id", id));
        if (dto.getTitle() != null) entity.setTitle(dto.getTitle());
        if (dto.getDocumentUrl() != null) entity.setDocumentUrl(dto.getDocumentUrl());
        if (dto.getReportYear() != null) entity.setReportYear(dto.getReportYear());
        if (dto.getLanguage() != null) entity.setLanguage(dto.getLanguage());
        return toGovernanceReportDTO(corporateGovernanceReportRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public void deleteCorporateGovernanceReport(Long id) {
        corporateGovernanceReportRepository.deleteById(id);
    }

    public String uploadCorporateGovernanceReportFile(MultipartFile file, Long id) {
        try {
            Path uploadPath = Paths.get(uploadDir, "corporate-governance-reports", String.valueOf(id));
            Files.createDirectories(uploadPath);
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
            Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return baseUrl + "/uploads/corporate-governance-reports/" + id + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    // ---- Financial statements ----
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'financialStatements:' + (#fromYear != null ? #fromYear : 'all') + ':' + (#toYear != null ? #toYear : 'all')")
    public List<FinancialStatementDTO> getFinancialStatements(Integer fromYear, Integer toYear) {
        List<FinancialStatement> list = (fromYear != null && toYear != null)
            ? financialStatementRepository.findByYearBetweenOrderByYearDesc(fromYear, toYear)
            : financialStatementRepository.findAllByOrderByYearDesc();
        return list.stream().map(this::toFinancialStatementDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'financialStatement:' + #id")
    public FinancialStatementDTO getFinancialStatementById(Long id) {
        return financialStatementRepository.findById(id)
            .map(this::toFinancialStatementDTO)
            .orElseThrow(() -> new ResourceNotFoundException("FinancialStatement", "id", id));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public FinancialStatementDTO createFinancialStatement(FinancialStatementDTO dto) {
        FinancialStatement entity = FinancialStatement.builder()
            .year(dto.getYear())
            .title(dto.getTitle())
            .documentUrl(dto.getDocumentUrl() != null ? dto.getDocumentUrl() : "#")
            .statementType(dto.getStatementType())
            .language(dto.getLanguage() != null ? dto.getLanguage() : "pt")
            .build();
        return toFinancialStatementDTO(financialStatementRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public FinancialStatementDTO updateFinancialStatement(Long id, FinancialStatementDTO dto) {
        FinancialStatement entity = financialStatementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("FinancialStatement", "id", id));
        if (dto.getYear() != null) entity.setYear(dto.getYear());
        if (dto.getTitle() != null) entity.setTitle(dto.getTitle());
        if (dto.getDocumentUrl() != null) entity.setDocumentUrl(dto.getDocumentUrl());
        if (dto.getStatementType() != null) entity.setStatementType(dto.getStatementType());
        if (dto.getLanguage() != null) entity.setLanguage(dto.getLanguage());
        return toFinancialStatementDTO(financialStatementRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public void deleteFinancialStatement(Long id) {
        financialStatementRepository.deleteById(id);
    }

    public String uploadFinancialStatementFile(MultipartFile file, Long id) {
        try {
            Path uploadPath = Paths.get(uploadDir, "financial-statements", String.valueOf(id));
            Files.createDirectories(uploadPath);
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");
            Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return baseUrl + "/uploads/financial-statements/" + id + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    // ---- Business indicators ----
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'businessIndicators:' + (#category != null ? #category : 'all')")
    public List<BusinessIndicatorDTO> getBusinessIndicators(String category) {
        List<BusinessIndicator> list = category != null && !category.isBlank()
            ? businessIndicatorRepository.findByCategoryOrderByPeriodYearDesc(category.trim())
            : businessIndicatorRepository.findAllByOrderByPeriodYearDescPeriodQuarterDesc();
        return list.stream().map(this::toBusinessIndicatorDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'businessIndicator:' + #id")
    public BusinessIndicatorDTO getBusinessIndicatorById(Long id) {
        return businessIndicatorRepository.findById(id)
            .map(this::toBusinessIndicatorDTO)
            .orElseThrow(() -> new ResourceNotFoundException("BusinessIndicator", "id", id));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public BusinessIndicatorDTO createBusinessIndicator(BusinessIndicatorDTO dto) {
        BusinessIndicator entity = BusinessIndicator.builder()
            .title(dto.getTitle())
            .indicatorValue(dto.getIndicatorValue())
            .numericValue(dto.getNumericValue())
            .periodYear(dto.getPeriodYear() != null ? dto.getPeriodYear() : java.time.LocalDateTime.now().getYear())
            .periodQuarter(dto.getPeriodQuarter())
            .category(dto.getCategory() != null ? dto.getCategory() : "Outro")
            .unit(dto.getUnit())
            .build();
        return toBusinessIndicatorDTO(businessIndicatorRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public BusinessIndicatorDTO updateBusinessIndicator(Long id, BusinessIndicatorDTO dto) {
        BusinessIndicator entity = businessIndicatorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("BusinessIndicator", "id", id));
        if (dto.getTitle() != null) entity.setTitle(dto.getTitle());
        if (dto.getIndicatorValue() != null) entity.setIndicatorValue(dto.getIndicatorValue());
        if (dto.getNumericValue() != null) entity.setNumericValue(dto.getNumericValue());
        if (dto.getPeriodYear() != null) entity.setPeriodYear(dto.getPeriodYear());
        if (dto.getPeriodQuarter() != null) entity.setPeriodQuarter(dto.getPeriodQuarter());
        if (dto.getCategory() != null) entity.setCategory(dto.getCategory());
        if (dto.getUnit() != null) entity.setUnit(dto.getUnit());
        return toBusinessIndicatorDTO(businessIndicatorRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public void deleteBusinessIndicator(Long id) {
        businessIndicatorRepository.deleteById(id);
    }

    // ---- Communications ----
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'communications:' + (#type != null ? #type : 'all') + ':' + (#section != null ? #section : 'all')")
    public List<CommunicationDTO> getCommunications(String type, String section) {
        List<Communication> list;
        if (section != null && !section.isBlank()) {
            list = communicationRepository.findBySectionAndActiveTrueOrderByPublishedAtDesc(section.trim());
        } else if (type != null && !type.isBlank()) {
            list = communicationRepository.findByCommunicationTypeAndActiveTrueOrderByPublishedAtDesc(type.trim());
        } else {
            list = communicationRepository.findAllByActiveTrueOrderByPublishedAtDesc();
        }
        return list.stream().map(this::toCommunicationDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'communication:' + #id")
    public CommunicationDTO getCommunicationById(Long id) {
        return communicationRepository.findById(id)
            .map(this::toCommunicationDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Communication", "id", id));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public CommunicationDTO createCommunication(CommunicationDTO dto) {
        Communication entity = Communication.builder()
            .title(dto.getTitle())
            .communicationType(dto.getCommunicationType())
            .summary(dto.getSummary())
            .documentUrl(dto.getDocumentUrl())
            .publishedAt(dto.getPublishedAt() != null ? dto.getPublishedAt() : java.time.LocalDateTime.now())
            .slugId(dto.getSlugId())
            .category(dto.getCategory())
            .contentHtml(dto.getContentHtml())
            .imageUrl(dto.getImageUrl())
            .author(dto.getAuthor())
            .displaySections(dto.getDisplaySections() != null && !dto.getDisplaySections().isBlank() ? dto.getDisplaySections() : "HOME,COMUNICADOS")
            .active(true)
            .build();
        return toCommunicationDTO(communicationRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public CommunicationDTO updateCommunication(Long id, CommunicationDTO dto) {
        Communication entity = communicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Communication", "id", id));
        entity.setTitle(dto.getTitle());
        entity.setCommunicationType(dto.getCommunicationType());
        entity.setSummary(dto.getSummary());
        entity.setDocumentUrl(dto.getDocumentUrl());
        if (dto.getPublishedAt() != null) entity.setPublishedAt(dto.getPublishedAt());
        entity.setSlugId(dto.getSlugId());
        entity.setCategory(dto.getCategory());
        entity.setContentHtml(dto.getContentHtml());
        entity.setImageUrl(dto.getImageUrl());
        entity.setAuthor(dto.getAuthor());
        if (dto.getDisplaySections() != null && !dto.getDisplaySections().isBlank()) {
            entity.setDisplaySections(dto.getDisplaySections());
        }
        return toCommunicationDTO(communicationRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public void deleteCommunication(Long id) {
        Communication entity = communicationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Communication", "id", id));
        entity.setActive(false);
        communicationRepository.save(entity);
    }

    // ---- Events ----
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'events:' + (#upcomingOnly != null ? #upcomingOnly : 'all')")
    public List<EventDTO> getEvents(Boolean upcomingOnly) {
        List<Event> list = Boolean.TRUE.equals(upcomingOnly)
            ? eventRepository.findByEventDateAfterAndActiveTrueOrderByEventDateAsc(LocalDateTime.now())
            : eventRepository.findAllByActiveTrueOrderByEventDateAsc();
        return list.stream().map(this::toEventDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'event:' + #id")
    public EventDTO getEventById(Long id) {
        return eventRepository.findById(id)
            .map(this::toEventDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public EventDTO createEvent(EventDTO dto) {
        Event entity = Event.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .eventDate(dto.getEventDate())
            .endDate(dto.getEndDate())
            .location(dto.getLocation())
            .eventType(dto.getEventType())
            .active(true)
            .build();
        return toEventDTO(eventRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public EventDTO updateEvent(Long id, EventDTO dto) {
        Event entity = eventRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setEventDate(dto.getEventDate());
        entity.setEndDate(dto.getEndDate());
        entity.setLocation(dto.getLocation());
        entity.setEventType(dto.getEventType());
        return toEventDTO(eventRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public void deleteEvent(Long id) {
        Event entity = eventRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        entity.setActive(false);
        eventRepository.save(entity);
    }

    // ---- Subsidiaries ----
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'subsidiaries'")
    public List<SubsidiaryDTO> getSubsidiaries() {
        return subsidiaryRepository.findAllByActiveTrueOrderByEntityNameAsc()
            .stream().map(this::toSubsidiaryDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'subsidiary:' + #id")
    public SubsidiaryDTO getSubsidiaryById(Long id) {
        return subsidiaryRepository.findById(id)
            .map(this::toSubsidiaryDTO)
            .orElseThrow(() -> new ResourceNotFoundException("Subsidiary", "id", id));
    }

    // ---- General Assembly documents ----
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'generalAssemblyDocuments'")
    public List<GeneralAssemblyDocumentDTO> getGeneralAssemblyDocuments() {
        return generalAssemblyDocumentRepository.findAllByOrderByAssemblyYearDesc()
            .stream().map(this::toGeneralAssemblyDocumentDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'generalAssemblyDocument:' + #id")
    public GeneralAssemblyDocumentDTO getGeneralAssemblyDocumentById(Long id) {
        return generalAssemblyDocumentRepository.findById(id)
            .map(this::toGeneralAssemblyDocumentDTO)
            .orElseThrow(() -> new ResourceNotFoundException("GeneralAssemblyDocument", "id", id));
    }

    // ---- General Assemblies ----
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'generalAssemblies:' + (#assemblyType != null ? #assemblyType : 'all')")
    public List<GeneralAssemblyDTO> getGeneralAssemblies(String assemblyType) {
        List<GeneralAssembly> list = assemblyType != null && !assemblyType.isBlank()
            ? generalAssemblyRepository.findAllByActiveTrueAndAssemblyTypeOrderByMeetingYearDesc(assemblyType.trim())
            : generalAssemblyRepository.findAllByActiveTrueOrderByMeetingYearDesc();
        return list.stream().map(this::toGeneralAssemblyDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'generalAssembly:' + #id")
    public GeneralAssemblyDTO getGeneralAssemblyById(Long id) {
        return generalAssemblyRepository.findById(id)
            .map(this::toGeneralAssemblyDTO)
            .orElseThrow(() -> new ResourceNotFoundException("GeneralAssembly", "id", id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'generalAssemblyBySlug:' + #slugId")
    public GeneralAssemblyDTO getGeneralAssemblyBySlug(String slugId) {
        return generalAssemblyRepository.findBySlugId(slugId)
            .map(this::toGeneralAssemblyDTO)
            .orElseThrow(() -> new ResourceNotFoundException("GeneralAssembly", "slugId", slugId));
    }

    // ---- General Assembly CRUD ----

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public GeneralAssemblyDTO createGeneralAssembly(GeneralAssemblyDTO dto) {
        GeneralAssembly entity = GeneralAssembly.builder()
            .slugId(dto.getSlugId() != null ? dto.getSlugId() : UUID.randomUUID().toString())
            .title(dto.getTitle())
            .meetingYear(dto.getMeetingYear())
            .meetingDate(dto.getMeetingDate())
            .status(dto.getStatus() != null ? dto.getStatus() : "Realizada")
            .assemblyType(dto.getAssemblyType() != null ? dto.getAssemblyType() : "Ordinária")
            .summary(dto.getSummary())
            .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
            .active(true)
            .build();
        if (dto.getAgendaItems() != null) {
            for (int i = 0; i < dto.getAgendaItems().size(); i++) {
                final int order = i;
                GeneralAssemblyAgendaItem item = GeneralAssemblyAgendaItem.builder()
                    .assembly(entity).itemText(dto.getAgendaItems().get(i)).displayOrder(order).build();
                entity.getAgendaItems().add(item);
            }
        }
        return toGeneralAssemblyDTO(generalAssemblyRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public GeneralAssemblyDTO updateGeneralAssembly(Long id, GeneralAssemblyDTO dto) {
        GeneralAssembly entity = generalAssemblyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("GeneralAssembly", "id", id));
        if (dto.getTitle() != null) entity.setTitle(dto.getTitle());
        if (dto.getMeetingYear() != null) entity.setMeetingYear(dto.getMeetingYear());
        if (dto.getMeetingDate() != null) entity.setMeetingDate(dto.getMeetingDate());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getAssemblyType() != null) entity.setAssemblyType(dto.getAssemblyType());
        if (dto.getSummary() != null) entity.setSummary(dto.getSummary());
        if (dto.getDisplayOrder() != null) entity.setDisplayOrder(dto.getDisplayOrder());
        if (dto.getAgendaItems() != null) {
            entity.getAgendaItems().clear();
            for (int i = 0; i < dto.getAgendaItems().size(); i++) {
                final int order = i;
                entity.getAgendaItems().add(GeneralAssemblyAgendaItem.builder()
                    .assembly(entity).itemText(dto.getAgendaItems().get(i)).displayOrder(order).build());
            }
        }
        return toGeneralAssemblyDTO(generalAssemblyRepository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public void deleteGeneralAssembly(Long id) {
        GeneralAssembly entity = generalAssemblyRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("GeneralAssembly", "id", id));
        entity.setActive(false);
        generalAssemblyRepository.save(entity);
    }

    // ---- Assembly document management ----

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public GeneralAssemblyDocumentDTO addAssemblyDocument(Long assemblyId, GeneralAssemblyDocumentDTO dto) {
        GeneralAssembly assembly = generalAssemblyRepository.findById(assemblyId)
            .orElseThrow(() -> new ResourceNotFoundException("GeneralAssembly", "id", assemblyId));
        GeneralAssemblyDocument doc = GeneralAssemblyDocument.builder()
            .assembly(assembly)
            .assemblyYear(dto.getAssemblyYear() != null ? dto.getAssemblyYear() : assembly.getMeetingYear())
            .title(dto.getTitle())
            .documentUrl(dto.getDocumentUrl())
            .documentType(dto.getDocumentType() != null ? dto.getDocumentType() : "PDF")
            .fileSizeLabel(dto.getFileSizeLabel())
            .assemblyDate(dto.getAssemblyDate())
            .build();
        return toGeneralAssemblyDocumentDTO(generalAssemblyDocumentRepository.save(doc));
    }

    @Transactional
    @CacheEvict(value = "investorContent", allEntries = true)
    public void deleteAssemblyDocument(Long docId) {
        if (!generalAssemblyDocumentRepository.existsById(docId)) {
            throw new ResourceNotFoundException("GeneralAssemblyDocument", "id", docId);
        }
        generalAssemblyDocumentRepository.deleteById(docId);
    }

    // ---- File upload ----

    public String uploadAssemblyFile(MultipartFile file, Long assemblyId) {
        try {
            Path uploadPath = Paths.get(uploadDir, "assemblies", String.valueOf(assemblyId));
            Files.createDirectories(uploadPath);
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename()
                .replaceAll("[^a-zA-Z0-9._-]", "_");
            Path dest = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
            return baseUrl + "/uploads/assemblies/" + assemblyId + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    public String uploadCommunicationFile(MultipartFile file, Long commId) {
        try {
            Path uploadPath = Paths.get(uploadDir, "communications", String.valueOf(commId));
            Files.createDirectories(uploadPath);
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename()
                .replaceAll("[^a-zA-Z0-9._-]", "_");
            Path dest = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);
            return baseUrl + "/uploads/communications/" + commId + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store communication file: " + e.getMessage(), e);
        }
    }
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'shareholderStructure'")
    public List<ShareholderStructureDTO> getShareholderStructure() {
        return shareholderStructureRepository.findAllByActiveTrueOrderByDisplayOrderAsc()
            .stream().map(this::toShareholderStructureDTO).collect(Collectors.toList());
    }

    // ---- Investor Relations ----
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'investorRelations'")
    public List<InvestorRelationsDTO> getInvestorRelations() {
        return investorRelationsRepository.findAll().stream().map(this::toInvestorRelationsDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'investorRelations:' + #id")
    public InvestorRelationsDTO getInvestorRelationsById(Long id) {
        return investorRelationsRepository.findById(id)
            .map(this::toInvestorRelationsDTO)
            .orElseThrow(() -> new ResourceNotFoundException("InvestorRelations", "id", id));
    }

    // ---- External auditors ----
    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'externalAuditors'")
    public List<ExternalAuditorDTO> getExternalAuditors() {
        return externalAuditorRepository.findAllByOrderByPeriodFromDesc()
            .stream().map(this::toExternalAuditorDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'externalAuditor:' + #id")
    public ExternalAuditorDTO getExternalAuditorById(Long id) {
        return externalAuditorRepository.findById(id)
            .map(this::toExternalAuditorDTO)
            .orElseThrow(() -> new ResourceNotFoundException("ExternalAuditor", "id", id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "investorContent", key = "'externalAuditorCurrent'")
    public ExternalAuditorDTO getCurrentExternalAuditor() {
        return externalAuditorRepository.findFirstByCurrentTrue()
            .map(this::toExternalAuditorDTO)
            .orElse(null);
    }

    /**
     * Evict all investor content cache entries (e.g. after admin updates static content).
     */
    @CacheEvict(value = "investorContent", allEntries = true)
    public void evictInvestorContentCache() {
    }

    // ---- Organogram (JSON file-based) ----

    /**
     * Returns the full organogram hierarchy loaded from organograma.json in the classpath.
     * To update the organogram, edit src/main/resources/organograma.json and redeploy
     * (or call POST /api/v1/investor-content/cache/evict to refresh cache without redeploy).
     */
    @Cacheable(value = "investorContent", key = "'organogram'")
    public OrganigramDTO getOrganogram() {
        return readJson("organograma.json", OrganigramDTO.class);
    }

    /**
     * Saves the full organogram hierarchy back to organograma.json on the classpath.
     * Also evicts the cached version so subsequent GET calls return fresh data.
     */
    @CacheEvict(value = "investorContent", key = "'organogram'")
    public OrganigramDTO saveOrganogram(OrganigramDTO dto) {
        try {
            // Update meta timestamp
            if (dto.getMeta() != null) {
                dto.getMeta().setUpdatedAt(java.time.LocalDate.now().toString());
            }
            writeJson("organograma.json", dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save organograma.json", e);
        }
    }

    // ---- Calendário de Divulgações (JSON file-based) ----

    @Cacheable(value = "investorContent", key = "'calendarioDivulgacoes'")
    public CalendarioDivulgacoesDTO getCalendarioDivulgacoes() {
        return readJson("calendario_divulgacoes.json", CalendarioDivulgacoesDTO.class);
    }

    @CacheEvict(value = "investorContent", key = "'calendarioDivulgacoes'")
    public CalendarioDivulgacoesDTO saveCalendarioDivulgacoes(CalendarioDivulgacoesDTO dto) {
        try {
            dto.setUpdatedAt(java.time.LocalDate.now().toString());
            writeJson("calendario_divulgacoes.json", dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save calendario_divulgacoes.json", e);
        }
    }

    // ---- Estatutos Sociais (JSON file-based) ----

    @Cacheable(value = "investorContent", key = "'estatutos'")
    public EstatutosDTO getEstatutos() {
        return readJson("estatutos.json", EstatutosDTO.class);
    }

    @CacheEvict(value = "investorContent", key = "'estatutos'")
    public EstatutosDTO saveEstatutos(EstatutosDTO dto) {
        try {
            dto.setUpdatedAt(java.time.LocalDate.now().toString());
            writeJson("estatutos.json", dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save estatutos.json", e);
        }
    }

    // ---- Annual Report Destaque (JSON file-based) ----

    @Cacheable(value = "investorContent", key = "'annualReportDestaque'")
    public AnnualReportDestaqueDTO getAnnualReportDestaque() {
        return readJson("annual_report_destaque.json", AnnualReportDestaqueDTO.class);
    }

    @CacheEvict(value = "investorContent", key = "'annualReportDestaque'")
    public AnnualReportDestaqueDTO saveAnnualReportDestaque(AnnualReportDestaqueDTO dto) {
        try {
            dto.setUpdatedAt(java.time.LocalDate.now().toString());
            writeJson("annual_report_destaque.json", dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save annual_report_destaque.json", e);
        }
    }

    // ---- Investor FAQ (JSON file-based) ----

    @Cacheable(value = "investorContent", key = "'investorFaq'")
    public InvestorFaqDTO getInvestorFaq() {
        return readJson("investor_faq.json", InvestorFaqDTO.class);
    }

    @CacheEvict(value = "investorContent", key = "'investorFaq'")
    public InvestorFaqDTO saveInvestorFaq(InvestorFaqDTO dto) {
        try {
            dto.setUpdatedAt(java.time.LocalDate.now().toString());
            writeJson("investor_faq.json", dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save investor_faq.json", e);
        }
    }

    // ---- Órgãos Sociais Members (JSON file-based) ----

    @Cacheable(value = "investorContent", key = "'organMembers'")
    public OrganMembersDTO getOrganMembers() {
        return readJson("organ_members.json", OrganMembersDTO.class);
    }

    @CacheEvict(value = "investorContent", key = "'organMembers'")
    public OrganMembersDTO saveOrganMembers(OrganMembersDTO dto) {
        try {
            dto.setUpdatedAt(java.time.LocalDate.now().toString());
            writeJson("organ_members.json", dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save organ_members.json", e);
        }
    }

    // ---- Carousel Slides (JSON file-based) ----

    @Cacheable(value = "investorContent", key = "'carouselSlides'")
    public CarouselSlidesDTO getCarouselSlides() {
        return readJson("carousel_slides.json", CarouselSlidesDTO.class);
    }

    @CacheEvict(value = "investorContent", key = "'carouselSlides'")
    public String uploadCarouselSlideImage(String slideId, MultipartFile file) {
        try {
            String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image";
            String filename = UUID.randomUUID() + "_" + original;
            Path uploadPath = Paths.get(uploadDir, "carousel-slides", slideId);
            Files.createDirectories(uploadPath);
            Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            String url = baseUrl + "/uploads/carousel-slides/" + slideId + "/" + filename;

            CarouselSlidesDTO current = getCarouselSlides();
            if (current.getSlides() != null) {
                current.getSlides().stream()
                        .filter(slide -> slideId.equals(slide.getId()))
                        .findFirst()
                        .ifPresent(slide -> slide.setImageUrl(url));
            }
            saveCarouselSlides(current);

            return url;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload carousel slide image", e);
        }
    }

    @CacheEvict(value = "investorContent", key = "'carouselSlides'")
    public CarouselSlidesDTO saveCarouselSlides(CarouselSlidesDTO dto) {
        try {
            dto.setUpdatedAt(java.time.LocalDate.now().toString());
            writeJson("carousel_slides.json", dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save carousel_slides.json", e);
        }
    }

    // ---- Who We Are (JSON file-based) ----

    @Cacheable(value = "investorContent", key = "'aboutWhoWeAre'")
    public WhoWeAreContentDTO getWhoWeAreContent() {
        return readJson("who_we_are.json", WhoWeAreContentDTO.class);
    }

    @CacheEvict(value = "investorContent", key = "'aboutWhoWeAre'")
    public WhoWeAreContentDTO saveWhoWeAreContent(WhoWeAreContentDTO dto) {
        try {
            dto.setRoute("/ensa/quem-somos");
            dto.setUpdatedAt(java.time.LocalDate.now().toString());

            writeJson("who_we_are.json", dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save who_we_are.json", e);
        }
    }

    // ---- Participadas (JSON file-based) ----

    @Cacheable(value = "investorContent", key = "'participadas'")
    public ParticipadasDataDTO getParticipadas() {
        return readJson("participadas.json", ParticipadasDataDTO.class);
    }

    @CacheEvict(value = "investorContent", key = "'participadas'")
    public ParticipadasDataDTO saveParticipadas(ParticipadasDataDTO dto) {
        try {
            dto.setUpdatedAt(java.time.LocalDate.now().toString());
            writeJson("participadas.json", dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save participadas.json", e);
        }
    }

    @Cacheable(value = "investorContent", key = "'financialIndicatorsPage'")
    public FinancialIndicatorsPageDTO getFinancialIndicatorsPage() {
        return readJson("financial_indicators.json", FinancialIndicatorsPageDTO.class);
    }

    @CacheEvict(value = "investorContent", key = "'financialIndicatorsPage'")
    public FinancialIndicatorsPageDTO saveFinancialIndicatorsPage(FinancialIndicatorsPageDTO dto) {
        try {
            dto.setRoute("/ensa/indicadores-financeiros");
            dto.setUpdatedAt(java.time.LocalDate.now().toString());

            writeJson("financial_indicators.json", dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save financial_indicators.json", e);
        }
    }

    // ---- CEO Message (JSON file-based) ----

    @Cacheable(value = "investorContent", key = "'ceoMessage'")
    public CeoMessageDTO getCeoMessage() {
        return readJson("ceo_message.json", CeoMessageDTO.class);
    }

    @CacheEvict(value = "investorContent", key = "'ceoMessage'")
    public CeoMessageDTO saveCeoMessage(CeoMessageDTO dto) {
        try {
            dto.setUpdatedAt(java.time.LocalDate.now().toString());
            writeJson("ceo_message.json", dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save ceo_message.json", e);
        }
    }

    @CacheEvict(value = "investorContent", key = "'ceoMessage'")
    public String uploadCeoPhoto(MultipartFile file) {
        try {
            String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "photo";
            String filename = UUID.randomUUID() + "_" + original;
            Path uploadPath = Paths.get(uploadDir, "ceo-message");
            Files.createDirectories(uploadPath);
            Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            String url = baseUrl + "/uploads/ceo-message/" + filename;
            // Patch the JSON's photoUrl immediately
            CeoMessageDTO current = getCeoMessage();
            current.setPhotoUrl(url);
            saveCeoMessage(current);
            return url;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload CEO photo", e);
        }
    }

    // ---- Plano Estratégico ----
    @Cacheable(value = "investorContent", key = "'planoEstrategico'")
    public PlanoEstrategicoDTO getPlanoEstrategico() {
        return readJson("plano_estrategico.json", PlanoEstrategicoDTO.class);
    }

    @CacheEvict(value = "investorContent", key = "'planoEstrategico'")
    public PlanoEstrategicoDTO savePlanoEstrategico(PlanoEstrategicoDTO dto) {
        try {
            writeJson("plano_estrategico.json", dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save plano_estrategico.json", e);
        }
    }

    // ---- Mappers ----
    private HistoricalMilestoneDTO toMilestoneDTO(HistoricalMilestone e) {
        return HistoricalMilestoneDTO.builder()
            .id(e.getId()).title(e.getTitle()).description(e.getDescription())
            .milestoneYear(e.getMilestoneYear()).displayOrder(e.getDisplayOrder())
            .imageUrl(e.getImageUrl()).contentHtml(e.getContentHtml()).eventTitle(e.getEventTitle())
            .createdAt(e.getCreatedAt()).build();
    }

    private BoardMemberDTO toBoardMemberDTO(BoardMember e) {
        return BoardMemberDTO.builder()
            .id(e.getId()).fullName(e.getFullName()).role(e.getRole()).bio(e.getBio())
            .cvDocumentUrl(e.getCvDocumentUrl()).photoUrl(e.getPhotoUrl()).displayOrder(e.getDisplayOrder()).createdAt(e.getCreatedAt()).build();
    }

    private CorporateGovernanceReportDTO toGovernanceReportDTO(CorporateGovernanceReport e) {
        return CorporateGovernanceReportDTO.builder()
            .id(e.getId()).title(e.getTitle()).documentUrl(e.getDocumentUrl()).reportYear(e.getReportYear())
            .language(e.getLanguage()).createdAt(e.getCreatedAt()).build();
    }

    private FinancialStatementDTO toFinancialStatementDTO(FinancialStatement e) {
        return FinancialStatementDTO.builder()
            .id(e.getId()).year(e.getYear()).title(e.getTitle()).documentUrl(e.getDocumentUrl())
            .statementType(e.getStatementType()).language(e.getLanguage()).createdAt(e.getCreatedAt()).build();
    }

    private BusinessIndicatorDTO toBusinessIndicatorDTO(BusinessIndicator e) {
        return BusinessIndicatorDTO.builder()
            .id(e.getId()).title(e.getTitle()).indicatorValue(e.getIndicatorValue()).numericValue(e.getNumericValue())
            .periodYear(e.getPeriodYear()).periodQuarter(e.getPeriodQuarter()).category(e.getCategory()).unit(e.getUnit()).createdAt(e.getCreatedAt()).build();
    }

    private CommunicationDTO toCommunicationDTO(Communication e) {
        return CommunicationDTO.builder()
            .id(e.getId()).title(e.getTitle()).communicationType(e.getCommunicationType()).summary(e.getSummary())
            .documentUrl(e.getDocumentUrl()).publishedAt(e.getPublishedAt())
            .slugId(e.getSlugId()).category(e.getCategory()).contentHtml(e.getContentHtml())
            .imageUrl(e.getImageUrl()).author(e.getAuthor())
            .displaySections(e.getDisplaySections())
            .createdAt(e.getCreatedAt()).build();
    }

    private EventDTO toEventDTO(Event e) {
        return EventDTO.builder()
            .id(e.getId()).title(e.getTitle()).description(e.getDescription()).eventDate(e.getEventDate()).endDate(e.getEndDate())
            .location(e.getLocation()).eventType(e.getEventType()).createdAt(e.getCreatedAt()).build();
    }

    private SubsidiaryDTO toSubsidiaryDTO(Subsidiary e) {
        return SubsidiaryDTO.builder()
            .id(e.getId()).entityName(e.getEntityName()).description(e.getDescription()).participationPercentage(e.getParticipationPercentage())
            .country(e.getCountry()).websiteUrl(e.getWebsiteUrl()).createdAt(e.getCreatedAt()).build();
    }

    private GeneralAssemblyDocumentDTO toGeneralAssemblyDocumentDTO(GeneralAssemblyDocument e) {
        return GeneralAssemblyDocumentDTO.builder()
            .id(e.getId()).assemblyYear(e.getAssemblyYear()).title(e.getTitle()).documentUrl(e.getDocumentUrl())
            .assemblyDate(e.getAssemblyDate()).documentType(e.getDocumentType())
            .assemblyId(e.getAssembly() != null ? e.getAssembly().getId() : null)
            .fileSizeLabel(e.getFileSizeLabel())
            .createdAt(e.getCreatedAt()).build();
    }

    private GeneralAssemblyDTO toGeneralAssemblyDTO(GeneralAssembly e) {
        List<String> agenda = e.getAgendaItems().stream()
            .map(GeneralAssemblyAgendaItem::getItemText).collect(Collectors.toList());
        List<GeneralAssemblyDocumentDTO> docs = e.getDocuments().stream()
            .map(this::toGeneralAssemblyDocumentDTO).collect(Collectors.toList());
        return GeneralAssemblyDTO.builder()
            .id(e.getId()).slugId(e.getSlugId()).title(e.getTitle())
            .meetingYear(e.getMeetingYear()).meetingDate(e.getMeetingDate())
            .status(e.getStatus()).assemblyType(e.getAssemblyType()).summary(e.getSummary())
            .displayOrder(e.getDisplayOrder()).agendaItems(agenda).documents(docs).build();
    }

    private ShareholderStructureDTO toShareholderStructureDTO(ShareholderStructure e) {
        return ShareholderStructureDTO.builder()
            .id(e.getId()).shareholderName(e.getShareholderName()).sharesLabel(e.getSharesLabel())
            .percentage(e.getPercentage()).displayColor(e.getDisplayColor()).displayOrder(e.getDisplayOrder()).build();
    }

    private InvestorRelationsDTO toInvestorRelationsDTO(InvestorRelations e) {
        return InvestorRelationsDTO.builder()
            .id(e.getId()).email(e.getEmail()).phone(e.getPhone()).address(e.getAddress())
            .otherContacts(e.getOtherContacts()).updatedAt(e.getUpdatedAt()).build();
    }

    private ExternalAuditorDTO toExternalAuditorDTO(ExternalAuditor e) {
        return ExternalAuditorDTO.builder()
            .id(e.getId()).auditorName(e.getAuditorName()).contactInfo(e.getContactInfo())
            .periodFrom(e.getPeriodFrom()).periodTo(e.getPeriodTo()).current(e.isCurrent()).updatedAt(e.getUpdatedAt()).build();
    }
}
