package ao.co.ensa.investor.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EstatutosDTO {
    private String lastUpdateLabel;
    private String legalBase;
    private String updatedAt;
    private List<EstatutosSectionDTO> sections;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EstatutosSectionDTO {
        private String id;
        private String title;
        private String content;
        /** Downloadable files attached to this section */
        private List<SectionFileDTO> files;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectionFileDTO {
        private String label;
        private String url;
    }
}
