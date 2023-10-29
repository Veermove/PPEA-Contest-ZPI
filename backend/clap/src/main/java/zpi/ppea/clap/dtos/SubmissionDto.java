package zpi.ppea.clap.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionDto {
    private Integer submissionId;
    private Integer year;
    private String name;
    private Integer durationDays;
    private List<AssessorDto> assessors;
    private List<RatingDto> ratings;
}
