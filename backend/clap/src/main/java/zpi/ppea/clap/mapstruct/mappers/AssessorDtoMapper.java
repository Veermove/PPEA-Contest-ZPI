package zpi.ppea.clap.mapstruct.mappers;

import data_store.Assessor;
import org.mapstruct.Mapper;
import zpi.ppea.clap.mapstruct.dtos.AssessorDto;

@Mapper(componentModel = "spring")
public interface AssessorDtoMapper {
    AssessorDto mapToDto(Assessor assessor);
}
