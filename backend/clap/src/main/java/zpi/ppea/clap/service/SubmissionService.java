package zpi.ppea.clap.service;

import data_store.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.dtos.AssessorDto;
import zpi.ppea.clap.dtos.RatingDto;
import zpi.ppea.clap.dtos.SubmissionDto;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreBlockingStub dataStoreBlockingStub;

    public List<SubmissionDto> getSubmissions(Integer assesorId) {
        SubmissionsResponse allSubmissionsGrpc = dataStoreBlockingStub.getSubmissions(
                SubmissionRequest.newBuilder().setAssessorId(assesorId).build()
        );
        List<SubmissionDto> submissionDtos = new ArrayList<>();
        for (var sub : allSubmissionsGrpc.getSubmissionsList()) {
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

    List<AssessorDto> mapAssessors(List<Assessor> assessors) {
        return assessors.stream()
                .map(assessor -> AssessorDto.builder().assessorId(assessor.getAssessorId()).firstName(assessor.getFirstName()).lastName(assessor.getLastName()).build())
                .toList();
    }

    List<RatingDto> mapRatings(List<Rating> ratings) {
        return ratings.stream()
                .map(rating -> RatingDto.builder().ratingId(rating.getRatingId()).isDraft(rating.getIsDraft()).build())
                .toList();
    }

}
