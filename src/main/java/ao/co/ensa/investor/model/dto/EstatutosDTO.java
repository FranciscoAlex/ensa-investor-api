package ao.co.ensa.investor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstatutosDTO {
    private String lastUpdateLabel;
    private String legalBase;
    private String pdfUrl;
    private String updatedAt;
    private List<EstatutosSectionDTO> sections;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EstatutosSectionDTO {
        private String id;
        private String title;
        private String content;
    }
}
