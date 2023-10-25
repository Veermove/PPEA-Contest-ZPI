package zpi.ppea.clap.mapstruct.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    private int ratingId;
    private boolean isDraft;
    private int assessorId;
    private RatingType type;
}
