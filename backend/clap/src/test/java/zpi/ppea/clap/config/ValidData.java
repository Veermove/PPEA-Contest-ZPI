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

    public static final Rating RATING = Rating.newBuilder()
            .setRatingId(1)
            .setIsDraft(true)
            .setType(RatingType.INITIAL)
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
}
