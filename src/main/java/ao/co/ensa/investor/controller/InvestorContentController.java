package ao.co.ensa.investor.controller;

import ao.co.ensa.investor.model.dto.*;
import ao.co.ensa.investor.service.InvestorContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/investor-content")
@RequiredArgsConstructor
@Tag(name = "Investor Content", description = "Public ENSA investor relations content (milestones, reports, board, financials, events, etc.)")
public class InvestorContentController {

    private final InvestorContentService investorContentService;

    private <T> ResponseEntity<List<T>> okList(List<T> items) {
        return ResponseEntity.ok(items != null ? items : List.of());
    }

    @GetMapping("/media-assets/images")
    @Operation(summary = "List reusable image assets", description = "Lists all uploaded image files available under /uploads for reuse in editors.")
    public ResponseEntity<List<java.util.Map<String, String>>> listImageAssets() {
        return ResponseEntity.ok(investorContentService.listImageAssets());
    }

    @PostMapping(value = "/media-assets/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload reusable image asset", description = "Uploads an image to shared assets and returns its public URL.")
    public ResponseEntity<java.util.Map<String, String>> uploadSharedImage(@RequestParam("file") MultipartFile file) {
        String url = investorContentService.uploadSharedImage(file);
        return ResponseEntity.status(201).body(java.util.Map.of("url", url,
            "filename", file.getOriginalFilename() != null ? file.getOriginalFilename() : "image"));
    }

