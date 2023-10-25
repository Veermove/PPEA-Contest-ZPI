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
public class RatingsSubmissionResponse {
    private List<CriterionDto> criteria;
    private AssessorRatingsDto individual;
    private AssessorRatingsDto initial;
    private AssessorRatingsDto finalRating;
}
