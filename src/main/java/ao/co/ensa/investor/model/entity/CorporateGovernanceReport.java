package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "corporate_governance_reports", indexes = @Index(name = "idx_cgr_year", columnList = "report_year"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorporateGovernanceReport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "corp_governance_report_seq")
    @SequenceGenerator(name = "corp_governance_report_seq", sequenceName = "CORP_GOVERNANCE_REPORT_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(name = "document_url", nullable = false, length = 1000)
    private String documentUrl;

    @Column(name = "report_year")
    private Integer reportYear;

    @Column(name = "language", length = 10)
    @Builder.Default
    private String language = "pt";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
