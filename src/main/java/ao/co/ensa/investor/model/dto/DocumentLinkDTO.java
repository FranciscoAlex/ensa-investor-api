package ao.co.ensa.investor.model.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentLinkDTO {
    private String id;
    private String title;
    private String url;
    private String fileType;
    private String fileSizeLabel;
}
