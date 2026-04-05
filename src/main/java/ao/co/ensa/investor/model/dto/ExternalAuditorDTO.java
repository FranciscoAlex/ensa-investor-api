package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalAuditorDTO {
    private Long id;
    private String auditorName;
    private String contactInfo;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private boolean current;
    private LocalDateTime updatedAt;
}
