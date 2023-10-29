package zpi.ppea.clap.service;

import data_store.DataStoreGrpc;
import data_store.DetailsSubmissionRequest;
import data_store.SubmissionRequest;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.SubmissionDto;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.logic.BusinessLogicService;
import zpi.ppea.clap.mappers.DetailedSubmissionMapper;
import zpi.ppea.clap.mappers.SubmissionMapper;
import zpi.ppea.clap.security.TokenDecoder;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;
    
    private final BusinessLogicService businessLogicService;
    private final TokenDecoder tokenDecoder;

    public List<SubmissionDto> getSubmissions() {
        var allSubmissionsGrpc = dataStoreFutureStub.getSubmissions(
                SubmissionRequest.newBuilder().setAssessorEmail(tokenDecoder.getEmail()).build()
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
