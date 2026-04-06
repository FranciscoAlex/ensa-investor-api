package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganigramDTO {

    private MetaDTO meta;
    private SettingsDTO settings;
    private List<TopNodeDTO> topNodes;
    private List<DirectorDTO> directors;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetaDTO {
        private String version;
        private String updatedAt;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SettingsDTO {
        private Boolean showHierarchy;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopNodeDTO {
        private String id;
        private String label;
        private String type;
        private Boolean isVisible;
        private String bgColor;
        private String textColor;
        private String parentId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DirectorDTO {
        private String id;
        private String topNodeId;
        private String personLabel;
        private String name;
        private String title;
        private String initials;
        private String photoUrl;
        private String bio;
        private Boolean isCEO;
        private Boolean isVisible;
        private String cardBgColor;
        private String cardTextColor;
        private Integer displayOrder;
        private List<DepartmentDTO> departments;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepartmentDTO {
        private String id;
        private String label;
        private List<String> titles;
        private String titlesBgColor;
        private String bgColor;
        private String textColor;
    }
}
