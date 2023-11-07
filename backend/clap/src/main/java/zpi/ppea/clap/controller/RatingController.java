package zpi.ppea.clap.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zpi.ppea.clap.dtos.NewRatingDto;
import zpi.ppea.clap.dtos.RatingDto;
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

    @PostMapping("/{submissionId}")
    public ResponseEntity<RatingDto> createNewRatingForSubmission(@PathVariable Integer submissionId,
                                                                  @Valid @RequestBody NewRatingDto newRatingDto) {
        return ResponseEntity.ok(ratingService.createNewRating(submissionId, newRatingDto));
    }

}
