package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorporateGovernanceReportDTO {
    private Long id;
    private String title;
    private String documentUrl;
    private Integer reportYear;
    private String language;
    private LocalDateTime createdAt;
}
