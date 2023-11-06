package zpi.ppea.clap.repository;

import data_store.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Repository;
import zpi.ppea.clap.exceptions.GrpcConcurrentException;
import zpi.ppea.clap.security.TokenDecoder;

import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class SubmissionRepository {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;
    private final TokenDecoder tokenDecoder;

    public SubmissionsResponse allSubmissions() {
        try {
            return dataStoreFutureStub.getSubmissions(
                    SubmissionRequest.newBuilder()
                            .setAssessorEmail(tokenDecoder.getEmail())
                            .build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new GrpcConcurrentException("Error while receiving data from datastore");
        }
    }

    public DetailsSubmissionResponse getDetailedSubmission(Integer submissionId) {
        try {
            return dataStoreFutureStub.getSubmissionDetails(
                    DetailsSubmissionRequest.newBuilder().setSubmissionId(submissionId).build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new GrpcConcurrentException("Error while receiving data from datastore");
        }
    }

}
