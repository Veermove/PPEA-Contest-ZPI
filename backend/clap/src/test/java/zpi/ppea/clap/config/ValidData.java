package zpi.ppea.clap.config;

import data_store.UserClaimsResponse;
import zpi.ppea.clap.dtos.AssessorRatingsDto;
import zpi.ppea.clap.dtos.RatingsSubmissionResponseDto;
import zpi.ppea.clap.security.FirebaseAgent;

import java.util.List;

public class ValidData {
    public static final String AUTH_HEADER = "Authorization";
    public static final String VALID_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
    public static final FirebaseAgent.UserAuthData ASSESSOR_AUTH = new FirebaseAgent.UserAuthData
            (UserClaimsResponse.newBuilder().setAssessorId(1).build(), "false");
    public static final RatingsSubmissionResponseDto ratingsSubmissionResponse =  RatingsSubmissionResponseDto
            .builder()
            .criteria(List.of())
            .finalRating(AssessorRatingsDto.builder().ratingId(1).build())
            .individualRatings(List.of())
            .initialRating(null).build();
}
