package zpi.ppea.clap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableWebSecurity
public class ClapApplication {

    @Value("${frontend.address}")
    public String FRONTEND_ADDRESS;

    public static void main(String[] args) {
        SpringApplication.run(ClapApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {

                Logger log = LogManager.getLogger("cors");
                log.info(String.format("allowed origins: %s", FRONTEND_ADDRESS));

                registry.addMapping("/**")
                    .allowedOrigins(FRONTEND_ADDRESS)
                    .allowedMethods("GET", "POST", "PUT", "PATCH", "OPTIONS", "DELETE")
                    .allowCredentials(true)
                    .allowedHeaders("*");
            }
        };
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(
                auth -> auth
                    .requestMatchers(HttpMethod.OPTIONS).permitAll()
                    .anyRequest().authenticated()
            )
            .oauth2ResourceServer(server -> server.jwt(Customizer.withDefaults()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionAuthenticationStrategy(new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl())))
            .build();
    }

}
