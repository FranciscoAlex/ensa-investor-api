package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "general_assembly_documents", indexes = @Index(name = "idx_gad_year", columnList = "assembly_year"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralAssemblyDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assembly_year", nullable = false)
    private Integer assemblyYear;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(name = "document_url", nullable = false, length = 1000)
    private String documentUrl;

    @Column(name = "assembly_date")
    private LocalDate assemblyDate;

    @Column(name = "document_type", length = 100)
    private String documentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assembly_id")
    private GeneralAssembly assembly;

    @Column(name = "file_size_label", length = 50)
    private String fileSizeLabel;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
