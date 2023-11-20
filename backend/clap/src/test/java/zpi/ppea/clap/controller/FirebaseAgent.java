package zpi.ppea.clap.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("test")
public class FirebaseAgent {

    @PostConstruct
    public void init() {

    }

}
