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
        log.info("Email received: {}", request.getEmail());
        UserClaims userClaims = UserClaims.newBuilder()
                .setFirstName("John")
                .setLastName("Doe")
                .setAssessorId(123)
                .setPersonId(456)
                .setAwardsRepresentativeId(789)
                .setJuryMemberId(101)
                .setIpmaExpertId(202)
                .setApplicantId(303)
                .build();
        responseObserver.onNext(userClaims);
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
        DetailsSubmissionResponse detailsResponse = DetailsSubmissionResponse.newBuilder()
                .setTeamSize(5)
                .setFinishDate("2023-12-31")
                .setStatus(ProjectState.ACCEPTED)
                .setBudget("$100,000")
                .setDescription("Sample project description")
                .setReport(AppReport.newBuilder()
                        .setIsDraft(false)
                        .setReportSubmissionDate("2023-12-31")
                        .setProjectGoals("Sample project goals")
                        .setOrganisationStructure("Sample org structure")
                        .setDivisionOfWork("Sample division of work")
                        .setProjectSchedule("Sample project schedule")
                        .setAttatchments("Sample attachments")
                        .build())
                .build();
        responseObserver.onNext(detailsResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getSubmissionRatings(RatingsSubmissionRequest request, StreamObserver<RatingsSubmissionResponse> responseObserver) {
        Criterion criterion = Criterion.newBuilder()
                .setCriterionId(1)
                .setName("Criterion 1")
                .setDescription("Description for Criterion 1")
                .setArea("Area 1")
                .setCriteria("Criteria 1")
                .setSubcriteria("Subcriteria 1")
                .build();

        AssessorRatings individualRatings = AssessorRatings.newBuilder()
                .setAssessorId(1)
                .setFirstName("Assessor")
                .setLastName("One")
                .addPartialRatings(PartialRating.newBuilder()
                        .setPartialRatingId(1)
                        .setCriterionId(1)
                        .setPoints(5)
                        .setJustification("Highly rated")
                        .setModified("2023-10-26")
                        .setModifiedBy(123)
                        .build())
                .build();

        AssessorRatings initialRatings = AssessorRatings.newBuilder()
                .setAssessorId(2)
                .setFirstName("Assessor")
                .setLastName("Two")
                .addPartialRatings(PartialRating.newBuilder()
                        .setPartialRatingId(2)
                        .setCriterionId(1)
                        .setPoints(4)
                        .setJustification("Well-rated")
                        .setModified("2023-10-26")
                        .setModifiedBy(124)
                        .build())
                .build();

        AssessorRatings finalRatings = AssessorRatings.newBuilder()
                .setAssessorId(3)
                .setFirstName("Assessor")
                .setLastName("Three")
                .addPartialRatings(PartialRating.newBuilder()
                        .setPartialRatingId(3)
                        .setCriterionId(1)
                        .setPoints(3)
                        .setJustification("Moderately rated")
                        .setModified("2023-10-26")
                        .setModifiedBy(125)
                        .build())
                .build();

        RatingsSubmissionResponse ratingsResponse = RatingsSubmissionResponse.newBuilder()
                .addCriteria(criterion)
                .addIndividual(individualRatings)
                .setInitial(initialRatings)
                .setFinal(finalRatings)
                .build();

        responseObserver.onNext(ratingsResponse);
        responseObserver.onCompleted();
    }

}
