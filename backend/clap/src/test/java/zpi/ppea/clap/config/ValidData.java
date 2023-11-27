package zpi.ppea.clap.config;

import data_store.*;
import zpi.ppea.clap.dtos.UpdatePartialRatingDto;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.List;

public class ValidData {
    public static final String AUTH_HEADER = "Authorization";
    public static final String VALID_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    public static final FirebaseAgent.UserAuthData ASSESSOR_AUTH = new FirebaseAgent.UserAuthData
            (UserClaimsResponse.newBuilder().setAssessorId(1).build(), "false");
    public static final RatingsSubmissionResponse RATINGS_SUBMISSION_RESPONSE =  RatingsSubmissionResponse.newBuilder()
            .addAllCriteria(List.of())
            .setFinal(AssessorRatings.newBuilder().setAssessorId(1).setIsDraft(false).setRatingId(1).build())
            .addAllIndividual(List.of())
            .setInitial(AssessorRatings.newBuilder().setRatingId(1).build())
            .build();

    public static final Rating RATING1 = Rating.newBuilder()
            .setRatingId(1)
            .setIsDraft(true)
            .setType(RatingType.INITIAL)
            .setAssessorId(1)
            .build();

    public static final Rating RATING2 = Rating.newBuilder()
            .setRatingId(2)
            .setIsDraft(false)
            .setType(RatingType.INDIVIDUAL)
            .setAssessorId(1)
            .build();

    public static final UpdatePartialRatingDto UPDATE_PARTIAL_RATING_DTO = UpdatePartialRatingDto.builder()
            .partialRatingId(1)
            .points(100)
            .justification("Very good work.")
            .criterionId(1)
            .build();

    public static final PartialRating PARTIAL_RATING = PartialRating.newBuilder().setPartialRatingId(1)
            .setPoints(100)
            .setJustification("Very good work.")
            .setCriterionId(1)
            .buildPartial();

    public static final SubmissionsResponse SUBMISSIONS_RESPONSE = SubmissionsResponse.newBuilder()
            .addAllSubmissions(List.of(
                    Submission.newBuilder().setSubmissionId(1).setName("sub1").setYear(2023).setDurationDays(30)
                            .addAllAssessors(List.of(Assessor.newBuilder().setAssessorId(1).build()))
                            .addAllRatings(List.of(ValidData.RATING1))
                            .build(),
                    Submission.newBuilder().setSubmissionId(2).setName("sub2").setYear(2022).setDurationDays(20)
                            .addAllAssessors(List.of(Assessor.newBuilder().setAssessorId(1).build()))
                            .addAllRatings(List.of(ValidData.RATING2))
                            .build()
            ))
            .build();

    public static final DetailsSubmissionResponse DETAILS_SUBMISSION_RESPONSE = DetailsSubmissionResponse.newBuilder()
            .setTeamSize(5)
            .setFinishDate("2023-12-20")
            .setStatus(ProjectState.DRAFT)
            .setBudget("10000")
            .setDescription("New fast drone")
            .setPoints(100)
            .setReport(AppReport.newBuilder().setIsDraft(true).build())
            .build();
    public static final StudyVisitResponse STUDY_VISIT_RESPONSE = StudyVisitResponse.newBuilder()
            .addAllQuestions(List.of(
                    Question.newBuilder().setContent("Was the project made with the newest technology?")
                            .addAllAnswers(List.of(
                                    Answer.newBuilder().setAnswerText("No, there are some old libraries").build(),
                                    Answer.newBuilder().setAnswerText("No, definitely no").build()
                            )).build(),
                    Question.newBuilder().setContent("Was the project well documented?")
                            .addAllAnswers(List.of(
                                    Answer.newBuilder().setAnswerText("Yes, there are necessary files").addAllFiles(
                                            List.of("file1", "file2")
                                    ).build()
                            )).build()
            ))
            .build();

    public static final PartialRatingRequest PARTIAL_RATING_REQUEST = PartialRatingRequest.newBuilder()
            .setAssessorId(1)
            .setPartialRatingId(1)
            .setPoints(100)
            .setJustification("Very good work.")
            .setCriterionId(1)
            .setModified("")
            .build();

}
