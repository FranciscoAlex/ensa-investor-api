package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodivaShareHistoryDTO {
    private Long id;
    private LocalDate recordDate;
    private BigDecimal sharePrice;
    private Long volume;
    private BigDecimal openingPrice;
    private BigDecimal closingPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private String notes;
    private LocalDateTime createdAt;
}
