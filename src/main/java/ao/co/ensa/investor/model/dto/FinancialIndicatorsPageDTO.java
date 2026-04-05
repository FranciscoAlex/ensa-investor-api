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
public class FinancialIndicatorsPageDTO {
    private String route;
    private String title;
    private Integer year;
    private String updatedAt;
    private List<MainIndicatorDTO> mainIndicators;
    private HighlightSectionDTO highlightSection;
    private ChartSectionDTO premiumsChart;
    private ChartSectionDTO claimsChart;
    private RiskSectionDTO riskSection;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MainIndicatorDTO {
        private String id;
        private String label;
        private String value;
        private String change;
        private Boolean isPositive;
        private String iconKey;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HighlightSectionDTO {
        private String eyebrow;
        private String title;
        private String description;
        private String annualGrowthValue;
        private String annualGrowthLabel;
        private String cornerLabel;
        private String cornerValue;
        private List<ValuePointDTO> chartData;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartSectionDTO {
        private String title;
        private String footnote;
        private String accentText;
        private List<ValuePointDTO> data;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskSectionDTO {
        private String title;
        private String panelTitle;
        private List<RiskPointDTO> data;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValuePointDTO {
        private String year;
        private Double value;
        private String growth;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RiskPointDTO {
        private String year;
        private Double rate;
        private String change;
    }
}
