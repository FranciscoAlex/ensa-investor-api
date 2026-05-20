package ao.co.ensa.investor.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganMembersDTO {
    private String updatedAt;
    private List<OrganDTO> organs;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrganDTO {
        private String id;
        private String title;
        private String description;
        private String color;
        private String textColor;
        private String transparencyTitle;
        private String transparencyText;
        // Transparency stats
        private String stat1Value;
        private String stat1Label;
        private String stat2Value;
        private String stat2Label;
        // Transparency cards
        private String card1Title;
        private String card1Text;
        private String card2Title;
        private String card2Text;
        private Boolean competenciasVisible;
        private String competenciasTitle;
        private String competenciasText;
        private List<OrganMemberDTO> members;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.ALWAYS)
    public static class OrganMemberDTO {
        private String name;
        private String role;
        private String photoUrl;
        private String bio;
        private Boolean showBio;
        private java.util.List<String> otherTitles;
        private String hyperlink;
        private Boolean active;
    }
}
