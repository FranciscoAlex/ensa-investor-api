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
public class InvestorFaqDTO {
    private String updatedAt;
    private List<FaqItemDTO> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FaqItemDTO {
        private String id;
        private String question;
        private String answer;
        private Integer sortOrder;
    }
}
