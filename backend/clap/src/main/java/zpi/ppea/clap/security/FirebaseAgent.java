package zpi.ppea.clap.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import data_store.UserClaimsResponse;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.exceptions.UserNotAuthorizedException;
import zpi.ppea.clap.service.DataStoreService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FirebaseAgent {

    private final Logger log = LogManager.getLogger("firebase-app");
    private final DataStoreService client;

    @SneakyThrows
    @PostConstruct
    public void init() {
        FirebaseOptions options = null;
        try (var input = this.getClass()
                .getClassLoader()
                .getResourceAsStream("service-account.json")) {

            if (input != null)
                options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(input))
                    .build();
        }

        if (options == null) {
            throw new NoAccessToResource(new Exception("failed to initialize firebase app"), "");
        }

        FirebaseApp.initializeApp(options);

        log.info("Successfully initialized firebase app");
    }

    @Data
    @AllArgsConstructor
    public static class UserAuthData {
        private UserClaimsResponse claims ;
        private String refresh;
    }

    public UserAuthData authenticate(String bearerToken) {
        if (!bearerToken.startsWith("Bearer ")) {
            throw new UserNotAuthorizedException(new Exception("Only bearer token authorization is recognized"), "");
        }

        FirebaseToken decodedToken;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(
                    bearerToken.replaceFirst("Bearer ", ""));

        } catch (FirebaseAuthException e) {
            throw new UserNotAuthorizedException(e, "");
        }

        var claims = decodedToken.getClaims();
        var authData = new UserAuthData(null, "");

        if (claims == null || !isUserAuthorized(claims)) {

            log.info(String.format("user %s was not authenticated. Querying for permissions", decodedToken.getEmail()));

            var userIds = client.getUserClaims(authData, decodedToken.getEmail());
            claims = new HashMap<String, Object>();
            claims.put("authdone", true);
            claims.put("first_name", userIds.getFirstName());
            claims.put("last_name", userIds.getLastName());
            claims.put("person_id", userIds.getPersonId());
            claims.put("assessor_id", userIds.getAssessorId());
            claims.put("awards_representative_id", userIds.getAwardsRepresentativeId());
            claims.put("jury_member_id", userIds.getJuryMemberId());
            claims.put("ipma_expert_id", userIds.getIpmaExpertId());
            claims.put("applicant_id", userIds.getApplicantId());

            try {
                FirebaseAuth.getInstance().setCustomUserClaims(decodedToken.getUid(), claims);
            } catch (FirebaseAuthException e) {
                throw new UserNotAuthorizedException(e, "");
            }

            return new UserAuthData(userIds, "true");
        }

        authData.setRefresh("");
        authData.setClaims(UserClaimsResponse.newBuilder()
            .setFirstName((String) claims.get("first_name"))
            .setLastName((String) claims.get("last_name"))
            .setPersonId(((Number) claims.get("person_id")).intValue())
            .setAssessorId(((Number) claims.get("assessor_id")).intValue())
            .setAwardsRepresentativeId(((Number) claims.get("awards_representative_id")).intValue())
            .setJuryMemberId(((Number) claims.get("jury_member_id")).intValue())
            .setIpmaExpertId(((Number) claims.get("ipma_expert_id")).intValue())
            .setApplicantId(((Number) claims.get("applicant_id")).intValue())
            .build());

        return authData;
    }


    public Boolean isUserAuthorized(Map<String, Object> claims) {
        return claims.containsKey("authdone")
            && Optional.ofNullable(claims.get("authdone"))
                .map(o -> (
                    o instanceof String && "true".equals(((String) o))
                    || o instanceof Boolean && (Boolean) o
                )).orElse(false);
    }
}
