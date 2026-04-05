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
public class AnnualReportDestaqueDTO {
    private String updatedAt;
    private String badgeLabel;
    private String subtitle;
    private String headline;
    private String description;
    private String downloadUrl;
    private String downloadLabel;
    private List<StatCardDTO> statCards;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatCardDTO {
        private String id;
        private String label;
        private String value;
        private String trend;
        private Integer progressValue;
        private Boolean showProgress;
        private String note;
    }
}
