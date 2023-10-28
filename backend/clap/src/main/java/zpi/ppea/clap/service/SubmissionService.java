package zpi.ppea.clap.service;

import data_store.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.config.ValueConfig;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
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

        return submissionDtos;
    }

    public DetailsSubmissionResponseDto getDetailedSubmission(Integer submissionId) {
        DetailsSubmissionResponse detailsSubmissionResponse = dataStoreBlockingStub.getSubmissionDetails(
                DetailsSubmissionRequest.newBuilder().setSubmissionId(submissionId).build()
        );

        return DetailedSubmissionMapper.mapToDto(detailsSubmissionResponse);
    }

}
