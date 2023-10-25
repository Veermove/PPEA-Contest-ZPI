package zpi.ppea.clap.mapstruct.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartialRatingDto {
    private int criterionId;
    private int points;
    private String justification;
    private String modified;
    private String modifiedBy;
}
