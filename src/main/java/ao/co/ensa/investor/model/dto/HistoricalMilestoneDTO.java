package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalMilestoneDTO {
    private Long id;
    private String title;
    private String description;
    private Integer milestoneYear;
    private Integer displayOrder;
    private String imageUrl;
    private String contentHtml;
    private String eventTitle;
    private LocalDateTime createdAt;
}
