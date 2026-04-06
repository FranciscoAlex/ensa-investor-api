package ao.co.ensa.investor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipadasDataDTO {
    private String updatedAt;
    private List<ParticipadaDTO> participadas;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParticipadaDTO {
        private String id;
        private String name;
        private Double percentage;
        private Integer displayOrder;
    }
}
