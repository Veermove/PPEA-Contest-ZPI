package zpi.ppea.clap.mapstruct.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessorRatingsDto {
    private int assessorId;
    private String firstName;
    private String lastName;
    private List<PartialRatingDto> partialRatings;
}
