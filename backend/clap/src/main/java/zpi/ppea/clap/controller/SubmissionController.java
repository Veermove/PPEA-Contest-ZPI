package zpi.ppea.clap.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zpi.ppea.clap.mapstruct.dtos.SubmissionDto;
import zpi.ppea.clap.service.SubmissionService;

import java.util.List;

@RestController
@RequestMapping("/submissions")
@AllArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @GetMapping("/{assesorId}")
    public ResponseEntity<List<SubmissionDto>> getSubmissions(@PathVariable Integer assesorId) {
        return ResponseEntity.ok(submissionService.getSubmissions(assesorId));
    }

}
