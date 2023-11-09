package zpi.ppea.clap.repository;

import data_store.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Repository;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class SubmissionRepository {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    public SubmissionsResponse allSubmissions(FirebaseAgent.UserAuthData authentication) {
        try {
            return dataStoreFutureStub.getSubmissions(
                    SubmissionRequest.newBuilder()
                            .setAssessorId(authentication.getClaims().getAssessorId())
                            .build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new NoAccessToResource(e, authentication.getRefresh());
        }
    }

    public DetailsSubmissionResponse getDetailedSubmission(Integer submissionId, FirebaseAgent.UserAuthData authentication) {
        try {
            return dataStoreFutureStub.getSubmissionDetails(
                    DetailsSubmissionRequest.newBuilder().setSubmissionId(submissionId)
                            .setAssessorId(authentication.getClaims().getAssessorId()).build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new NoAccessToResource(e, authentication.getRefresh());
        }
    }

}
