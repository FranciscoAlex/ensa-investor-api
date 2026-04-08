package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyLegislationPageDTO {
    private String route;
    private String title;
    private String introText;
    private String updatedAt;
    private List<InfoSectionDTO> sections;
    private List<DocumentLinkDTO> documents;
}
