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
public class RatingsRepository {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    public RatingsSubmissionResponse submissionRatings(Integer submissionId, FirebaseAgent.UserAuthData authentication) {
        try {
            return dataStoreFutureStub.getSubmissionRatings(
                    RatingsSubmissionRequest.newBuilder()
                            .setAssessorId(authentication.getClaims().getAssessorId())
                            .setSubmissionId(submissionId)
                            .build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new NoAccessToResource(e, authentication.getRefresh());
        }
    }

    public Rating createNewRating(Integer submissionId, FirebaseAgent.UserAuthData authentication, RatingType newType) {
        try {
            return dataStoreFutureStub.postNewSubmissionRating(
                    NewSubmissionRatingRequest.newBuilder().setSubmissionId(submissionId)
                            .setAssessorId(authentication.getClaims().getAssessorId()).setType(newType).build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new NoAccessToResource(e, authentication.getRefresh());
        }
    }

    public PartialRating submitRatingDraft(Integer ratingId, FirebaseAgent.UserAuthData authentication) {
        try {
            return dataStoreFutureStub.postSubmitRating(SubmitRatingDraft.newBuilder()
                    .setRatingId(ratingId).setAssessorId(authentication.getClaims().getAssessorId()).build()).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new NoAccessToResource(e, authentication.getRefresh());
        }
    }
}
