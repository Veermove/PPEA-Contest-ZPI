package zpi.ppea.clap.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessorRatingsDto {
    private int ratingId;
    private int assessorId;
    private String firstName;
    private String lastName;
    private List<PartialRatingDto> partialRatings;
    private boolean isDraft;
}
