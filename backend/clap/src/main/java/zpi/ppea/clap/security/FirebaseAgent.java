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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.config.ValueConfig;
import zpi.ppea.clap.exceptions.FirebaseConnectionLost;
import zpi.ppea.clap.exceptions.UserNotAuthorizedException;
import zpi.ppea.clap.service.DataStoreService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Profile("!test")
@RequiredArgsConstructor
public class FirebaseAgent {

    private final DataStoreService client;
    private final ValueConfig valueConfig;

    @SneakyThrows
    @PostConstruct
    public void init() {
        FirebaseOptions options = null;
        try (var input = this.getClass().getClassLoader().getResourceAsStream("service-account.json")) {
            if (input != null)
                options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(input)).build();
        }

        if (options == null)
            throw new FirebaseConnectionLost(new Exception("Failed to initialize firebase app, check credentials"), "");

        FirebaseApp.initializeApp(options);
        log.info("Successfully initialized firebase app");
    }

    @Data
    @AllArgsConstructor
    public static class UserAuthData {
        private UserClaimsResponse claims;
        private String refresh;
    }

    public UserAuthData authenticate(String bearerToken) {
        if (!bearerToken.startsWith("Bearer "))
            throw new UserNotAuthorizedException(new Exception("Only bearer token authorization is recognized"), "");

        FirebaseToken decodedToken;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(bearerToken.replaceFirst("Bearer ", ""));
        } catch (FirebaseAuthException e) {
            throw new UserNotAuthorizedException(e, "");
        }

        var claims = decodedToken.getClaims();
        var authData = new UserAuthData(null, "");

        if (claims == null || !isUserAuthorized(claims)) {

            log.info(String.format("user %s was not authenticated. Querying for permissions", decodedToken.getEmail()));

            var userIds = client.getUserClaims(authData, decodedToken.getEmail());
            claims = new HashMap<>();
            claims.put(valueConfig.getAuthDone(), true);
            claims.put(valueConfig.getFirstName(), userIds.getFirstName());
            claims.put(valueConfig.getLastName(), userIds.getLastName());
            claims.put(valueConfig.getPersonId(), userIds.getPersonId());
            claims.put(valueConfig.getAssessorId(), userIds.getAssessorId());
            claims.put(valueConfig.getAwardsRepresentativeId(), userIds.getAwardsRepresentativeId());
            claims.put(valueConfig.getJuryMemberId(), userIds.getJuryMemberId());
            claims.put(valueConfig.getIpmaExpertId(), userIds.getIpmaExpertId());
            claims.put(valueConfig.getApplicantId(), userIds.getApplicantId());

            try {
                FirebaseAuth.getInstance().setCustomUserClaims(decodedToken.getUid(), claims);
            } catch (FirebaseAuthException e) {
                throw new UserNotAuthorizedException(e, "");
            }

            return new UserAuthData(userIds, "true");
        }

        authData.setRefresh("");
        authData.setClaims(UserClaimsResponse.newBuilder()
                .setFirstName((String) claims.get(valueConfig.getFirstName()))
                .setLastName((String) claims.get(valueConfig.getLastName()))
                .setPersonId(((Number) claims.get(valueConfig.getPersonId())).intValue())
                .setAssessorId(((Number) claims.get(valueConfig.getAssessorId())).intValue())
                .setAwardsRepresentativeId(((Number) claims.get(valueConfig.getAwardsRepresentativeId())).intValue())
                .setJuryMemberId(((Number) claims.get(valueConfig.getJuryMemberId())).intValue())
                .setIpmaExpertId(((Number) claims.get(valueConfig.getIpmaExpertId())).intValue())
                .setApplicantId(((Number) claims.get(valueConfig.getApplicantId())).intValue())
                .build());

        return authData;
    }


    public Boolean isUserAuthorized(Map<String, Object> claims) {
        return claims.containsKey(valueConfig.getAuthDone()) && Optional.ofNullable(claims.get(valueConfig.getAuthDone()))
                .map(o -> ("true".equals(o) || o instanceof Boolean b && b)).orElse(false);
    }
}
