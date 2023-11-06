package zpi.ppea.clap.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zpi.ppea.clap.dtos.RatingsSubmissionResponseDto;
import zpi.ppea.clap.service.RatingService;

@RestController
@RequestMapping("/ratings")
@AllArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/{submissionId}")
    public ResponseEntity<RatingsSubmissionResponseDto> getSubmissionRatings(@PathVariable Integer submissionId) {
        return ResponseEntity.ok(ratingService.getSubmissionRatings(submissionId));
    }

}
