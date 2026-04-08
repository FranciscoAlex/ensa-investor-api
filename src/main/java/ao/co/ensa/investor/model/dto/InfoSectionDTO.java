package ao.co.ensa.investor.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoSectionDTO {
    private String id;
    private String title;
    private String content;
}
