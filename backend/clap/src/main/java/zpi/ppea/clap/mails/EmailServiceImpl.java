package zpi.ppea.clap.mails;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import zpi.ppea.clap.repository.EmailRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@AllArgsConstructor
public class EmailServiceImpl {

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;

    public void sendHtmlMessageFromFile() throws MessagingException, IOException {
        var emailsToSentList = emailRepository.getEmailsToSent();

        String htmlFilePath = "templates/reminder_template.html";
        String to = "recipient@example.com";
        String subject = "Subject";

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("ppea-competition");
        helper.setTo(to);
        helper.setSubject(subject);

        String htmlContent = readHtmlFile(htmlFilePath);

        // Set values
        htmlContent = htmlContent.replace("[AssessorName]", "Asesor");
        htmlContent = htmlContent.replace("[AssessorSurname]", "Pierwszy");

        helper.setText(htmlContent, true);

        emailSender.send(message);
    }

    private String readHtmlFile(String filePath) throws IOException {
        Resource resource = new ClassPathResource(filePath);
        byte[] contentBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(contentBytes, StandardCharsets.UTF_8);
    }

}
