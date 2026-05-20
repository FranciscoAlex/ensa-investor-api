package ao.co.ensa.investor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestorDocumentDTO {
    private Long id;
    private String title;
    private String documentUrl;
    private Integer year;
    private String category;
    private LocalDateTime createdAt;
}
