package zpi.ppea.clap.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ValueConfig {

    @Value("${frontend.address}")
    private String frontendAddress;

}