package zpi.ppea.clap.security;

import lombok.AllArgsConstructor;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import zpi.ppea.clap.config.ValueConfig;

@Component
@AllArgsConstructor
public class TokenDecoder {

    private final ValueConfig valueConfig;

    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var principal = (Jwt) authentication.getPrincipal();
        var token = principal.getTokenValue();

        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build();
        try {
            var claims = jwtConsumer.processToClaims(token);
            if (
                    claims.getClaimValue("iss").equals(valueConfig.getFirebaseIssuer()) &&
                            claims.getClaimValue("aud").equals(valueConfig.getFirebaseAudience()) &&
                            !claims.getClaimValue("email").toString().isEmpty()
            )
                return claims.getClaimValue("email").toString();

        } catch (InvalidJwtException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
