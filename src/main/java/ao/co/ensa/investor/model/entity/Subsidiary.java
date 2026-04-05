package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subsidiaries", indexes = @Index(name = "idx_subs_country", columnList = "country"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subsidiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_name", nullable = false, length = 500)
    private String entityName;

    @Column(columnDefinition = "CLOB")
    private String description;

    @Column(name = "participation_percentage", precision = 5, scale = 2)
    private BigDecimal participationPercentage;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "website_url", length = 1000)
    private String websiteUrl;

    @Column(name = "is_active")
    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
