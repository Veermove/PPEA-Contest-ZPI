package zpi.ppea.clap.service;

import backend.clap.SubmissionList;
import backend.clap.SubmissionServiceGrpc;
import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.mapstruct.dtos.SubmissionDTO;
import zpi.ppea.clap.mapstruct.mappers.SubmissionDtoMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    @GrpcClient("submissionService")
    SubmissionServiceGrpc.SubmissionServiceBlockingStub submissionServiceBlockingStub;

    private final SubmissionDtoMapper submissionDtoMapper;

    public List<SubmissionDTO> getSubmissions() {
        SubmissionList allSubmissionsGrpc = submissionServiceBlockingStub.getAllSubmissions(Empty.newBuilder().build());
        return allSubmissionsGrpc.getSubmissionsList().stream().map(submissionDtoMapper::mapToDTO).toList();
    }
}
