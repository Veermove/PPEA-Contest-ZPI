package zpi.ppea.clap.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zpi.ppea.clap.mails.EmailServiceImpl;

@RestController
@RequestMapping("/admin/email")
@AllArgsConstructor
public class EmailController {

    private final EmailServiceImpl emailService;

    @GetMapping
    public ResponseEntity<Void> sendEmailManually() {
        emailService.sendHtmlMessageFromFile();
        return ResponseEntity.noContent().build();
    }

}
