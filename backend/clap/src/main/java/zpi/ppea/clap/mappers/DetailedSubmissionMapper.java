package zpi.ppea.clap.mappers;

import data_store.DetailsSubmissionResponse;
import zpi.ppea.clap.dtos.AppReportDto;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.ProjectState;

public class DetailedSubmissionMapper {
    private DetailedSubmissionMapper() {
    }

    public static DetailsSubmissionResponseDto mapToDto(DetailsSubmissionResponse response) {

        AppReportDto appReportDto = AppReportDto.builder()
                .isDraft(response.getReport().getIsDraft())
                .reportSubmissionDate(response.getReport().getReportSubmissionDate())
                .projectGoals(response.getReport().getProjectGoals())
                .organisationStructure(response.getReport().getOrganisationStructure())
                .divisionOfWork(response.getReport().getDivisionOfWork())
                .projectSchedule(response.getReport().getProjectSchedule())
                .attachments(response.getReport().getAttachments())
                .build();

        return DetailsSubmissionResponseDto.builder()
                .teamSize(response.getTeamSize())
                .finishDate(response.getFinishDate())
                .status(ProjectState.valueOf(response.getStatus().name()))
                .budget(response.getBudget())
                .description(response.getDescription())
                .appReportDto(appReportDto)
                .build();
    }
}