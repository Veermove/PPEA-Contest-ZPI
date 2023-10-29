package zpi.ppea.clap.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import data_store.AssessorRatings;
import data_store.Criterion;
import data_store.PartialRating;
import zpi.ppea.clap.dtos.AssessorRatingsDto;
import zpi.ppea.clap.dtos.CriterionDto;
import zpi.ppea.clap.dtos.PartialRatingDto;

@Mapper
public interface DtoMapper {

    DtoMapper INSTANCE = Mappers.getMapper( DtoMapper.class );

    @Mapping(target = "partialRatings", source = "partialRatingsList")
    AssessorRatingsDto assessorRatingsToDtos(AssessorRatings assessorRatings);
    List<AssessorRatingsDto> assessorRatingsListToDtos(List<AssessorRatings> assessorRatings);

    CriterionDto criterionToDto(Criterion rating);
    List<CriterionDto> criterionListToDto(List<Criterion> rating);

    PartialRatingDto partialRatingToDtos(PartialRating partialRatings);
    List<PartialRatingDto> partialRatingListToDtos(List<PartialRating> partialRatings);

}
