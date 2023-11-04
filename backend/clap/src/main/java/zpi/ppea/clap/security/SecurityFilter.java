package zpi.ppea.clap.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import zpi.ppea.clap.config.ValueConfig;

import java.io.IOException;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final ValueConfig valueConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
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
                valueConfig.setFirebaseEmail(claims.getClaimValue("email").toString());

        } catch (InvalidJwtException e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);
    }
}
