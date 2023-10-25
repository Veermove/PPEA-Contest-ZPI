package zpi.ppea.clap.mapstruct.mappers;

import backend.clap.Submission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import zpi.ppea.clap.mapstruct.dtos.SubmissionDTO;

@Mapper(componentModel = "spring")
public interface SubmissionDtoMapper {
    @Mapping(source = "submissionId", target = "id")
    SubmissionDTO mapToDTO(Submission submission);
}
