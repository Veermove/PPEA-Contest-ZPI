package zpi.ppea.clap.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zpi.ppea.clap.dtos.DetailsSubmissionResponseDto;
import zpi.ppea.clap.dtos.SubmissionDto;
import zpi.ppea.clap.service.SubmissionService;

import java.util.List;

@RestController
@RequestMapping("/submissions")
@AllArgsConstructor
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

}
