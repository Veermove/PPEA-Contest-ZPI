package zpi.ppea.clap.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppReportDto {
    private Boolean isDraft;
    private String reportSubmissionDate;
    private String projectGoals;
    private String organisationStructure;
    private String divisionOfWork;
    private String projectSchedule;
    private String attachments;
}
