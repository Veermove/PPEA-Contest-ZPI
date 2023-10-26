package zpi.ppea.clap.service;

import data_store.*;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.util.Arrays;

@Slf4j
@GRpcService
public class GrpcServerMock extends DataStoreGrpc.DataStoreImplBase {

    @Override
    public void getUserClaims(UserRequest request, StreamObserver<UserClaims> responseObserver) {
        responseObserver.onNext(UserClaims.newBuilder()
                .setAssessorId(1)
                .setFirstName("Jan")
                .setLastName("Nowak")
                .setPersonId(1)
                .setApplicantId(1)
                .setAwardsRepresentativeId(1)
                .setIpmaExpertId(1)
                .setJuryMemberId(1)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void getSubmissions(SubmissionRequest request, StreamObserver<SubmissionsResponse> responseObserver) {
        SubmissionsResponse submissionResponse = SubmissionsResponse.newBuilder()
                .addAllSubmissions(Arrays.asList(
                        Submission.newBuilder()
                                .setSubmissionId(1)
                                .setYear(2023)
                                .setName("Sample Submission 1")
                                .setDurationDays(30)
                                .addAssessors(Assessor.newBuilder()
                                        .setFirstName("John")
                                        .setLastName("Doe")
                                        .setAssessorId(101)
                                        .build())
                                .addRatings(Rating.newBuilder()
                                        .setRatingId(1)
                                        .setIsDraft(true)
                                        .setAssessorId(101)
                                        .setType(RatingType.INDIVIDUAL)
                                        .build())
                                .build(),
                        Submission.newBuilder()
                                .setSubmissionId(2)
                                .setYear(2023)
                                .setName("Sample Submission 2")
                                .setDurationDays(45)
                                .addAssessors(Assessor.newBuilder()
                                        .setFirstName("Jane")
                                        .setLastName("Smith")
                                        .setAssessorId(102)
                                        .build())
                                .addRatings(Rating.newBuilder()
                                        .setRatingId(2)
                                        .setIsDraft(false)
                                        .setAssessorId(102)
                                        .setType(RatingType.FINAL)
                                        .build())
                                .build()
                ))
                .build();
        responseObserver.onNext(submissionResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getSubmissionDetails(DetailsSubmissionRequest request, StreamObserver<DetailsSubmissionResponse> responseObserver) {
        super.getSubmissionDetails(request, responseObserver);
    }

    @Override
    public void getSubmissionRatings(RatingsSubmissionRequest request, StreamObserver<RatingsSubmissionResponse> responseObserver) {
        super.getSubmissionRatings(request, responseObserver);
    }
}
