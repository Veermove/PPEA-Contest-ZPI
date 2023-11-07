package zpi.ppea.clap.mappers;

import data_store.PartialRating;
import data_store.PartialRatingRequest;
import zpi.ppea.clap.dtos.PartialRatingDto;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;

public class PartialRatingMapper {
    private PartialRatingMapper() {
    }

    public static PartialRatingDto partialRatingToDto(PartialRating partialRating) {
        return PartialRatingDto.builder()
                .partialRatingId(partialRating.getPartialRatingId())
                .criterionId(partialRating.getCriterionId())
                .points(partialRating.getPoints())
                .justification(partialRating.getJustification())
                .modified(partialRating.getModified())
                .modifiedBy(partialRating.getModifiedBy())
                .build();
    }

    public static PartialRating dtoToPartialRating(PartialRatingDto dto) {
        return PartialRating.newBuilder()
                .setPartialRatingId(dto.getPartialRatingId())
                .setCriterionId(dto.getCriterionId())
                .setPoints(dto.getPoints())
                .setJustification(dto.getJustification())
                .setModified(dto.getModified())
                .setModifiedBy(dto.getModifiedBy())
                .build();
    }

    public static PartialRatingRequest dtoToPartialRatingRequest(UpdatePartialRatingDto dto) {
        return PartialRatingRequest.newBuilder()
                .setPartialRatingId(dto.getPartialRatingId())
                .setCriterionId(dto.getCriterionId())
                .setPoints(dto.getPoints())
                .setJustification(dto.getJustification())
                .build();
    }

}
