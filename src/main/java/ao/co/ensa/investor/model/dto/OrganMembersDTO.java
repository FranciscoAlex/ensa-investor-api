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
