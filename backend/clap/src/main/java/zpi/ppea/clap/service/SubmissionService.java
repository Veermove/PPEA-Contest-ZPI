package zpi.ppea.clap.service;

import data_store.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.config.ValueConfig;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.SubmissionDto;
import zpi.ppea.clap.logic.BusinessLogicService;
import zpi.ppea.clap.mappers.DetailedSubmissionMapper;
import zpi.ppea.clap.mappers.SubmissionMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreBlockingStub dataStoreBlockingStub;

    private final ValueConfig valueConfig;
    private final BusinessLogicService businessLogicService;

    public List<SubmissionDto> getSubmissions() {
        SubmissionsResponse allSubmissionsGrpc = dataStoreBlockingStub.getSubmissions(
                SubmissionRequest.newBuilder().setAssessorEmail(valueConfig.getFirebaseEmail()).build()
        );

        return SubmissionMapper.submissionListToDtos(allSubmissionsGrpc.getSubmissionsList());
    }

    public DetailsSubmissionResponseDto getDetailedSubmission(Integer submissionId) {
        DetailsSubmissionResponse detailsSubmissionResponse = dataStoreBlockingStub.getSubmissionDetails(
                DetailsSubmissionRequest.newBuilder().setSubmissionId(submissionId).build()
        );

        DetailsSubmissionResponseDto dto = DetailedSubmissionMapper.mapToDto(detailsSubmissionResponse);
        dto.setPoints(businessLogicService.getRatingPointsByArea(submissionId));
        return dto;
    }

}
