package zpi.ppea.clap.service;

import data_store.RatingType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.dtos.NewRatingDto;
import zpi.ppea.clap.dtos.RatingDto;
import zpi.ppea.clap.dtos.RatingsSubmissionResponseDto;
import zpi.ppea.clap.mappers.DtoMapper;
import zpi.ppea.clap.repository.RatingsRepository;
import zpi.ppea.clap.security.FirebaseAgent;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingsRepository ratingsRepository;

    @Cacheable("SubmissionRatings")
    public RatingsSubmissionResponseDto getSubmissionRatings(Integer submissionId, FirebaseAgent.UserAuthData authentication) {
        log.info("Getting submission ratings for submission {}", submissionId);
        var ratings = ratingsRepository.submissionRatings(submissionId, authentication);
        return DtoMapper.INSTANCE.ratingsResponseToDto(ratings);
    }

    public RatingDto createNewRating(Integer submissionId, NewRatingDto newRatingDto, FirebaseAgent.UserAuthData authentication) {
        log.info("Creating new rating for submission {}", submissionId);
        var rating = ratingsRepository.createNewRating(submissionId,
                authentication, RatingType.valueOf(newRatingDto.getRatingType().name()));
        return DtoMapper.INSTANCE.ratingToDto(rating);
    }

    public RatingDto submitRatingDraft(Integer ratingId, FirebaseAgent.UserAuthData authentication) {
        log.info("Submitting rating draft for rating {}", ratingId);
        var updatedRating = ratingsRepository.submitRatingDraft(ratingId, authentication);
        return DtoMapper.INSTANCE.ratingToDto(updatedRating);
    }

}
