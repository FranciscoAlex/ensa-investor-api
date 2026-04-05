package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "financial_statements", indexes = @Index(name = "idx_fs_year", columnList = "year"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialStatement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "financial_statement_seq")
    @SequenceGenerator(name = "financial_statement_seq", sequenceName = "FINANCIAL_STATEMENT_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(name = "document_url", nullable = false, length = 1000)
    private String documentUrl;

    @Column(name = "statement_type", length = 100)
    private String statementType;

    @Column(name = "language", length = 10)
    @Builder.Default
    private String language = "pt";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
