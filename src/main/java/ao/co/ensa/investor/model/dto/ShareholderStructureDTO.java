package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShareholderStructureDTO {
    private Long id;
    private String shareholderName;
    private String sharesLabel;
    private BigDecimal percentage;
    private String displayColor;
    private Integer displayOrder;
}
