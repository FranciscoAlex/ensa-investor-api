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
public class PlanoEstrategicoDTO {

    private String updatedAt;
    private String pdfUrl;
    private String pdfLabel;
    private int pdfTotalPages;
    private List<FoundationItem> foundation;
    private List<PillarItem> pillars;
    private String commitmentTitle;
    private String commitmentText;
    private List<StatItem> commitmentStats;
    private List<ModernizationItem> modernizationItems;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FoundationItem {
        private String id;
        private String title;
        private String content;
        private String color;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PillarItem {
        private String id;
        private String title;
        private String description;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatItem {
        private String id;
        private String value;
        private String label;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModernizationItem {
        private String id;
        private String title;
        private String description;
        private String accentColor;
    }
}
