package zpi.ppea.clap.service;

import data_store.DataStoreGrpc;
import data_store.SubmissionRequest;
import data_store.SubmissionsResponse;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.exceptions.GrpcConcurrentException;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.security.TokenDecoder;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;
    private final TokenDecoder tokenDecoder;

    @Cacheable("access")
    public void checkAccessToResource(Integer submissionId) {
        SubmissionsResponse allAssessorsSubmissions;
        try {
            allAssessorsSubmissions = dataStoreFutureStub.getSubmissions(
                    SubmissionRequest.newBuilder().setAssessorEmail(tokenDecoder.getEmail()).build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new GrpcConcurrentException("Error while receiving data from datastore");
        }

        if (allAssessorsSubmissions.getSubmissionsList().stream().noneMatch(submission -> submission.getSubmissionId() == submissionId)) {
            throw new NoAccessToResource("No access to this resource");
        }
    }

}
