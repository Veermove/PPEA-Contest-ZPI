package zpi.ppea.clap.config;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import zpi.ppea.clap.security.FirebaseAgent;
import zpi.ppea.clap.service.DataStoreService;

@Configuration
@EnableWebSecurity
public class IntegrationTestConfig {

    @MockBean
    private JwtDecoder jwtDecoder;

    @Bean
    ValueConfig valueConfig() {
        return new ValueConfig();
    }

    @Bean
    FirebaseAgent firebaseAgent() {
        return new FirebaseAgent(new DataStoreService(), valueConfig());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

}
