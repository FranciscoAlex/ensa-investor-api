package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "general_assembly_agenda_items",
    indexes = @Index(name = "idx_gai_assembly", columnList = "assembly_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralAssemblyAgendaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "general_assembly_agenda_seq")
    @SequenceGenerator(name = "general_assembly_agenda_seq", sequenceName = "GENERAL_ASSEMBLY_AGENDA_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assembly_id", nullable = false)
    private GeneralAssembly assembly;

    @Column(name = "item_text", nullable = false, columnDefinition = "CLOB")
    private String itemText;

    @Column(name = "display_order")
    @Builder.Default
    private Integer displayOrder = 0;
}
