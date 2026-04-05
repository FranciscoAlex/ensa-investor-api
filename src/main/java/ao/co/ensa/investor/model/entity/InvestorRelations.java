package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "investor_relations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvestorRelations {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "investor_relations_seq")
    @SequenceGenerator(name = "investor_relations_seq", sequenceName = "INVESTOR_RELATIONS_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 100)
    private String phone;

    @Column(columnDefinition = "CLOB")
    private String address;

    @Column(name = "other_contacts", columnDefinition = "CLOB")
    private String otherContacts;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
