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
public class FinancialDashboardDTO {

    private String headerTitle;
    private String headerDescription;
    private String powerBiUrl;

    private List<KpiCardDTO> kpis;
    private List<ChartRowDTO> chartData;
    private List<SegmentDTO> segments;
    private String marketShareNote;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class KpiCardDTO {
        private String label;
        private String value;
        private String suffix;
        private String growth;
        private Boolean isNegativeGood;
        private String color;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ChartRowDTO {
        private String year;
        private Double premiums;
        private Double claims;
        private Double profit;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class SegmentDTO {
        private String name;
        private Double value;
        private String color;
    }
}
