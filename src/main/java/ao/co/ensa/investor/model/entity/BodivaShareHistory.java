package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bodiva_share_history", indexes = @Index(name = "idx_bodiva_date", columnList = "record_date"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BodivaShareHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bodiva_share_history_seq")
    @SequenceGenerator(name = "bodiva_share_history_seq", sequenceName = "BODIVA_SHARE_HISTORY_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "share_price", precision = 18, scale = 4)
    private BigDecimal sharePrice;

    @Column(name = "volume")
    private Long volume;

    @Column(name = "opening_price", precision = 18, scale = 4)
    private BigDecimal openingPrice;

    @Column(name = "closing_price", precision = 18, scale = 4)
    private BigDecimal closingPrice;

    @Column(name = "high_price", precision = 18, scale = 4)
    private BigDecimal highPrice;

    @Column(name = "low_price", precision = 18, scale = 4)
    private BigDecimal lowPrice;

    @Column(name = "notes", length = 1000)
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
