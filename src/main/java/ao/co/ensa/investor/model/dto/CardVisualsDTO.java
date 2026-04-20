package ao.co.ensa.investor.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardVisualsDTO {

    private String updatedAt;
    private boolean showMarketTicker;
    private List<CardVisualDTO> cards;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CardVisualDTO {
        private String key;
        private String label;
        private String bgColor;
        private String backdropBlur;
        private String accentColor;
        private String iconBgColor;
        private String borderColor;
        private String valueTextColor;
        private String labelTextColor;
    }
}
