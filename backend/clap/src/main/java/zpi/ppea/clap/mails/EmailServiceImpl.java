package zpi.ppea.clap.mails;

import data_store.Confirmation;
import data_store.ConfirmationRequest;
import data_store.ConfirmationResponse;
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
import java.util.ArrayList;
import java.util.List;

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
        List<Confirmation> confirmations = new ArrayList<>();
        Confirmation confirmation;
        log.info("Amount of emails: {}", emailsList.size());

        for (var email : emailsList) {
            // Normal reminder
            if (email.getIsFirstWarning()) {
                confirmation = prepareAndSendEmail("templates/reminder_template.html",
                        "PPEA przypomnienie o wystawieniu oceny ||| PPEA rating reminder", email);
            }
            // Urgent reminder
            else {
                confirmation = prepareAndSendEmail("templates/urgent_template.html",
                        "PPEA ponaglenie do wystawienia oceny ||| PPEA urgent rating reminder", email);
            }
            confirmations.add(confirmation);
        }
        var wereConfirmationsSent = sendConfirmations(confirmations);
        if (!wereConfirmationsSent)
            throw new RuntimeException("Couldn't sent emails");
    }

    private String readHtmlFile(String filePath) throws IOException {
        Resource resource = new ClassPathResource(filePath);
        byte[] contentBytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(contentBytes, StandardCharsets.UTF_8);
    }

    private Confirmation prepareAndSendEmail(String htmlFilePath, String subject, EmailDetails email) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(valueConfig.getEmailSender());
            helper.setTo(email.getAssessorEmail());
            helper.setSubject(subject);

            String htmlContent = readHtmlFile(htmlFilePath);
            // Set values
            htmlContent = htmlContent.replace("[AssessorName]", email.getAssessorFirstName());
            htmlContent = htmlContent.replace("[AssessorLastname]", email.getAssessorLastName());
            htmlContent = htmlContent.replace("[SubmissionName]", email.getSubmissionName());
            htmlContent = htmlContent.replace("[RatingType]", email.getRatingType().name());
            htmlContent = htmlContent.replace("[EditionYear]", Integer.toString(email.getEditionYear()));
            htmlContent = htmlContent.replace("[FinishDate]", email.getRatingSubmitDate());
            htmlContent = htmlContent.replace("[IsCreatedPl]", email.getIsRatingCreated() ?
                    "Prosimy zatwierdź ocenę" : "Prosimy utwórz i zatwierdź ocenę");
            htmlContent = htmlContent.replace("[IsCreatedEng]", email.getIsRatingCreated() ?
                    "Please submit rating" : "Please create and submit rating");
            helper.setText(htmlContent, true);

            // Send email and add to confirmation list
            emailSender.send(message);
            return Confirmation.newBuilder().setAssessorId(email.getAssessorId())
                    .setRatingType(email.getRatingType()).setSubmissionId(email.getSubmissionId()).build();

        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean sendConfirmations(List<Confirmation> confirmations) {
        var confirmationRequest = ConfirmationRequest.newBuilder();
        confirmationRequest.addAllConfirmations(confirmations);
        ConfirmationResponse confirmationResponse = emailRepository.sendConfirmation(confirmationRequest.build());
        return !confirmationResponse.getError();
    }

}
