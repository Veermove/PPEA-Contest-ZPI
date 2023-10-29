package zpi.ppea.clap.service;

import data_store.DataStoreGrpc;
import data_store.DetailsSubmissionRequest;
import data_store.RatingsSubmissionRequest;
import data_store.RatingsSubmissionResponse;
import data_store.SubmissionRequest;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.RatingsSubmissionResponseDto;
import zpi.ppea.clap.dtos.SubmissionDto;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.logic.BusinessLogicService;
import zpi.ppea.clap.mappers.DetailedSubmissionMapper;
import zpi.ppea.clap.mappers.DtoMapper;
import zpi.ppea.clap.mappers.SubmissionMapper;
import zpi.ppea.clap.security.TokenDecoder;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    private final BusinessLogicService businessLogicService;
    private final TokenDecoder tokenDecoder;

    public List<SubmissionDto> getSubmissions() {
        var allSubmissionsGrpc = dataStoreFutureStub.getSubmissions(
            SubmissionRequest.newBuilder()
                .setAssessorEmail(tokenDecoder.getEmail())
                .build()
        );

        try {
            return SubmissionMapper.submissionListToDtos(allSubmissionsGrpc.get().getSubmissionsList());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public DetailsSubmissionResponseDto getDetailedSubmission(Integer submissionId) {
        checkAccessToResource(submissionId);
        var detailsSubmissionResponse = dataStoreFutureStub.getSubmissionDetails(
                DetailsSubmissionRequest.newBuilder().setSubmissionId(submissionId).build()
        );

        DetailsSubmissionResponseDto dto;
        try {
            dto = DetailedSubmissionMapper.mapToDto(detailsSubmissionResponse.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        dto.setPoints(businessLogicService.calculateSubmissionRating(submissionId));
        return dto;
    }


    public RatingsSubmissionResponseDto getSubmissionRatings(Integer submissionId) {
        checkAccessToResource(submissionId);

        var ft = dataStoreFutureStub.getSubmissionRatings(
            RatingsSubmissionRequest.newBuilder()
            .setSubmissionId(submissionId)
            .build()
        );

        RatingsSubmissionResponse resp;

        try {
            resp = ft.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return RatingsSubmissionResponseDto.builder()
            .criteria(resp.getCriteriaList() == null
                ? List.of()
                : resp.getCriteriaList()
                    .stream()
                    .map(s -> DtoMapper.INSTANCE.criterionToDto(s) )
                    .collect(Collectors.toList())
            )
            .individual(DtoMapper.INSTANCE.assessorRatingsListToDtos(resp.getIndividualList()))
            .initial(DtoMapper.INSTANCE.assessorRatingsToDtos(resp.getInitial()))
            .finalRating(DtoMapper.INSTANCE.assessorRatingsToDtos(resp.getInitial()))
            .build();
    }


    private void checkAccessToResource(Integer submissionId) {
        var allAssessorsSubmissions = dataStoreFutureStub.getSubmissions(
                SubmissionRequest.newBuilder().setAssessorEmail(tokenDecoder.getEmail()).build()
        );
        try {
            if (allAssessorsSubmissions.get().getSubmissionsList().stream()
                    .noneMatch(submission -> submission.getSubmissionId() == submissionId)) {
                throw new NoAccessToResource("No access to this resource");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
