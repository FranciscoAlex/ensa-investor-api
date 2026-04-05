package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialStatementDTO {
    private Long id;
    private Integer year;
    private String title;
    private String documentUrl;
    private String statementType;
    private String language;
    private LocalDateTime createdAt;
}
