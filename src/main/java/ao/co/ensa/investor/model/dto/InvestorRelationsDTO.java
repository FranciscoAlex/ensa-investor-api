package ao.co.ensa.investor.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestorRelationsDTO {
    private Long id;
    private String email;
    private String phone;
    private String address;
    private String otherContacts;
    private LocalDateTime updatedAt;
}
