package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "historical_milestones", indexes = {
    @Index(name = "idx_hm_year", columnList = "milestone_year"),
    @Index(name = "idx_hm_order", columnList = "display_order")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricalMilestone {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historical_milestone_seq")
    @SequenceGenerator(name = "historical_milestone_seq", sequenceName = "HISTORICAL_MILESTONE_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(columnDefinition = "CLOB")
    private String description;

    @Column(name = "milestone_year")
    private Integer milestoneYear;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(name = "content_html", columnDefinition = "CLOB")
    private String contentHtml;

    @Column(name = "event_title", length = 500)
    private String eventTitle;

    @Column(name = "is_active")
    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
