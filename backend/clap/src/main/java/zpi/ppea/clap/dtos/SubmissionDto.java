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
    private int submissionId;
    private int year;
    private String name;
    private int durationDays;
    private List<AssessorDto> assessors;
    private List<RatingDto> ratings;
}
