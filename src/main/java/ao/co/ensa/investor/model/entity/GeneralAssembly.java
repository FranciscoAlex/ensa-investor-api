package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "general_assemblies", indexes = {
    @Index(name = "idx_ga_year",  columnList = "meeting_year"),
    @Index(name = "idx_ga_type",  columnList = "assembly_type"),
    @Index(name = "idx_ga_order", columnList = "display_order")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralAssembly {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "general_assembly_seq")
    @SequenceGenerator(name = "general_assembly_seq", sequenceName = "GENERAL_ASSEMBLY_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "slug_id", nullable = false, length = 100, unique = true)
    private String slugId;

    @Column(nullable = false, length = 500)
    private String title;

    @Column(name = "meeting_year", nullable = false)
    private Integer meetingYear;

    /** Human-readable date label, e.g. "10 de Abril de 2025" */
    @Column(name = "meeting_date", length = 100)
    private String meetingDate;

    /** Realizada | Convocada */
    @Column(length = 50)
    @Builder.Default
    private String status = "Realizada";

    /** Ordinária | Extraordinária | Eleitoral */
    @Column(name = "assembly_type", length = 50)
    @Builder.Default
    private String assemblyType = "Ordinária";

    @Column(columnDefinition = "CLOB")
    private String summary;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "is_active")
    @Builder.Default
    private boolean active = true;

    @OneToMany(mappedBy = "assembly", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<GeneralAssemblyAgendaItem> agendaItems = new ArrayList<>();

    @OneToMany(mappedBy = "assembly", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<GeneralAssemblyDocument> documents = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
