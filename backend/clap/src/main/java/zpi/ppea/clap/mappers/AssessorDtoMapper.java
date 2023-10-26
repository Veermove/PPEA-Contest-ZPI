package zpi.ppea.clap.mappers;

import data_store.Assessor;
import org.mapstruct.Mapper;
import zpi.ppea.clap.dtos.AssessorDto;

@Mapper(componentModel = "spring")
public interface AssessorDtoMapper {
    AssessorDto mapToDto(Assessor assessor);
}
