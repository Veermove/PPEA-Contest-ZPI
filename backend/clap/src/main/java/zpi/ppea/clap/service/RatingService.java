package zpi.ppea.clap.service;

import data_store.RatingType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.dtos.NewRatingDto;
import zpi.ppea.clap.dtos.PartialRatingDto;
import zpi.ppea.clap.dtos.RatingDto;
import zpi.ppea.clap.dtos.RatingsSubmissionResponseDto;
import zpi.ppea.clap.mappers.DtoMapper;
import zpi.ppea.clap.repository.RatingsRepository;
import zpi.ppea.clap.security.FirebaseAgent;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingsRepository ratingsRepository;

    @Cacheable("SubmissionRatings")
    public RatingsSubmissionResponseDto getSubmissionRatings(Integer submissionId, FirebaseAgent.UserAuthData authentication) {
        var ratings = ratingsRepository.submissionRatings(submissionId, authentication);
        return DtoMapper.INSTANCE.ratingsResponseToDto(ratings);
    }

    public RatingDto createNewRating(Integer submissionId, NewRatingDto newRatingDto, FirebaseAgent.UserAuthData authentication) {
        var rating = ratingsRepository.createNewRating(submissionId,
                authentication, RatingType.valueOf(newRatingDto.getRatingType().name()));
        return DtoMapper.INSTANCE.ratingToDto(rating);
    }

    public PartialRatingDto submitRatingDraft(Integer ratingId, FirebaseAgent.UserAuthData authentication) {
        var updatedPartialRating = ratingsRepository.submitRatingDraft(ratingId, authentication);
        return DtoMapper.INSTANCE.partialRatingToDtos(updatedPartialRating);
    }

}
