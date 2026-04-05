package ao.co.ensa.investor.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "content_blocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContentBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_key", unique = true, nullable = false, length = 100)
    private String blockKey;

    @Column(columnDefinition = "LONGTEXT", nullable = false)
    private String data;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
