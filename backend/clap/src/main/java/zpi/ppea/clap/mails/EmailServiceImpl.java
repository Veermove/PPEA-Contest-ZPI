package zpi.ppea.clap.mails;

import data_store.EmailDetails;
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
import zpi.ppea.clap.config.ValueConfig;
import zpi.ppea.clap.repository.EmailRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
@AllArgsConstructor
public class EmailServiceImpl {

    private final JavaMailSender emailSender;
    private final EmailRepository emailRepository;
    private final ValueConfig valueConfig;

    public void sendHtmlMessageFromFile() {
        var emailResponse = emailRepository.getEmailsToSent();
        if (emailResponse == null)
            return;

        var emailsList = emailResponse.getEmailsList();
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        for (var email : emailsList) {
            try {
                // Normal reminder
                if (dateFormat.parse(email.getRatingSubmitDate()).before(currentDate)) {
                    prepareAndSendEmail("templates/reminder_template.html",
                            "PPEA przypomnienie o wystawieniu oceny", email);
                }
                // Urgent reminder
                else {
                    prepareAndSendEmail("templates/urgent_template.html",
                            "PPEA ponaglenie do wystawienia oceny", email);
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String readHtmlFile(String filePath) throws IOException {
        Resource resource = new ClassPathResource(filePath);
        byte[] contentBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(contentBytes, StandardCharsets.UTF_8);
    }

    private void prepareAndSendEmail(String htmlFilePath, String subject, EmailDetails email) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(valueConfig.getEmailSender());
            helper.setTo(email.getAssessorEmail());
            helper.setSubject(subject);

            String htmlContent = readHtmlFile(htmlFilePath);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
            // Set values
            htmlContent = htmlContent.replace("[AssessorName]", email.getAssessorFirstName());
            htmlContent = htmlContent.replace("[AssessorLastname]", email.getAssessorLastName());
            htmlContent = htmlContent.replace("[SubmissionName]", email.getSubmissionName());
            htmlContent = htmlContent.replace("[RatingType]", email.getRatingType().name());
            htmlContent = htmlContent.replace("[EditionYear]", Integer.toString(email.getEditionYear()));
            htmlContent = htmlContent.replace("[FinishDate]", outputDateFormat.format(email.getRatingSubmitDate()));
            htmlContent = htmlContent.replace("[IsCreatedPl]", email.getIsRatingCreated() ?
                    "Prosimy zatwierdź ocenę" : "Prosimy utwórz i zatwierdź ocenę");
            htmlContent = htmlContent.replace("[IsCreatedEng]", outputDateFormat.format(email.getIsRatingCreated() ?
                    "Please submit rating" : "Please create and submit rating"));
            helper.setText(htmlContent, true);
            emailSender.send(message);

        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
