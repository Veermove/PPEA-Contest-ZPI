package zpi.ppea.clap.repository;

import data_store.DataStoreGrpc;
import data_store.StudyVisitRequest;
import data_store.StudyVisitResponse;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Repository;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class StudyVisitRepository {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    public StudyVisitResponse getStudyVisits(Integer submissionId, FirebaseAgent.UserAuthData authentication) {
        try {
            return dataStoreFutureStub.getStudyVisit(
                    StudyVisitRequest.newBuilder()
                            .setAssessorId(authentication.getClaims().getAssessorId())
                            .setSubmissionId(submissionId)
                            .build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new NoAccessToResource(e, authentication.getRefresh());
        }
    }

}
