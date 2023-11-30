package zpi.ppea.clap.mails;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class MailCronJob {

    private final EmailServiceImpl emailService;

    @Scheduled(cron = "${cron.job.mail}")
    public void checkEmails() {
        log.info("Sending emails");
        emailService.sendHtmlMessageFromFile();
    }

}
