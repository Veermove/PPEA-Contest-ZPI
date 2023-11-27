package zpi.ppea.clap;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import zpi.ppea.clap.config.IntegrationTestConfig;

@SpringBootTest
@ContextConfiguration(classes = {IntegrationTestConfig.class})
class ClapApplicationTests {

    @Test
    void contextLoads() {
    }

}
