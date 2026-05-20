package ao.co.ensa.investor.controller;

import ao.co.ensa.investor.model.dto.InvestorDocumentDTO;
import ao.co.ensa.investor.service.InvestorContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/investor-content/investor-documents")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class InvestorDocumentController {

    private final InvestorContentService service;

    @GetMapping
    public ResponseEntity<List<InvestorDocumentDTO>> getAll() {
        return ResponseEntity.ok(service.getInvestorDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestorDocumentDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getInvestorDocumentById(id));
    }

    @PostMapping
    public ResponseEntity<InvestorDocumentDTO> create(@RequestBody InvestorDocumentDTO dto) {
        return new ResponseEntity<>(service.createInvestorDocument(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvestorDocumentDTO> update(@PathVariable Long id, @RequestBody InvestorDocumentDTO dto) {
        return ResponseEntity.ok(service.updateInvestorDocument(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteInvestorDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/file")
    public ResponseEntity<InvestorDocumentDTO> uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        String url = service.uploadInvestorDocumentFile(file, id);
        InvestorDocumentDTO dto = new InvestorDocumentDTO();
        dto.setDocumentUrl(url);
        return ResponseEntity.ok(service.updateInvestorDocument(id, dto));
    }
}
