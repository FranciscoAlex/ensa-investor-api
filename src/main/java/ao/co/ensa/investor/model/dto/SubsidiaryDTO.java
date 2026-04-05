package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubsidiaryDTO {
    private Long id;
    private String entityName;
    private String description;
    private BigDecimal participationPercentage;
    private String country;
    private String websiteUrl;
    private LocalDateTime createdAt;
}
