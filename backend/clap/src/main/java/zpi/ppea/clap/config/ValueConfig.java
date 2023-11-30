package zpi.ppea.clap.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ValueConfig {

    @Value("${frontend.address}")
    private String frontendAddress;

    @Value("${firebase.issuer}")
    private String firebaseIssuer;

    @Value("${firebase.audience}")
    private String firebaseAudience;

    @Value("${firebaseAgent.auth_done}")
    private String authDone;

    @Value("${firebaseAgent.first_name}")
    private String firstName;

    @Value("${firebaseAgent.last_name}")
    private String lastName;

    @Value("${firebaseAgent.person_id}")
    private String personId;

    @Value("${firebaseAgent.assessor_id}")
    private String assessorId;

    @Value("${firebaseAgent.awards_representative_id}")
    private String awardsRepresentativeId;

    @Value("${firebaseAgent.jury_member_id}")
    private String juryMemberId;

    @Value("${firebaseAgent.ipma_expert_id}")
    private String ipmaExpertId;

    @Value("${firebaseAgent.applicant_id}")
    private String applicantId;

    @Value("${firebaseAgent.refresh_token_header_name}")
    private String refreshTokenHeaderName;

    @Value("${firebaseAgent.bearer_prefix}")
    private String bearerPrefix;

    @Value("${spring.mail.host}")
    private String mailSenderHost;

    @Value("${spring.mail.port}")
    private Integer mailSenderPort;

    @Value("${spring.mail.username}")
    private String mailSenderUsername;

    @Value("${spring.mail.password}")
    private String mailSenderPassword;

    @Value("${email.sender}")
    private String emailSender;

}
