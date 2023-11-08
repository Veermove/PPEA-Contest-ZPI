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
import zpi.ppea.clap.mappers.PartialRatingMapper;
import zpi.ppea.clap.mappers.RatingMapper;
import zpi.ppea.clap.repository.RatingsRepository;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingsRepository ratingsRepository;

    @Cacheable("submissionRatings")
    public RatingsSubmissionResponseDto getSubmissionRatings(Integer submissionId, FirebaseAgent.UserAuthData authentication) {
        var ratings = ratingsRepository.submissionRatings(submissionId, authentication);
        return RatingsSubmissionResponseDto.builder()
                .criteria(DtoMapper.INSTANCE.criterionListToDto(
                        Optional.of(ratings.getCriteriaList()).orElse(List.of())
                ))
                .individualRatings(DtoMapper.INSTANCE.assessorRatingsListToDtos(
                        Optional.of(ratings.getIndividualList()).orElse(List.of())
                ))
                .initialRating(DtoMapper.INSTANCE.assessorRatingsToDtos(ratings.getInitial()))
                .finalRating(DtoMapper.INSTANCE.assessorRatingsToDtos(ratings.getInitial()))
                .build();
    }

    public RatingDto createNewRating(Integer submissionId, NewRatingDto newRatingDto, FirebaseAgent.UserAuthData authentication) {
        var rating = ratingsRepository.createNewRating(submissionId,
                authentication, RatingType.valueOf(newRatingDto.getRatingType().name()));
        return RatingMapper.ratingToDto(rating);
    }

    public PartialRatingDto submitRatingDraft(Integer ratingId, FirebaseAgent.UserAuthData authentication) {
        var updatedPartialRating = ratingsRepository.submitRatingDraft(ratingId, authentication);
        return PartialRatingMapper.partialRatingToDto(updatedPartialRating);
    }
}
