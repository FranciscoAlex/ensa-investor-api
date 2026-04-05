package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Generic static data definition table.
 * Stores all reference/lookup data that the frontend needs for dropdowns,
 * labels, categories, provinces, document types, currencies, etc.
 *
 * Supports hierarchical data via self-referencing parent_id.
 * Extra attributes stored as JSON in the metadata CLOB column.
 */
@Entity
@Table(name = "static_data_definitions",
    uniqueConstraints = @UniqueConstraint(columnNames = {"category", "code"}),
    indexes = {
        @Index(name = "idx_static_category", columnList = "category"),
        @Index(name = "idx_static_active", columnList = "is_active"),
        @Index(name = "idx_static_parent", columnList = "parent_id")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StaticDataDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(name = "label_pt", nullable = false, length = 255)
    private String labelPt;  // Portuguese label (primary language in Angola)

    @Column(name = "label_en", length = 255)
    private String labelEn;  // English label (optional)

    @Column(length = 500)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private StaticDataDefinition parent;

    @Column(name = "sort_order")
    @Builder.Default
    private Integer sortOrder = 0;

    @Column(name = "is_active")
    @Builder.Default
    private boolean active = true;

    @Column(columnDefinition = "CLOB")
    private String metadata;  // JSON for extra attributes

    @Column(length = 500)
    private String description;

    @Column(length = 255)
    private String icon;  // Optional icon identifier for frontend

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
