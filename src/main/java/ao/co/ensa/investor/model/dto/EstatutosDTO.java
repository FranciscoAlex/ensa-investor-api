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
    private String documentUrl;
    private String documentLabel;
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
    }
}
