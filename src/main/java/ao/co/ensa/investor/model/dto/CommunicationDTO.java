package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunicationDTO {
    private Long id;
    private String title;
    private String communicationType;
    private String summary;
    private String documentUrl;
    private LocalDateTime publishedAt;
    private String slugId;
    private String category;
    private String contentHtml;
    private String imageUrl;
    private String author;
    private String displaySections;
    private LocalDateTime createdAt;
}
