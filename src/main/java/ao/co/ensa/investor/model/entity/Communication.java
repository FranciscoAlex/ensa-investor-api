package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "communications", indexes = {
    @Index(name = "idx_comm_type", columnList = "communication_type"),
    @Index(name = "idx_comm_published", columnList = "published_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Communication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "communication_seq")
    @SequenceGenerator(name = "communication_seq", sequenceName = "COMMUNICATION_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(name = "communication_type", length = 100)
    private String communicationType;

    @Column(columnDefinition = "CLOB")
    private String summary;

    @Column(name = "document_url", length = 1000)
    private String documentUrl;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "slug_id", length = 100)
    private String slugId;

    @Column(length = 100)
    private String category;

    @Column(name = "content_html", columnDefinition = "CLOB")
    private String contentHtml;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(length = 255)
    private String author;

    @Column(name = "display_sections", length = 255)
    @Builder.Default
    private String displaySections = "HOME,COMUNICADOS";

    @Column(name = "is_active")
    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
