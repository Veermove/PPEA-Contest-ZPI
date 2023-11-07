package zpi.ppea.clap.mappers;

import data_store.Rating;
import zpi.ppea.clap.dtos.RatingDto;
import zpi.ppea.clap.dtos.RatingType;

public class RatingMapper {
    private RatingMapper() {
    }

    public static RatingDto ratingToDto(Rating rating) {
        return RatingDto.builder()
                .ratingId(rating.getRatingId())
                .assessorId(rating.getAssessorId())
                .isDraft(rating.getIsDraft())
                .type(RatingType.valueOf(rating.getType().name()))
                .build();
    }

}
