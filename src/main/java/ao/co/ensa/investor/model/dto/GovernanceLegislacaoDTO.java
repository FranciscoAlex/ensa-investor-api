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
public class GovernanceLegislacaoDTO {

    private String updatedAt;
    private List<DiplomaItemDTO> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DiplomaItemDTO {
        private String id;
        private String label;
        private Integer sortOrder;
        private String fileUrl;
        private String fileName;
    }
}