    @DeleteMapping("/media-assets/images")
    @Operation(summary = "Delete reusable image asset", description = "Deletes a shared image by relative path.")
    public ResponseEntity<Void> deleteSharedImage(@RequestParam("path") String path) {
        investorContentService.deleteSharedImage(path);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/media-assets/files")
    @Operation(summary = "List reusable file assets", description = "Lists all uploaded document files (pdf/doc/docx) available under /uploads for reuse in editors.")
    public ResponseEntity<List<java.util.Map<String, String>>> listFileAssets() {
        return ResponseEntity.ok(investorContentService.listFileAssets());
    }

    @PostMapping(value = "/media-assets/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload reusable file asset", description = "Uploads a document to shared assets and returns its public URL.")
    public ResponseEntity<java.util.Map<String, String>> uploadSharedFile(@RequestParam("file") MultipartFile file) {
        String url = investorContentService.uploadSharedFile(file);
        return ResponseEntity.status(201).body(java.util.Map.of("url", url,
            "filename", file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"));
    }

    @DeleteMapping("/media-assets/files")
    @Operation(summary = "Delete reusable file asset", description = "Deletes a shared document by relative path.")
    public ResponseEntity<Void> deleteSharedFile(@RequestParam("path") String path) {
        investorContentService.deleteSharedFile(path);
        return ResponseEntity.noContent().build();
    }

    // ---- Historical milestones ----
    @GetMapping("/historical-milestones")
    @Operation(summary = "List historical milestones", description = "Main historical milestones of ENSA")
    public ResponseEntity<List<HistoricalMilestoneDTO>> getHistoricalMilestones() {
        return okList(investorContentService.getHistoricalMilestones());
    }

    @GetMapping("/historical-milestones/{id}")
    @Operation(summary = "Get historical milestone by ID")
    public ResponseEntity<HistoricalMilestoneDTO> getHistoricalMilestoneById(@PathVariable Long id) {
        return ResponseEntity.ok(investorContentService.getHistoricalMilestoneById(id));
    }

    // ---- Board of Directors ----
    @GetMapping("/board-members")
    @Operation(summary = "List board members", description = "Board of Directors (CA) with description, organogram and CV links")
    public ResponseEntity<List<BoardMemberDTO>> getBoardMembers() {
        return okList(investorContentService.getBoardMembers());
    }

    @GetMapping("/board-members/{id}")
    @Operation(summary = "Get board member by ID")
    public ResponseEntity<BoardMemberDTO> getBoardMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(investorContentService.getBoardMemberById(id));
    }

    @PostMapping("/board-members")
    @Operation(summary = "Create board member")
    public ResponseEntity<BoardMemberDTO> createBoardMember(@RequestBody BoardMemberDTO dto) {
        return ResponseEntity.status(201).body(investorContentService.createBoardMember(dto));
    }

    @PutMapping("/board-members/{id}")
    @Operation(summary = "Update board member")
    public ResponseEntity<BoardMemberDTO> updateBoardMember(@PathVariable Long id, @RequestBody BoardMemberDTO dto) {
        return ResponseEntity.ok(investorContentService.updateBoardMember(id, dto));
    }

    @DeleteMapping("/board-members/{id}")
    @Operation(summary = "Soft-delete board member")
    public ResponseEntity<Void> deleteBoardMember(@PathVariable Long id) {
        investorContentService.deleteBoardMember(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/board-members/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload board member photo or CV")
    public ResponseEntity<BoardMemberDTO> uploadBoardMemberFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "field", defaultValue = "photo") String field) {
        return ResponseEntity.ok(investorContentService.uploadBoardMemberFile(id, file, field));
    }

    // ---- Corporate governance reports ----
    @GetMapping("/corporate-governance-reports")
    @Operation(summary = "List corporate governance reports", description = "Corporate governance report or equivalent documents")
    public ResponseEntity<List<CorporateGovernanceReportDTO>> getCorporateGovernanceReports() {
        return okList(investorContentService.getCorporateGovernanceReports());
    }

    @GetMapping("/corporate-governance-reports/{id}")
    @Operation(summary = "Get corporate governance report by ID")
    public ResponseEntity<CorporateGovernanceReportDTO> getCorporateGovernanceReportById(@PathVariable Long id) {
        return ResponseEntity.ok(investorContentService.getCorporateGovernanceReportById(id));
    }

    @PostMapping("/corporate-governance-reports")
    @Operation(summary = "Create corporate governance report")
    public ResponseEntity<CorporateGovernanceReportDTO> createCorporateGovernanceReport(@RequestBody CorporateGovernanceReportDTO dto) {
        return ResponseEntity.status(201).body(investorContentService.createCorporateGovernanceReport(dto));
    }

    @PutMapping("/corporate-governance-reports/{id}")
    @Operation(summary = "Update corporate governance report")
    public ResponseEntity<CorporateGovernanceReportDTO> updateCorporateGovernanceReport(@PathVariable Long id, @RequestBody CorporateGovernanceReportDTO dto) {
        return ResponseEntity.ok(investorContentService.updateCorporateGovernanceReport(id, dto));
    }

    @DeleteMapping("/corporate-governance-reports/{id}")
    @Operation(summary = "Delete corporate governance report")
    public ResponseEntity<Void> deleteCorporateGovernanceReport(@PathVariable Long id) {
        investorContentService.deleteCorporateGovernanceReport(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/corporate-governance-reports/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload document for a corporate governance report")
    public ResponseEntity<java.util.Map<String, String>> uploadCorporateGovernanceReportFile(
            @PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String url = investorContentService.uploadCorporateGovernanceReportFile(file, id);
        return ResponseEntity.status(201).body(java.util.Map.of("url", url,
            "filename", file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"));
    }

    // ---- Financial statements ----
    @GetMapping("/financial-statements")
    @Operation(summary = "List financial statements", description = "Reports and accounts (e.g. last 10 years)")
    public ResponseEntity<List<FinancialStatementDTO>> getFinancialStatements(
            @RequestParam(required = false) Integer fromYear,
            @RequestParam(required = false) Integer toYear) {
        return okList(investorContentService.getFinancialStatements(fromYear, toYear));
    }

    @GetMapping("/financial-statements/{id}")
    @Operation(summary = "Get financial statement by ID")
    public ResponseEntity<FinancialStatementDTO> getFinancialStatementById(@PathVariable Long id) {
        return ResponseEntity.ok(investorContentService.getFinancialStatementById(id));
    }

    @PostMapping("/financial-statements")
    @Operation(summary = "Create financial statement")
    public ResponseEntity<FinancialStatementDTO> createFinancialStatement(@RequestBody FinancialStatementDTO dto) {
        return ResponseEntity.status(201).body(investorContentService.createFinancialStatement(dto));
    }

    @PutMapping("/financial-statements/{id}")
    @Operation(summary = "Update financial statement")
    public ResponseEntity<FinancialStatementDTO> updateFinancialStatement(@PathVariable Long id, @RequestBody FinancialStatementDTO dto) {
        return ResponseEntity.ok(investorContentService.updateFinancialStatement(id, dto));
    }

    @DeleteMapping("/financial-statements/{id}")
    @Operation(summary = "Delete financial statement")
    public ResponseEntity<Void> deleteFinancialStatement(@PathVariable Long id) {
        investorContentService.deleteFinancialStatement(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/financial-statements/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload document for a financial statement")
    public ResponseEntity<java.util.Map<String, String>> uploadFinancialStatementFile(
            @PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String url = investorContentService.uploadFinancialStatementFile(file, id);
        return ResponseEntity.status(201).body(java.util.Map.of("url", url,
            "filename", file.getOriginalFilename() != null ? file.getOriginalFilename() : "file"));
    }

    // ---- Business indicators ----
    @GetMapping("/business-indicators")
    @Operation(summary = "List business indicators", description = "Main financial, activity and business indicators")
    public ResponseEntity<List<BusinessIndicatorDTO>> getBusinessIndicators(@RequestParam(required = false) String category) {
        return okList(investorContentService.getBusinessIndicators(category));
    }

    @GetMapping("/business-indicators/{id}")
    @Operation(summary = "Get business indicator by ID")
    public ResponseEntity<BusinessIndicatorDTO> getBusinessIndicatorById(@PathVariable Long id) {
        return ResponseEntity.ok(investorContentService.getBusinessIndicatorById(id));
    }

    @PostMapping("/business-indicators")
    @Operation(summary = "Create business indicator")
    public ResponseEntity<BusinessIndicatorDTO> createBusinessIndicator(@RequestBody BusinessIndicatorDTO dto) {
        return ResponseEntity.status(201).body(investorContentService.createBusinessIndicator(dto));
    }

    @PutMapping("/business-indicators/{id}")
    @Operation(summary = "Update business indicator")
    public ResponseEntity<BusinessIndicatorDTO> updateBusinessIndicator(@PathVariable Long id, @RequestBody BusinessIndicatorDTO dto) {
        return ResponseEntity.ok(investorContentService.updateBusinessIndicator(id, dto));
    }

    @DeleteMapping("/business-indicators/{id}")
    @Operation(summary = "Delete business indicator")
    public ResponseEntity<Void> deleteBusinessIndicator(@PathVariable Long id) {
        investorContentService.deleteBusinessIndicator(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Communications ----
    @GetMapping("/communications")
    @Operation(summary = "List communications", description = "Filter by type and/or section (HOME, COMUNICADOS, RELATORIOS)")
    public ResponseEntity<List<CommunicationDTO>> getCommunications(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String section) {
        return okList(investorContentService.getCommunications(type, section));
    }

    @GetMapping("/communications/{id}")
    @Operation(summary = "Get communication by ID")
    public ResponseEntity<CommunicationDTO> getCommunicationById(@PathVariable Long id) {
        return ResponseEntity.ok(investorContentService.getCommunicationById(id));
    }

    @PostMapping("/communications")
    @Operation(summary = "Create communication")
    public ResponseEntity<CommunicationDTO> createCommunication(@RequestBody CommunicationDTO dto) {
        return ResponseEntity.status(201).body(investorContentService.createCommunication(dto));
    }

    @PutMapping("/communications/{id}")
    @Operation(summary = "Update communication")
    public ResponseEntity<CommunicationDTO> updateCommunication(@PathVariable Long id, @RequestBody CommunicationDTO dto) {
        return ResponseEntity.ok(investorContentService.updateCommunication(id, dto));
    }

    @DeleteMapping("/communications/{id}")
    @Operation(summary = "Delete communication (soft delete)")
    public ResponseEntity<Void> deleteCommunication(@PathVariable Long id) {
        investorContentService.deleteCommunication(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/communications/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload file attachment for a communication")
    public ResponseEntity<java.util.Map<String, String>> uploadCommunicationFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String url = investorContentService.uploadCommunicationFile(file, id);
        long bytes = file.getSize();
        String sizeLabel = bytes < 1024 * 1024
            ? String.format("%.0f KB", bytes / 1024.0)
            : String.format("%.1f MB", bytes / (1024.0 * 1024));
        return ResponseEntity.status(201).body(java.util.Map.of(
            "url", url,
            "filename", file.getOriginalFilename() != null ? file.getOriginalFilename() : "file",
            "fileSizeLabel", sizeLabel
        ));
    }

    // ---- Events ----
    @GetMapping("/events")
    @Operation(summary = "List events", description = "Calendar of main meetings and events")
    public ResponseEntity<List<EventDTO>> getEvents(@RequestParam(required = false) Boolean upcomingOnly) {
        return okList(investorContentService.getEvents(upcomingOnly));
    }

    @GetMapping("/events/{id}")
    @Operation(summary = "Get event by ID")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(investorContentService.getEventById(id));
    }

    @PostMapping("/events")
    @Operation(summary = "Create event", description = "Add a new event to the disclosure calendar")
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO dto) {
        return ResponseEntity.status(201).body(investorContentService.createEvent(dto));
    }

    @PutMapping("/events/{id}")
    @Operation(summary = "Update event", description = "Update an existing disclosure calendar event")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO dto) {
        return ResponseEntity.ok(investorContentService.updateEvent(id, dto));
    }

    @DeleteMapping("/events/{id}")
    @Operation(summary = "Delete event (soft delete)", description = "Deactivate a disclosure calendar event")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        investorContentService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Subsidiaries ----
    @GetMapping("/subsidiaries")
    @Operation(summary = "List subsidiaries", description = "Portfolio of participated entities")
    public ResponseEntity<List<SubsidiaryDTO>> getSubsidiaries() {
        return okList(investorContentService.getSubsidiaries());
    }

    @GetMapping("/subsidiaries/{id}")
    @Operation(summary = "Get subsidiary by ID")
    public ResponseEntity<SubsidiaryDTO> getSubsidiaryById(@PathVariable Long id) {
        return ResponseEntity.ok(investorContentService.getSubsidiaryById(id));
    }

    // ---- General Assembly documents ----
    @GetMapping("/general-assembly-documents")
    @Operation(summary = "List General Assembly documents", description = "Support documents for General Assembly, including historical")
    public ResponseEntity<List<GeneralAssemblyDocumentDTO>> getGeneralAssemblyDocuments() {
        return okList(investorContentService.getGeneralAssemblyDocuments());
    }

    @GetMapping("/general-assembly-documents/{id}")
    @Operation(summary = "Get General Assembly document by ID")
    public ResponseEntity<GeneralAssemblyDocumentDTO> getGeneralAssemblyDocumentById(@PathVariable Long id) {
        return ResponseEntity.ok(investorContentService.getGeneralAssemblyDocumentById(id));
    }

    // ---- General Assemblies ----
    @GetMapping("/general-assemblies")
    @Operation(summary = "List General Assemblies", description = "List all General Assembly meetings, optionally filtered by type (Ordinária, Extraordinária, Eleitoral)")
    public ResponseEntity<List<GeneralAssemblyDTO>> getGeneralAssemblies(
            @RequestParam(required = false) String type) {
        return okList(investorContentService.getGeneralAssemblies(type));
    }

    @GetMapping("/general-assemblies/{id}")
    @Operation(summary = "Get General Assembly by ID")
    public ResponseEntity<GeneralAssemblyDTO> getGeneralAssemblyById(@PathVariable Long id) {
        return ResponseEntity.ok(investorContentService.getGeneralAssemblyById(id));
    }

    @GetMapping("/general-assemblies/slug/{slugId}")
    @Operation(summary = "Get General Assembly by slug ID")
    public ResponseEntity<GeneralAssemblyDTO> getGeneralAssemblyBySlug(@PathVariable String slugId) {
        return ResponseEntity.ok(investorContentService.getGeneralAssemblyBySlug(slugId));
    }

    // ---- Shareholder Structure ----
    @GetMapping("/shareholder-structure")
    @Operation(summary = "List shareholder structure", description = "Current ENSA shareholder composition with percentages")
    public ResponseEntity<List<ShareholderStructureDTO>> getShareholderStructure() {
        return okList(investorContentService.getShareholderStructure());
    }

    // ---- Investor Relations ----
    @GetMapping("/investor-relations")
    @Operation(summary = "List investor relations contacts", description = "Investor Relations contact data")
    public ResponseEntity<List<InvestorRelationsDTO>> getInvestorRelations() {
        return okList(investorContentService.getInvestorRelations());
    }

    @GetMapping("/investor-relations/{id}")
    @Operation(summary = "Get investor relations record by ID")
    public ResponseEntity<InvestorRelationsDTO> getInvestorRelationsById(@PathVariable Long id) {
        return ResponseEntity.ok(investorContentService.getInvestorRelationsById(id));
    }

    // ---- External auditors ----
    @GetMapping("/external-auditors")
    @Operation(summary = "List external auditors", description = "External auditor information")
    public ResponseEntity<List<ExternalAuditorDTO>> getExternalAuditors() {
        return okList(investorContentService.getExternalAuditors());
    }

    @GetMapping("/external-auditors/current")
    @Operation(summary = "Get current external auditor")
    public ResponseEntity<ExternalAuditorDTO> getCurrentExternalAuditor() {
        ExternalAuditorDTO dto = investorContentService.getCurrentExternalAuditor();
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.noContent().build();
    }

    @GetMapping("/external-auditors/{id}")
    @Operation(summary = "Get external auditor by ID")
    public ResponseEntity<ExternalAuditorDTO> getExternalAuditorById(@PathVariable Long id) {
        return ResponseEntity.ok(investorContentService.getExternalAuditorById(id));
    }

    // ---- CEO Message ----
    @GetMapping("/ceo-message")
    @Operation(summary = "Get CEO message", description = "Returns the editable CEO message shown on the home page.")
    public ResponseEntity<CeoMessageDTO> getCeoMessage() {
        return ResponseEntity.ok(investorContentService.getCeoMessage());
    }

    @PutMapping("/ceo-message")
    @Operation(summary = "Update CEO message", description = "Saves CEO message text fields to ceo_message.json.")
    public ResponseEntity<CeoMessageDTO> updateCeoMessage(@RequestBody CeoMessageDTO dto) {
        return ResponseEntity.ok(investorContentService.saveCeoMessage(dto));
    }

    @PostMapping(value = "/ceo-message/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload CEO photo", description = "Uploads the CEO cover photo, saves it under /uploads/ceo-message/, updates the JSON photoUrl.")
    public ResponseEntity<java.util.Map<String, String>> uploadCeoPhoto(@RequestParam("file") MultipartFile file) {
        String url = investorContentService.uploadCeoPhoto(file);
        return ResponseEntity.ok(java.util.Map.of("url", url));
    }

    @PostMapping("/cache/evict")
    @Operation(summary = "Evict investor content cache", description = "Admin-only: clear all investor content from Redis cache (use after updating static content)")
    public ResponseEntity<String> evictCache() {
        investorContentService.evictInvestorContentCache();
        return ResponseEntity.ok("{\"message\": \"Investor content cache evicted\"}");
    }

    // ---- Organogram ----
    @GetMapping("/organogram")
    @Operation(summary = "Get organogram", description = "Returns the full ENSA organisational hierarchy loaded from organograma.json. Edit that file to update the structure without DB changes.")
    public ResponseEntity<OrganigramDTO> getOrganogram() {
        return ResponseEntity.ok(investorContentService.getOrganogram());
    }

    @PutMapping("/organogram")
    @Operation(summary = "Update organogram", description = "Saves the full organogram hierarchy back to organograma.json and evicts cache.")
    public ResponseEntity<OrganigramDTO> updateOrganogram(@RequestBody OrganigramDTO dto) {
        return ResponseEntity.ok(investorContentService.saveOrganogram(dto));
    }

    // ---- Estatutos Sociais ----
    @GetMapping("/estatutos")
    @Operation(summary = "Get Estatutos Sociais", description = "Returns editable statute sections for route /ensa/estatuto.")
    public ResponseEntity<EstatutosDTO> getEstatutos() {
        return ResponseEntity.ok(investorContentService.getEstatutos());
    }

    @PutMapping("/estatutos")
    @Operation(summary = "Update Estatutos Sociais", description = "Saves statute sections to estatutos.json.")
    public ResponseEntity<EstatutosDTO> updateEstatutos(@RequestBody EstatutosDTO dto) {
        return ResponseEntity.ok(investorContentService.saveEstatutos(dto));
    }

    // ---- Annual Report Destaque ----
    @GetMapping("/annual-report-destaque")
    @Operation(summary = "Get Annual Report Destaque", description = "Returns the featured report banner shown at the top of /ensa/relatorio-contas.")
    public ResponseEntity<AnnualReportDestaqueDTO> getAnnualReportDestaque() {
        return ResponseEntity.ok(investorContentService.getAnnualReportDestaque());
    }

    @PutMapping("/annual-report-destaque")
    @Operation(summary = "Update Annual Report Destaque", description = "Saves the featured report banner content to annual_report_destaque.json.")
    public ResponseEntity<AnnualReportDestaqueDTO> updateAnnualReportDestaque(@RequestBody AnnualReportDestaqueDTO dto) {
        return ResponseEntity.ok(investorContentService.saveAnnualReportDestaque(dto));
    }

    // ---- Investor FAQ ----
    @GetMapping("/investor-faq")
    @Operation(summary = "Get Investor FAQ", description = "Returns the FAQ items for the investor support page from investor_faq.json.")
    public ResponseEntity<InvestorFaqDTO> getInvestorFaq() {
        return ResponseEntity.ok(investorContentService.getInvestorFaq());
    }

    @PutMapping("/investor-faq")
    @Operation(summary = "Update Investor FAQ", description = "Saves FAQ items to investor_faq.json and evicts cache.")
    public ResponseEntity<InvestorFaqDTO> updateInvestorFaq(@RequestBody InvestorFaqDTO dto) {
        return ResponseEntity.ok(investorContentService.saveInvestorFaq(dto));
    }

    // ---- Órgãos Sociais Members ----
    @GetMapping("/organ-members")
    @Operation(summary = "Get Órgãos Sociais members", description = "Returns members for each social organ loaded from organ_members.json.")
    public ResponseEntity<OrganMembersDTO> getOrganMembers() {
        return ResponseEntity.ok(investorContentService.getOrganMembers());
    }

    @PutMapping("/organ-members")
    @Operation(summary = "Update Órgãos Sociais members", description = "Saves organ member lists back to organ_members.json and evicts cache.")
    public ResponseEntity<OrganMembersDTO> updateOrganMembers(@RequestBody OrganMembersDTO dto) {
        return ResponseEntity.ok(investorContentService.saveOrganMembers(dto));
    }

    // ---- Carousel Slides ----
    @GetMapping("/carousel-slides")
    @Operation(summary = "Get carousel slides", description = "Returns the main page hero carousel slides from carousel_slides.json.")
    public ResponseEntity<CarouselSlidesDTO> getCarouselSlides() {
        return ResponseEntity.ok(investorContentService.getCarouselSlides());
    }

    @PutMapping("/carousel-slides")
    @Operation(summary = "Update carousel slides", description = "Saves carousel slides to carousel_slides.json and evicts cache.")
    public ResponseEntity<CarouselSlidesDTO> updateCarouselSlides(@RequestBody CarouselSlidesDTO dto) {
        return ResponseEntity.ok(investorContentService.saveCarouselSlides(dto));
    }

    @PostMapping(value = "/carousel-slides/upload/{slideId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload carousel slide image", description = "Uploads an image for the given slide ID, returns the hosted URL.")
    public ResponseEntity<java.util.Map<String, String>> uploadCarouselSlideImage(
            @PathVariable String slideId,
            @RequestParam("file") MultipartFile file) {
        String url = investorContentService.uploadCarouselSlideImage(slideId, file);
        return ResponseEntity.ok(java.util.Map.of("url", url));
    }

    // ---- Plano Estratégico ----
    @GetMapping("/plano-estrategico")
    @Operation(summary = "Get Plano Estratégico content", description = "Returns editable content for route /ensa/plano-estrategico.")
    public ResponseEntity<PlanoEstrategicoDTO> getPlanoEstrategico() {
        return ResponseEntity.ok(investorContentService.getPlanoEstrategico());
    }

    @PutMapping("/plano-estrategico")
    @Operation(summary = "Update Plano Estratégico content", description = "Saves Plano Estratégico content and evicts cache.")
    public ResponseEntity<PlanoEstrategicoDTO> updatePlanoEstrategico(@RequestBody PlanoEstrategicoDTO dto) {
        return ResponseEntity.ok(investorContentService.savePlanoEstrategico(dto));
    }

    // ---- Who We Are (Quem Somos) ----
    @GetMapping("/about/who-we-are")
    @Operation(summary = "Get Quem Somos content", description = "Returns editable main content for route /ensa/quem-somos.")
    public ResponseEntity<WhoWeAreContentDTO> getWhoWeAreContent() {
        return ResponseEntity.ok(investorContentService.getWhoWeAreContent());
    }

    @PutMapping("/about/who-we-are")
    @Operation(summary = "Update Quem Somos content", description = "Updates editable main content for route /ensa/quem-somos.")
    public ResponseEntity<WhoWeAreContentDTO> updateWhoWeAreContent(@RequestBody WhoWeAreContentDTO dto) {
        return ResponseEntity.ok(investorContentService.saveWhoWeAreContent(dto));
    }

    // ---- Financial Indicators Page ----
    @GetMapping("/financial-indicators-page")
    @Operation(summary = "Get financial indicators page content", description = "Returns editable page content for route /ensa/indicadores-financeiros.")
    public ResponseEntity<FinancialIndicatorsPageDTO> getFinancialIndicatorsPage() {
        return ResponseEntity.ok(investorContentService.getFinancialIndicatorsPage());
    }

    @PutMapping("/financial-indicators-page")
    @Operation(summary = "Update financial indicators page content", description = "Updates editable page content for route /ensa/indicadores-financeiros.")
    public ResponseEntity<FinancialIndicatorsPageDTO> updateFinancialIndicatorsPage(@RequestBody FinancialIndicatorsPageDTO dto) {
        return ResponseEntity.ok(investorContentService.saveFinancialIndicatorsPage(dto));
    }

    // ---- Participadas (JSON file-based) ----
    @GetMapping("/participadas")
    @Operation(summary = "Get Participadas content", description = "Returns editable page content for route /ensa/participadas.")
    public ResponseEntity<ParticipadasDataDTO> getParticipadas() {
        return ResponseEntity.ok(investorContentService.getParticipadas());
    }

    @PutMapping("/participadas")
    @Operation(summary = "Update Participadas content", description = "Updates editable page content for route /ensa/participadas.")
    public ResponseEntity<ParticipadasDataDTO> updateParticipadas(@RequestBody ParticipadasDataDTO dto) {
        return ResponseEntity.ok(investorContentService.saveParticipadas(dto));
    }

    // ---- Políticas / Legislação (JSON file-based) ----
    @GetMapping("/politicas-page")
    @Operation(summary = "Get Políticas page content", description = "Returns editable page content for route /politicas.")
    public ResponseEntity<PolicyLegislationPageDTO> getPoliticasPage() {
        return ResponseEntity.ok(investorContentService.getPoliticasPage());
    }

    @PutMapping("/politicas-page")
    @Operation(summary = "Update Políticas page content", description = "Updates editable page content for route /politicas.")
    public ResponseEntity<PolicyLegislationPageDTO> updatePoliticasPage(@RequestBody PolicyLegislationPageDTO dto) {
        return ResponseEntity.ok(investorContentService.savePoliticasPage(dto));
    }

    @GetMapping("/legislacao-page")
    @Operation(summary = "Get Legislação page content", description = "Returns editable page content for route /legislacao.")
    public ResponseEntity<PolicyLegislationPageDTO> getLegislacaoPage() {
        return ResponseEntity.ok(investorContentService.getLegislacaoPage());
    }

    @PutMapping("/legislacao-page")
    @Operation(summary = "Update Legislação page content", description = "Updates editable page content for route /legislacao.")
    public ResponseEntity<PolicyLegislationPageDTO> updateLegislacaoPage(@RequestBody PolicyLegislationPageDTO dto) {
        return ResponseEntity.ok(investorContentService.saveLegislacaoPage(dto));
    }

    // ---- Calendário de Divulgações (JSON file-based) ----
    @GetMapping("/calendario-divulgacoes")
    @Operation(summary = "Get disclosure calendar", description = "Returns all card groups and their items for /ensa/calendario-divulgacoes.")
    public ResponseEntity<CalendarioDivulgacoesDTO> getCalendarioDivulgacoes() {
        return ResponseEntity.ok(investorContentService.getCalendarioDivulgacoes());
    }

    @PutMapping("/calendario-divulgacoes")
    @Operation(summary = "Update disclosure calendar", description = "Saves all card groups and items for /ensa/calendario-divulgacoes.")
    public ResponseEntity<CalendarioDivulgacoesDTO> updateCalendarioDivulgacoes(@RequestBody CalendarioDivulgacoesDTO dto) {
        return ResponseEntity.ok(investorContentService.saveCalendarioDivulgacoes(dto));
    }

    // ---- General Assembly CRUD ----

    @PostMapping("/general-assemblies")
    @Operation(summary = "Create General Assembly")
    public ResponseEntity<GeneralAssemblyDTO> createGeneralAssembly(@RequestBody GeneralAssemblyDTO dto) {
        return ResponseEntity.status(201).body(investorContentService.createGeneralAssembly(dto));
    }

    @PutMapping("/general-assemblies/{id}")
    @Operation(summary = "Update General Assembly")
    public ResponseEntity<GeneralAssemblyDTO> updateGeneralAssembly(@PathVariable Long id, @RequestBody GeneralAssemblyDTO dto) {
        return ResponseEntity.ok(investorContentService.updateGeneralAssembly(id, dto));
    }

    @DeleteMapping("/general-assemblies/{id}")
    @Operation(summary = "Delete General Assembly (soft delete)")
    public ResponseEntity<Void> deleteGeneralAssembly(@PathVariable Long id) {
        investorContentService.deleteGeneralAssembly(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Assembly document management ----

    @PostMapping("/general-assemblies/{assemblyId}/documents")
    @Operation(summary = "Add document to assembly (manual URL)")
    public ResponseEntity<GeneralAssemblyDocumentDTO> addAssemblyDocument(
            @PathVariable Long assemblyId, @RequestBody GeneralAssemblyDocumentDTO dto) {
        return ResponseEntity.status(201).body(investorContentService.addAssemblyDocument(assemblyId, dto));
    }

    @DeleteMapping("/general-assemblies/documents/{docId}")
    @Operation(summary = "Delete assembly document")
    public ResponseEntity<Void> deleteAssemblyDocument(@PathVariable Long docId) {
        investorContentService.deleteAssemblyDocument(docId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/general-assemblies/{assemblyId}/documents/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload file for assembly document")
    public ResponseEntity<java.util.Map<String, String>> uploadAssemblyDocument(
            @PathVariable Long assemblyId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "documentType", defaultValue = "PDF") String documentType) {
        String url = investorContentService.uploadAssemblyFile(file, assemblyId);
        long bytes = file.getSize();
        String sizeLabel = bytes < 1024 * 1024
            ? String.format("%.0f KB", bytes / 1024.0)
            : String.format("%.1f MB", bytes / (1024.0 * 1024));
        GeneralAssemblyDocumentDTO doc = new GeneralAssemblyDocumentDTO();
        doc.setTitle(title);
        doc.setDocumentUrl(url);
        doc.setDocumentType(documentType);
        doc.setFileSizeLabel(sizeLabel);
        GeneralAssemblyDocumentDTO saved = investorContentService.addAssemblyDocument(assemblyId, doc);
        return ResponseEntity.status(201).body(java.util.Map.of(
            "id", String.valueOf(saved.getId()),
            "url", url,
            "title", title,
            "fileSizeLabel", sizeLabel
        ));
    }
}
