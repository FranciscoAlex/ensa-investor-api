package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardMemberDTO {
    private Long id;
    private String fullName;
    private String role;
    private String bio;
    private String cvDocumentUrl;
    private String photoUrl;
    private Integer displayOrder;
    private LocalDateTime createdAt;
}
