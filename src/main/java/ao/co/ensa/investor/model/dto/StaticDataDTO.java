package ao.co.ensa.investor.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaticDataDTO implements Serializable {

    private Long id;
    private String category;
    private String code;
    private String labelPt;
    private String labelEn;
    private String value;
    private Long parentId;
    private Integer sortOrder;
    private boolean active;
    private String metadata;
    private String description;
    private String icon;
}
