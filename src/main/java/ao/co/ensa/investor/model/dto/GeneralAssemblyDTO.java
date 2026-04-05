package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralAssemblyDTO {
    private Long id;
    private String slugId;
    private String title;
    private Integer meetingYear;
    private String meetingDate;
    private String status;
    private String assemblyType;
    private String summary;
    private Integer displayOrder;
    private List<String> agendaItems;
    private List<GeneralAssemblyDocumentDTO> documents;
}
