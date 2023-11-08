package zpi.ppea.clap.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.SubmissionDto;
import zpi.ppea.clap.security.FirebaseAgent;
import zpi.ppea.clap.service.SubmissionService;

import java.util.List;

@RestController
@RequestMapping("/submissions")
@AllArgsConstructor
public class SubmissionController {

    private final FirebaseAgent firebaseAgent;
    private final SubmissionService submissionService;

    @GetMapping
    public ResponseEntity<List<SubmissionDto>> getSubmissions(@RequestHeader(name = "Authorization") String bearerToken) {
        var authentication = firebaseAgent.authenticate(bearerToken);
        return ResponseEntity.ok()
                .header(FirebaseAgent.REFRESH_TOKEN_NAME, authentication.getRefresh())
                .body(submissionService.getSubmissions(authentication));
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<DetailsSubmissionResponseDto> getDetailedSubmission(
            @RequestHeader(name = "Authorization") String bearerToken,
            @PathVariable Integer submissionId) {
        var authentication = firebaseAgent.authenticate(bearerToken);
        return ResponseEntity.ok()
                .header(FirebaseAgent.REFRESH_TOKEN_NAME, authentication.getRefresh())
                .body(submissionService.getDetailedSubmission(submissionId, authentication));
    }

}
