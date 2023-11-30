package zpi.ppea.clap.config;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@AllArgsConstructor
@Configuration
public class MailSender {

    private final ValueConfig valueConfigs;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(valueConfigs.getMailSenderHost());
        mailSender.setPort(valueConfigs.getMailSenderPort());
        mailSender.setUsername(valueConfigs.getMailSenderUsername());
        mailSender.setPassword(valueConfigs.getMailSenderPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");

        return mailSender;
    }

}