package ao.co.ensa.investor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CeoMessageDTO {
    private String executiveName;
    private String executiveTitle;
    private String executiveName2;
    private String executiveTitle2;
    private String quoteText;
    private String bodyText;
    private String photoUrl;
    private String photoUrl2;
    private String updatedAt;
}
