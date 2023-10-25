package zpi.ppea.clap.mapstruct.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionDTO {
    private Integer id;
    private String name;
    private Integer durationDays;
    private Integer year;
    private String ratingType;
    private Integer ratingPoints;
}
