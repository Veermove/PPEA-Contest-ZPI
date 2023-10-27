package zpi.ppea.clap.service;

import data_store.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.config.ValueConfig;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.RatingDto;
import zpi.ppea.clap.dtos.SubmissionDto;
import zpi.ppea.clap.exceptions.UserNotAuthorizedException;
import zpi.ppea.clap.mappers.DetailedSubmissionMapper;
import zpi.ppea.clap.mappers.SubmissionMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreBlockingStub dataStoreBlockingStub;

    private final ValueConfig valueConfig;

    public List<SubmissionDto> getSubmissions() {
        UserClaims userClaims = dataStoreBlockingStub.getUserClaims(UserRequest.newBuilder()
                .setEmail(valueConfig.getFirebaseEmail()).build());

        if (userClaims == null || userClaims.getAssessorId() == 0) {
            throw new UserNotAuthorizedException("User not authorized");
        }

        SubmissionsResponse allSubmissionsGrpc = dataStoreBlockingStub.getSubmissions(
                SubmissionRequest.newBuilder().setAssessorId(userClaims.getAssessorId()).build()
        );

        List<SubmissionDto> submissionDtos = SubmissionMapper.submissionListToDtos(allSubmissionsGrpc.getSubmissionsList());

        // count total rating
        submissionDtos.forEach(
                submissionDto -> submissionDto.setTotalRating(
                        countTotalRating(submissionDto.getSubmissionId(), submissionDto.getRatings().stream().map(RatingDto::getRatingId).toList())
                )
        );

        return submissionDtos;
    }

    public DetailsSubmissionResponseDto getDetailedSubmission(Integer submissionId) {
        DetailsSubmissionResponse detailsSubmissionResponse = dataStoreBlockingStub.getSubmissionDetails(
                DetailsSubmissionRequest.newBuilder().setSubmissionId(submissionId).build()
        );

        return DetailedSubmissionMapper.mapToDto(detailsSubmissionResponse);
    }

    public Integer countTotalRating(Integer submissionId, List<Integer> ratingIds) {
        return ratingIds.stream()
                .map(ratingId -> getRatingPoints(submissionId, ratingId))
                .reduce(0, Integer::sum);
    }

    public Integer getRatingPoints(Integer submissionId, Integer ratingId) {
        var ratings = dataStoreBlockingStub.getSubmissionRatings(RatingsSubmissionRequest.newBuilder()
                .setSubmissionId(submissionId).build());
        var individualSum = 0;
        for (var individual : ratings.getIndividualList()) {
            individualSum += individual.getPartialRatingsList().stream()
                    .filter(r -> r.getPartialRatingId() == ratingId)
                    .map(PartialRating::getPoints).toList()
                    .stream().reduce(0, Integer::sum);
        }
        Integer initial = ratings.getInitial().getPartialRatingsList().stream()
                .filter(r -> r.getPartialRatingId() == ratingId)
                .map(PartialRating::getPoints).toList()
                .stream().reduce(0, Integer::sum);
        Integer finalRate = ratings.getFinal().getPartialRatingsList().stream()
                .filter(r -> r.getPartialRatingId() == ratingId)
                .map(PartialRating::getPoints).toList()
                .stream().reduce(0, Integer::sum);
        return individualSum + initial + finalRate;
    }

}
