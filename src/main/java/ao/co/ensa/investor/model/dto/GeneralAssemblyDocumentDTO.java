package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralAssemblyDocumentDTO {
    private Long id;
    private Integer assemblyYear;
    private String title;
    private String documentUrl;
    private LocalDate assemblyDate;
    private String documentType;
    private Long assemblyId;
    private String fileSizeLabel;
    private LocalDateTime createdAt;
}
