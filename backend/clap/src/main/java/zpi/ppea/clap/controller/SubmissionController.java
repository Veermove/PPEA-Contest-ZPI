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
import zpi.ppea.clap.service.DataStoreClient;

import java.util.List;

@RestController
@RequestMapping("/submissions")
@AllArgsConstructor
@ControllerAdvice
public class SubmissionController {

    private final DataStoreClient dataStoreClient;
    private final FirebaseAgent agent;
    private final Logger log = LogManager.getLogger("submissions-controller");

    @GetMapping
    public ResponseEntity<List<SubmissionDto>> getSubmissions(
        @RequestHeader(name = "Authorization") String bearerToken
    ) {

        var auth = agent.authenticate(bearerToken);
        var claims = auth.x();
        return ResponseEntity.ok()
            .header("refresh", auth.y() ? "yes" : "")
            .body(dataStoreClient.getSubmissions(claims.getAssessorId()));
    }

    @GetMapping("/{submissionId}")
    public ResponseEntity<DetailsSubmissionResponseDto> getDetailedSubmission(
        @RequestHeader(name = "Authorization") String bearerToken,
        @PathVariable Integer submissionId
    ) {

        var auth = agent.authenticate(bearerToken);
        var claims = auth.x();
        if (claims.getAssessorId() == 0) {
            throw new NoAccessToResource(String.format("user %s %s has no access to this resource",
                claims.getLastName(), claims.getFirstName()));
        }

        return ResponseEntity.ok()
            .header("refresh", auth.y() ? "yes" : "")
            .body(dataStoreClient.getDetailedSubmission(submissionId, claims.getAssessorId()));
    }

    @GetMapping("/ratings/{submissionId}")
    public ResponseEntity<RatingsSubmissionResponseDto> getSubmissionRatings(
        @RequestHeader(name = "Authorization") String bearerToken,
        @PathVariable Integer submissionId
    ) {

        var auth = agent.authenticate(bearerToken);
        var claims = auth.x();
        if (claims.getAssessorId() == 0) {
            throw new NoAccessToResource(String.format("user %s %s has no access to this resource",
                claims.getLastName(), claims.getFirstName()));
        }


        return ResponseEntity.ok()
            .header("refresh", auth.y() ? "yes" : "")
            .body(dataStoreClient.getSubmissionRatings(submissionId, claims.getAssessorId()));
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
