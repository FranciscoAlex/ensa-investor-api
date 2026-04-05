package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessIndicatorDTO {
    private Long id;
    private String title;
    private String indicatorValue;
    private BigDecimal numericValue;
    private Integer periodYear;
    private Integer periodQuarter;
    private String category;
    private String unit;
    private LocalDateTime createdAt;
}
