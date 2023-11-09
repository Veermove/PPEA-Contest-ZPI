package zpi.ppea.clap.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@NoArgsConstructor
@ConfigurationProperties(prefix = "caches")
public class CaffeineCacheDto {

    private List<Cache> caffeines;

    @Data
    public static class Cache {
        private String name;
        private Long expiryInMinutes;
    }
}
