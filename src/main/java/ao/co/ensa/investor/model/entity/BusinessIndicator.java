package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "business_indicators", indexes = {
    @Index(name = "idx_bi_period", columnList = "period_year, period_quarter"),
    @Index(name = "idx_bi_category", columnList = "category")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "business_indicator_seq")
    @SequenceGenerator(name = "business_indicator_seq", sequenceName = "BUSINESS_INDICATOR_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(name = "indicator_value", length = 500)
    private String indicatorValue;

    @Column(name = "numeric_value", precision = 18, scale = 4)
    private BigDecimal numericValue;

    @Column(name = "period_year")
    private Integer periodYear;

    @Column(name = "period_quarter")
    private Integer periodQuarter;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "unit", length = 50)
    private String unit;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
