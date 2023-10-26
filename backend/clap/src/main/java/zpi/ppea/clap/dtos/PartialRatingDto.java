package zpi.ppea.clap.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartialRatingDto {
    private Integer partialRatingId;
    private Integer criterionId;
    private Integer points;
    private String justification;
    private String modified;
    private Integer modifiedBy;
}
