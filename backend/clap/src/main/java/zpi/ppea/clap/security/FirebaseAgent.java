package zpi.ppea.clap.security;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.apache.logging.log4j.LogManager;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.exceptions.UserNotAuthorizedException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FirebaseAgent {

    private final Logger log = LogManager.getLogger("firebase-app");

    public static class Claims {}

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

    public Claims authenticate(String bearerToken)  {
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
//        Map<String, Object> claims = new HashMap<>();

        Optional.ofNullable(claims.get("auth")).orElseGet(() -> {
            // Means that "auth" was not present in claims,
            // meaning we need to get user claims from database


            return true;
        })
        FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);


        return null;
    }
}
