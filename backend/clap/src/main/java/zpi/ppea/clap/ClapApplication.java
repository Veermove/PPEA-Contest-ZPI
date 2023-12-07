package zpi.ppea.clap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClapApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClapApplication.class, args);
    }
}
