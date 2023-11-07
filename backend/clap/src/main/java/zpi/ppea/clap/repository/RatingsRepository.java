package zpi.ppea.clap.repository;

import data_store.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Repository;
import zpi.ppea.clap.exceptions.GrpcConcurrentException;

import java.util.concurrent.ExecutionException;

@Repository
@RequiredArgsConstructor
public class RatingsRepository {

    @GrpcClient("dataStore")
    DataStoreGrpc.DataStoreFutureStub dataStoreFutureStub;

    public RatingsSubmissionResponse submissionRatings(Integer submissionId) {
        try {
            return dataStoreFutureStub.getSubmissionRatings(
                    RatingsSubmissionRequest.newBuilder()
                            .setSubmissionId(submissionId)
                            .build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new GrpcConcurrentException("Error while receiving data from datastore");
        }
    }

    public Rating createNewRating(Integer submissionId, Integer assessorId, RatingType newType) {
        try {
            return dataStoreFutureStub.postNewSubmissionRating(
                    NewSubmissionRatingRequest.newBuilder()
                            .setSubmissionId(submissionId).setAssessorId(assessorId).setType(newType).build()
            ).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new GrpcConcurrentException("Error while receiving data from datastore");
        }
    }

    public PartialRating submitRatingDraft(Integer ratingId) {
        try {
            return dataStoreFutureStub.postSubmitRating(SubmitRatingDraft.newBuilder()
                    .setRatingId(ratingId).build()).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new GrpcConcurrentException("Error while receiving data from datastore");
        }
    }
}
