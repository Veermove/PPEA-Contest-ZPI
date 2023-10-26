package zpi.ppea.clap.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterionDto {
    private int criterionId;
    private String name;
    private String description;
    private String area;
    private String criteria;
    private String subcriteria;
}
