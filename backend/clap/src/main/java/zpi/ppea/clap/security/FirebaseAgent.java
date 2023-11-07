package zpi.ppea.clap.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import data_store.UserClaimsResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.exceptions.UserNotAuthorizedException;
import zpi.ppea.clap.service.DataStoreClient;

import java.util.HashMap;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FirebaseAgent {

    private final Logger log = LogManager.getLogger("firebase-app");
    private final DataStoreClient client;

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
            throw new NoAccessToResource("failed to initialize firebase app");
        }

        FirebaseApp.initializeApp(options);

        log.info("Successfully initialized firebase app");
    }

    public UserClaimsResponse authenticate(String bearerToken)  {
        if (!bearerToken.startsWith("Bearer ")) {
            throw new UserNotAuthorizedException("Only bearer token authorization is recognized");
        }

        FirebaseToken decodedToken;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(
                bearerToken.replaceFirst("Bearer ", "")
            );

        } catch (FirebaseAuthException e) {
            throw new UserNotAuthorizedException(e.getMessage());
        }

        var claims = decodedToken.getClaims();
        var authDone = claims.get("authdone");

        if (authDone == null || !Objects.equals(authDone, Boolean.TRUE)) {

            log.debug(String.format("user %s was not authenticated. Querying for permissions", decodedToken.getEmail()));

            var userIds = client.getUserClaims(decodedToken.getEmail());
            claims = new HashMap<String, Object>(claims);
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
                throw new UserNotAuthorizedException(e.getMessage());
            }

            return userIds;
        }

        return UserClaimsResponse.newBuilder()
            .setFirstName((String) claims.get("first_name"))
            .setLastName((String) claims.get("last_name"))
            .setPersonId((Integer) claims.get("person_id"))
            .setAssessorId((Integer) claims.get("assessor_id"))
            .setAwardsRepresentativeId((Integer) claims.get("awards_representative_id"))
            .setJuryMemberId((Integer) claims.get("jury_member_id"))
            .setIpmaExpertId((Integer) claims.get("ipma_expert_id"))
            .setApplicantId((Integer) claims.get("applicant_id"))
            .build();
    }
}
