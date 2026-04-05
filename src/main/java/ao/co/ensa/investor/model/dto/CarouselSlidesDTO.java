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
public class CarouselSlidesDTO {

    private String updatedAt;
    private List<CarouselSlideDTO> slides;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CarouselSlideDTO {
        private String id;
        private String title;
        private String subtitle;
        private String imageUrl;
        private Integer order;
        private Boolean active;
    }
}
