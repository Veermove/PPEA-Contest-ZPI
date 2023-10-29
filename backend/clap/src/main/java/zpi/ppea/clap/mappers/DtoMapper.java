package zpi.ppea.clap.mappers;

import java.util.List;

import org.mapstruct.factory.Mappers;

import data_store.AssessorRatings;
import data_store.Criterion;
import zpi.ppea.clap.dtos.AssessorRatingsDto;
import zpi.ppea.clap.dtos.CriterionDto;

public interface DtoMapper {

    DtoMapper INSTANCE = Mappers.getMapper( DtoMapper.class );

    CriterionDto criterionToDto(Criterion rating);

    List<AssessorRatingsDto> assessorRatingsListToDtos(List<AssessorRatings> assessorRatings);

    AssessorRatingsDto assessorRatingsToDtos(AssessorRatings assessorRatings);

}
