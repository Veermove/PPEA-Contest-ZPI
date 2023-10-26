package zpi.ppea.clap.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailsSubmissionResponse {
    private int teamSize;
    private String finishDate;
    private ProjectState status;
    private String budget;
    private String description;
}
