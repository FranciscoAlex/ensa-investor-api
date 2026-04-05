package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "shareholder_structure",
    indexes = @Index(name = "idx_sh_order", columnList = "display_order"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareholderStructure {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shareholder_structure_seq")
    @SequenceGenerator(name = "shareholder_structure_seq", sequenceName = "SHAREHOLDER_STRUCTURE_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "shareholder_name", nullable = false, length = 500)
    private String shareholderName;

    /** Formatted display string, e.g. "1.680.000" */
    @Column(name = "shares_label", length = 100)
    private String sharesLabel;

    @Column(nullable = false, precision = 6, scale = 4)
    private java.math.BigDecimal percentage;

    /** Hex colour for charts, e.g. "#164993" */
    @Column(name = "display_color", length = 20)
    private String displayColor;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "is_active")
    @Builder.Default
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
