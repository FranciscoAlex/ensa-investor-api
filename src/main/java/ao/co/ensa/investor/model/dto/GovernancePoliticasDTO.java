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
public class GovernancePoliticasDTO {

    private String updatedAt;
    private List<PolicyItemDTO> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PolicyItemDTO {
        private String id;
        private String question;
        private String answer;
        private Integer sortOrder;
        private String fileUrl;
        private String fileName;
    }
}
