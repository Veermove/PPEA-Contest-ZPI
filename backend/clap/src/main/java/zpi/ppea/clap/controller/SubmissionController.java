package zpi.ppea.clap.controller;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.RatingsSubmissionResponseDto;
import zpi.ppea.clap.dtos.SubmissionDto;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.exceptions.UserNotAuthorizedException;
import zpi.ppea.clap.security.FirebaseAgent;
import zpi.ppea.clap.service.SubmissionService;

import java.util.List;

@RestController
@RequestMapping("/submissions")
@AllArgsConstructor
@ControllerAdvice
public class SubmissionController {

    private final SubmissionService submissionService;
    private final FirebaseAgent agent;
    private final Logger log = LogManager.getLogger("submissions-controller");

    @GetMapping
    public ResponseEntity<List<SubmissionDto>> getSubmissions(@RequestHeader(name = "Authorization") String bearerToken) {
        return ResponseEntity.ok(submissionService.getSubmissions());
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<DetailsSubmissionResponseDto> getDetailedSubmission(
        @RequestHeader(name = "Authorization") String bearerToken,
        @PathVariable Integer submissionId
    ) {
        return ResponseEntity.ok(submissionService.getDetailedSubmission(submissionId));
    }

    @GetMapping("/ratings/{submissionId}")
    public ResponseEntity<RatingsSubmissionResponseDto> getSubmissionRatings(
        @RequestHeader(name = "Authorization") String bearerToken,
        @PathVariable Integer submissionId
    ) {
        return ResponseEntity.ok(submissionService.getSubmissionRatings(submissionId));
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    public ResponseEntity<String> handleUserNotAuthorizedException(UserNotAuthorizedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoAccessToResource.class)
    public ResponseEntity<String> handleNoAccessToResourceException(NoAccessToResource ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

}
