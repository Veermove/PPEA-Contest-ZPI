package zpi.ppea.clap.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.SubmissionDto;
import zpi.ppea.clap.exceptions.NoAccessToResource;
import zpi.ppea.clap.exceptions.UserNotAuthorizedException;
import zpi.ppea.clap.service.SubmissionService;

import java.util.List;

@RestController
@RequestMapping("/submissions")
@AllArgsConstructor
@ControllerAdvice
public class SubmissionController {

    private final SubmissionService submissionService;

    @GetMapping
    public ResponseEntity<List<SubmissionDto>> getSubmissions() {
        return ResponseEntity.ok(submissionService.getSubmissions());
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<DetailsSubmissionResponseDto> getDetailedSubmission(@PathVariable Integer submissionId) {
        return ResponseEntity.ok(submissionService.getDetailedSubmission(submissionId));
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
