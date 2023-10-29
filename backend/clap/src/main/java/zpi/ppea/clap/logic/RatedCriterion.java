package zpi.ppea.clap.logic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatedCriterion {
    private Integer criterionId;
    private String name;
    private String description;
    private String area;
    private String criteria;
    private String subcriteria;
    private Integer partialRatingId;
    private Integer points;
    private String justification;
    private String modified;
    private Integer modifiedBy;
}
