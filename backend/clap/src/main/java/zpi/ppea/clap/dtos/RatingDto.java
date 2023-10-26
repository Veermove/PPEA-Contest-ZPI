package zpi.ppea.clap.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    private Integer ratingId;
    private boolean isDraft;
    private Integer assessorId;
    private RatingType type;
    private Integer points;
}
