package zpi.ppea.clap.mappers;

import data_store.Assessor;
import data_store.Rating;
import data_store.Submission;
import zpi.ppea.clap.dtos.AssessorDto;
import zpi.ppea.clap.dtos.RatingDto;
import zpi.ppea.clap.dtos.RatingType;
import zpi.ppea.clap.dtos.SubmissionDto;

import java.util.ArrayList;
import java.util.List;

public class SubmissionMapper {

    private SubmissionMapper() {
    }

    public static List<SubmissionDto> submissionListToDtos(List<Submission> submissions) {
        List<SubmissionDto> submissionDtos = new ArrayList<>();
        for (var sub : submissions) {
            var submissionDto = SubmissionDto.builder()
                    .submissionId(sub.getSubmissionId())
                    .name(sub.getName())
                    .year(sub.getYear())
                    .durationDays(sub.getDurationDays())
                    .build();
            submissionDto.setAssessors(
                    mapAssessors(sub.getAssessorsList())
            );
            submissionDto.setRatings(
                    mapRatings(sub.getRatingsList())
            );
            submissionDtos.add(submissionDto);
        }
        return submissionDtos;
    }

    static List<AssessorDto> mapAssessors(List<Assessor> assessors) {
        return assessors.stream()
                .map(assessor -> AssessorDto.builder()
                        .assessorId(assessor.getAssessorId())
                        .firstName(assessor.getFirstName())
                        .lastName(assessor.getLastName()).
                        build())
                .toList();
    }

    static List<RatingDto> mapRatings(List<Rating> ratings) {
        return ratings.stream()
                .map(rating -> RatingDto.builder()
                        .ratingId(rating.getRatingId())
                        .isDraft(rating.getIsDraft())
                        .type(RatingType.valueOf(rating.getType().name()))
                        .build())
                .toList();
    }
}
